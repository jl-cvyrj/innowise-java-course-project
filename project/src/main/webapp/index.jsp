<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add User!</title>
</head>
<body>
<h1><%= "Add User!" %>
</h1>
<br/>
<form method="get" action="hello_servlet">
    <input type="hidden" name="command" value="add_user"/>
    <input type="text" name="lastname" value=""/>
    <input type="text" name="numphone" value=""/>
    <input type="submit" name="push" value="submit"/>
</form>
<a href="hello-servlet">Hello Servlet</a>
</body>
</html>