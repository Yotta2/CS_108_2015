package assign4;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class WebFrame extends JFrame {

	private static final String APP_NAME = "WebLoader";
	private static final String LINKS_FILEPATH = "input/links.txt";
	private static final String RUNNING_THREADS_COUNT_INIT = "Running:0";
	private static final String COMPLETED_THREADS_COUNT_INIT = "Completed:0";
	private static final String ELAPSED_INIT = "Elapsed:";
	private static final int LAUCHER_SLEEP_MS = 1000;

	private enum State {
		RUNNING, READY
	}

	public WebFrame(String linksFilepath) {
		super(APP_NAME);

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		model = new DefaultTableModel(new String[]{"url", "status"}, 0);
		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(600,300));
		add(scrollpane);
		loadUrls(linksFilepath);

		singleFetch = new JButton("Single Thread Fetch");
		add(singleFetch);
		singleFetch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setState(State.RUNNING);
				launcher = new Launcher(1);
				launcher.start();
			}

		});

		concurrentFetch = new JButton("Concurrent Fetch");
		add(concurrentFetch);
		concurrentFetch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setState(State.RUNNING);
				launcher = new Launcher(Integer.parseInt(threadLimit.getText()));
				launcher.start();
			}
		});
		
		threadLimit = new JTextField("1");
		add(threadLimit);
		threadLimit.setMaximumSize(new Dimension(60, 20));
		
		runningThreads = new JLabel(RUNNING_THREADS_COUNT_INIT);
		add(runningThreads);

		completedThreads = new JLabel(COMPLETED_THREADS_COUNT_INIT);
		add(completedThreads);
		
		elapsedLabel = new JLabel(ELAPSED_INIT);
		add(elapsedLabel);

		progressBar = new JProgressBar();
		add(progressBar);

		stopButton = new JButton("Stop");
		add(stopButton);
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				launcher.interrupt();
				setState(State.READY);
			}
		});

		setState(State.READY);

		// Could do this:
		setLocationByPlatform(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Set the state of the whole app
	 */
	private void setState(State state) {
		if (state == State.RUNNING) {
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					singleFetch.setEnabled(false);
					concurrentFetch.setEnabled(false);
					stopButton.setEnabled(true);
					runningThreads.setText(RUNNING_THREADS_COUNT_INIT );
					completedThreads.setText(COMPLETED_THREADS_COUNT_INIT);
					elapsedLabel.setText(ELAPSED_INIT);
					progressBar.setMaximum(model.getRowCount());
				}
			});
			
		} else if (state == State.READY) {
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					singleFetch.setEnabled(true);
					concurrentFetch.setEnabled(true);
					stopButton.setEnabled(false);
					progressBar.setValue(0);
				}
			});
		}
		
	}

	/**
	 * Load the urls in link file, and put them in model
	 * @param linksFilepath
	 */
	private void loadUrls(String linksFilepath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(linksFilepath));
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				model.addRow(new Object[]{line, null});
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	/**
	 * A laucher thread to control the maximum concurrent thread pool size
	 *
	 */
	public final class Launcher extends Thread {
		public Launcher(int limit) {
			this.limit = limit;
			runningThreadCount = 0;
		}

		@Override
		public void run() {
			clearTableStatus();
			threadSemaphore = new Semaphore(limit);
			workers = new ArrayList<WebWorker>();
			completedCount = 0;
			startTime = System.currentTimeMillis();
			incrementRunningThreadCount();
			int urlCount = model.getRowCount();
			for (int i = 0; i < urlCount; i++) {
				if (isInterrupted()) {
					interruptAllWorkers();
					break;
				}
				try {
					threadSemaphore.acquire();
				} catch (InterruptedException e) {
					interruptAllWorkers();
					break;
				}
				WebWorker worker = new WebWorker((String)model.getValueAt(i, 0), i, WebFrame.this);
				worker.start();
				workers.add(worker);
			}
			waitForCompletion();
			setFinalState();
			decrementRunningThreadCount();
		}

		private void interruptAllWorkers() {
			for (WebWorker worker : workers)
				worker.interrupt();
		}

		private void setFinalState() {
			// set elapsed
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					long elapsed = System.currentTimeMillis() - startTime;
					elapsedLabel.setText("Elapsed:" + elapsed + "ms");
					
				}
			});
			setState(WebFrame.State.READY);
		}

		private void waitForCompletion() {
			while (getRunningThreadCount() != 1) {
				try {
					sleep(LAUCHER_SLEEP_MS);
				} catch (InterruptedException e) {
					interruptAllWorkers();
					break;
				}
			}
		}

		private int limit;
		private List<WebWorker> workers;
	}

	public synchronized int getRunningThreadCount() {
		return runningThreadCount;
	}

	public void clearTableStatus() {
		int rowCount = model.getRowCount();
		for (int i = 0; i < rowCount; i++)
			model.setValueAt(null, i, 1);
	}

	public synchronized void incrementRunningThreadCount() {
		runningThreadCount++;
		updateRunningLabelLater();
	}

	public synchronized void decrementRunningThreadCount() {
		runningThreadCount--;
		updateRunningLabelLater();
		threadSemaphore.release();
	}
	
	private synchronized void updateRunningLabelLater() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				runningThreads.setText("Running:" + runningThreadCount);
				
			}
		});
	}

	public void updateTable(int row, String status) {
		model.setValueAt(status, row, 1);
	}

	public synchronized void increaseCompletedCount() {
		completedCount++;
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				completedThreads.setText("Completed:" + completedCount);
				progressBar.setValue(completedCount);
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new WebFrame(LINKS_FILEPATH);
			}
		});

	}

	private DefaultTableModel model;
	private JTable table;
	private JButton singleFetch;
	private JButton concurrentFetch;
	private JTextField threadLimit;
	private JLabel runningThreads;
	private JLabel completedThreads;
	private JLabel elapsedLabel;
	private JProgressBar progressBar;
	private JButton stopButton;
	private Launcher launcher;
	private Semaphore threadSemaphore;
	private int runningThreadCount;
	private int completedCount;
	private long startTime;
}
