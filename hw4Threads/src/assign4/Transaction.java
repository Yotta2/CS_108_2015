package assign4;

/**
 * Transaction is a simple immutable class that stores information on each transaction.
 * @author Yotta2
 *
 */
public class Transaction {
	public Transaction(int fromId, int toId, int amount) {
		this.fromId = fromId;
		this.toId = toId;
		this.amount = amount;
	}

	public int getFromId() {
		return fromId;
	}

	public int getToId() {
		return toId;
	}

	public int getAmount() {
		return amount;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Transaction))
			return false;
		Transaction other = (Transaction) obj;
		return (other.getFromId() == fromId)
				&& (other.getToId() == toId)
				&& (other.getAmount() == amount);
	}

	private int fromId;
	private int toId;
	private int amount;
}
