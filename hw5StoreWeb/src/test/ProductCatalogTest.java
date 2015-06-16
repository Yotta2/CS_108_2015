package test;

import org.junit.Test;

import model.ProductCatalog;

public class ProductCatalogTest {
	@Test
	public void testGetProducts() {
		ProductCatalog pCatalog = new ProductCatalog();
		System.out.println(pCatalog.getProducts());
	}
}
