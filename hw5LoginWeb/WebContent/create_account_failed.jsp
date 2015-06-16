<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create Account</title>
</head>
<body>
<h1>The Name <%= request.getAttribute("name") %> is Already In Use</h1>
<h3>Please enter another name and password.</h3>
<form action="CreateAccountServlet" method="post">
User Name:<input type="text" name="name" /><br>
Password:<input type="text" name="password" />
<input type="submit" value="Login" /><br>
</form>
</body>
</html>