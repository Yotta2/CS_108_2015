package assign4;

/**
 * Thread-safe Account class to store account info.
 * @author Yotta2
 *
 */
public final class Account {

	public Account(int id, int bal) {
		this.id = id;
		this.bal = bal;
		trans = 0;
	}

	public synchronized int getId() {
		return id;
	}

	public synchronized int getBal() {
		return bal;
	}

	public synchronized int getTrans() {
		return trans;
	}

	@Override
	public synchronized String toString() {
		return "acct:" + id + " bal:" + bal + " trans:" + trans;
	}

	public synchronized void deposit(int amount) {
		bal += amount;
		trans++;
	}

	public synchronized void withdraw(int amount) {
		bal -= amount;
		trans++;
	}

	private int id;
	private int bal;
	private int trans;
}
