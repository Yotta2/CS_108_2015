package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ProductCatalog class store a list of products fetched from db.
 * @author yotta2
 *
 */
public class ProductCatalog {

	private static final String SELECT_CMD_ALL = "SELECT * FROM PRODUCTS";
	public ProductCatalog() {
		updateProductCatalogLocalCache();
	}

	/**
	 * create a connection to db and fetch the latest product list.
	 */
	public void updateProductCatalogLocalCache() {
		products = new HashMap<String, Product>();
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStatement();
		ResultSet rs;
		try {
			rs = stmt.executeQuery(SELECT_CMD_ALL);
			while (rs.next()) {
				String productId = rs.getString("productid");
				String name = rs.getString("name");
				String imageFile = rs.getString("imagefile");
				double price = rs.getDouble("price");
				products.put(productId, new Product(productId, name, imageFile, price));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		conn.close();
	}

	public Collection<Product> getProducts() {
		return products.values();
	}

	/**
	 * get product by product id
	 */
	public Product getById(String id) {
		return products.get(id);
	}

	private Map<String, Product> products;
}
