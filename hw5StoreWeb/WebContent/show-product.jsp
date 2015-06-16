<%@page import="model.ProductCatalog"%>
<%@page import="model.Product"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	Product product = ((ProductCatalog) getServletContext().getAttribute(ProductCatalog.class.getName())).getById(request.getParameter("id"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%= product.getName() %></title> 
</head>
<body>
<h1><%= product.getName() %></h1>
<img src=<%= "store-images/" + product.getImageFile()%>><br>
<form action="shopping-cart.jsp" method="post">
<input type="hidden" name="addID" value=<%= product.getProductId() %>>
$<%= product.getPrice()%> <input type="submit" value="Add to Cart">
</form>
</body>
</html>