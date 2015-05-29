package assign4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * The Bank class maintains a list of accounts and the BlockingQueue used to communicate between the main thread and the worker threads.
 * @author Yotta2
 *
 */
public class Bank {

	public static final int ACCOUNT_COUNT = 20;
	public static final int ACCOUNT_INIT_BAL = 1000;
	public static final int TRANS_QUEUE_SIZE = 20;
	private static final Transaction NULL_TRANSACTION = new Transaction(-1, 0, 0);

	public Bank() {
		accounts = new ArrayList<Account>();
		for (int i = 0; i < ACCOUNT_COUNT; i++)
			accounts.add(new Account(i, ACCOUNT_INIT_BAL));
		transQueue = new ArrayBlockingQueue<Transaction>(TRANS_QUEUE_SIZE);
	}

	/**
	 * Get a copy of all accounts in this bank. Note that this just a copy,
	 * modifying this copy will not affect the internal state of the bank
	 * @return
	 */
	public List<Account> getAccounts() {
		return new ArrayList<Account>(accounts);
	}

	/**
	 * Given a transaction list file path and number of worker thread,
	 * process all transactions concurrently
	 */
	public void process(String transFilePath, int workers) throws IOException {
		transQueue.clear();
		latch = new CountDownLatch(workers);
		
		for (int i = 0; i < workers; i++)
			new Worker().start();
		loadTrans(transFilePath, workers);
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print();
	}

	private void loadTrans(String transFilePath, int workers) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(transFilePath));
		while (true) {
			String line = "";
			try {
				line = br.readLine();
			} catch (IOException e) {
				System.err.println("IOException occurred when reading transaction file");
				e.printStackTrace();
			}
			if (line == null)
				break;
			StringTokenizer st = new StringTokenizer(line, " ");
			int fromId = Integer.parseInt(st.nextToken());
			int toId = Integer.parseInt(st.nextToken());
			int amount = Integer.parseInt(st.nextToken());
			try {
				transQueue.put(new Transaction(fromId, toId, amount));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		br.close();
		for (int i = 0; i < workers; i++)
			try {
				transQueue.put(NULL_TRANSACTION);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void print() {
		for (int i = 0; i < ACCOUNT_COUNT; i++)
			System.out.println(accounts.get(i));
	}

	private final class Worker extends Thread {
		@Override
		public void run() {
			while (true) {
				Transaction tran = null;
				try {
					tran = transQueue.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (NULL_TRANSACTION.equals(tran))
					break;
				accounts.get(tran.getFromId()).withdraw(tran.getAmount());
				accounts.get(tran.getToId()).deposit(tran.getAmount());
			}
			latch.countDown();
		}
	}

	public static void main(String args[]) throws IOException {
		String transFilePath = args[0];
		int workers = Integer.parseInt(args[1]);
		
		Bank boa = new Bank();
		boa.process(transFilePath, workers);
	}
	
	private List<Account> accounts;
	private BlockingQueue<Transaction> transQueue;
	private CountDownLatch latch;
}
