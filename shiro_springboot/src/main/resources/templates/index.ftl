<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h4>List Page</h4>

    <#--获取用户信息-->
	Welcome <@shiro.principal property="username"></@shiro.principal>

	<a href="/shiro/logout">Logout</a>


    <#--用户拥有admin角色才可以访问-->
    <@shiro.hasRole name="admin">
        <a href="/shiro/admin">Admin Page</a>
    </@shiro.hasRole>

    <#--用户拥有user角色才可以访问-->
    <@shiro.hasRole name="user">
        <a href="/shiro/user">User Page</a>
    </@shiro.hasRole>

    <a href="/shiro/testAnno">Test Shiro Anno</a>


	
</body>
</html>