package model;

/**
 * Immutable Product class is used to encapsulate store product info
 * @author yotta2
 *
 */
public class Product {

	public Product(String productId, String name, String imageFile, double price) {
		this.productId = productId;
		this.name = name;
		this.imageFile = imageFile;
		this.price = price;
	}

	public String getProductId() {
		return productId;
	}

	public String getName() {
		return name;
	}

	public String getImageFile() {
		return imageFile;
	}

	public double getPrice() {
		return price;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Product [productId=");
		builder.append(productId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", imageFile=");
		builder.append(imageFile);
		builder.append(", price=");
		builder.append(price);
		builder.append("]");
		return builder.toString();
	}

	private String productId;
	private String name;
	private String imageFile;
	private double price;
}
