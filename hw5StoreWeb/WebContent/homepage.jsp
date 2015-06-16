<%@page import="model.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Student Store</title>
</head>
<body>
<h1>Student Store</h1>
<h5>Items available:</h5>
<ul>
<%
	ProductCatalog pCatalog = (ProductCatalog) getServletContext().getAttribute(ProductCatalog.class.getName());
	for (Product product : pCatalog.getProducts()) {
		out.println("<li><a href=\"show-product.jsp?id=" + product.getProductId() + "\">" + product.getName() + "</a></li>");
	}
%>
</ul>
</body>
</html>