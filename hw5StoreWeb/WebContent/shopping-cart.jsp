<%@page import="model.ProductCatalog"%>
<%@page import="model.ShoppingCart"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Shopping Cart</title>
</head>
<body>
<h1>Shopping Cart</h1>
<form action="shopping-cart.jsp" method="post">
<ul>
<%
	String addId = request.getParameter("addID");
	ShoppingCart userCart = (ShoppingCart) request.getSession().getAttribute(ShoppingCart.class.getName());
	if (addId != null) {
		if (userCart.contains(addId)) {
			userCart.updateEntry(addId, userCart.getEntryAmount(addId) + 1);
		} else {
			ProductCatalog pCatalog = (ProductCatalog) request.getServletContext().getAttribute(ProductCatalog.class.getName());
			userCart.add(pCatalog.getById(addId));			
		}
	} else { // update cart
		for (ShoppingCart.Entry entry : userCart.getAllEntries()) {
			int amount = Integer.parseInt(request.getParameter(entry.getProduct().getProductId()));
			if (amount == 0) {
				userCart.remove(entry.getProduct().getProductId());
			} else {
				userCart.updateEntry(entry.getProduct().getProductId(), amount);
			}
		}
	}
	System.out.println(userCart);
	double total = 0;
	for (ShoppingCart.Entry entry : userCart.getAllEntries()) {
		total += entry.getAmount() * entry.getProduct().getPrice();
		out.print("<li><input type=\"text\" value=");
		out.print(entry.getAmount());
		out.print(" name=");
		out.print(entry.getProduct().getProductId() + ">");
		out.print(entry.getProduct().getName() + "," + entry.getProduct().getPrice());
		out.println("</li>");
	}
%>
</ul>
		Total: $<%= total %> <input type="submit" value="Update Cart">
</form>
<a href="homepage.jsp">Continue Shopping</a>
</body>
</html>