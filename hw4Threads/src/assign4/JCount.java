package assign4;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JCount extends JPanel {

	private static String DEFAULT_TXT = "100000000";
	private static int MILESTONE_INT = 10000;
	private static int SLEEP_MS = 100;
	
	public JCount() {
		textField = new JTextField(DEFAULT_TXT);
		label = new JLabel("0");
		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (worker.isAlive()) {
					worker.interrupt();
				}
				try {
					worker.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				worker = new Worker();
				worker.start();
			}
		});
		stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (worker.isAlive()) {
					worker.interrupt();
				}
				try {
					worker.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		worker = new Worker();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(textField);
		add(label);
		add(startButton);
		add(stopButton);
	}

	private final class Worker extends Thread {
		@Override
		public void run() {
			end = Integer.parseInt(textField.getText());
			for (int i = 0; i <= end; i++) {
				if (i % MILESTONE_INT == 0) {
					try {
						sleep(SLEEP_MS);
					} catch (InterruptedException e) {
						break;
					}
					final int counter = i;
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							label.setText(String.valueOf(counter));
						}
					});
				}
			}
		}

		private int end;
	}

	private static void createAndShowGUI() {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
		}
		JFrame frame = new JFrame("Counters");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		for (int i = 0; i < 4; i++) {
			frame.add(new JCount());
			frame.add(Box.createRigidArea(new Dimension(0,40)));
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});

	}

	private JTextField textField;
	private JLabel label;
	private JButton startButton;
	private JButton stopButton;
	private Worker worker;
}
