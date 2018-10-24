<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<%--Welcome <shiro:principal />--%>

	<h4>List Page</h4>

	<shiro:hasRole name="admin">
		<a href="/admin.jsp">Admin Page</a>
	</shiro:hasRole>

	<shiro:hasRole name="user">
		<a href="/user.jsp">User Page</a>
	</shiro:hasRole>

	<a href="/shiro/testAnno">Test Shiro Anno</a>

	<a href="/shiro/logout">Logout</a>

	
</body>
</html>