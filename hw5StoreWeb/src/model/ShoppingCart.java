package model;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shopping cart is used to store a list of to-be-check-out products of a user
 * @author yotta2
 *
 */
public class ShoppingCart {

	/**
	 * Create an empty shopping cart
	 */
	public ShoppingCart() {
		idToEntry = new ConcurrentHashMap<>();
	}

	/**
	 * add an item to shopping cart
	 */
	public void add(Product product) {
		idToEntry.put(product.getProductId(), new Entry(1, product));
	}

	/**
	 * remove entry
	 */
	public void remove(String id) {
		idToEntry.remove(id);
	}

	/**
	 * update entry amount
	 */
	public void updateEntry(String productId, int amount) {
		idToEntry.get(productId).setAmount(amount);
	}

	/**
	 * check if the cart contains an entry with the specified product id
	 */
	public boolean contains(String id) {
		return idToEntry.containsKey(id);
	}

	/**
	 * Get the specified product entry amount
	 */
	public int getEntryAmount(String id) {
		return idToEntry.get(id).getAmount();
	}

	public Collection<Entry> getAllEntries() {
		return idToEntry.values();
	}

	public final class Entry {
		public Entry(int amount, Product product) {
			this.amount = amount;
			this.product = product;
		}

		
		public int getAmount() {
			return amount;
		}


		public void setAmount(int amount) {
			this.amount = amount;
		}


		public Product getProduct() {
			return product;
		}


		public void setProduct(Product product) {
			this.product = product;
		}


		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Entry [amount=");
			builder.append(amount);
			builder.append(", product=");
			builder.append(product);
			builder.append("]");
			return builder.toString();
		}

		private int amount;
		private Product product;
	}
	
	private Map<String, Entry> idToEntry;
}
