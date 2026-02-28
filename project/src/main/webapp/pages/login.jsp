<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <h1>Login</h1>
    <form action="${pageContext.request.contextPath}/login" method="post">
        Username or Email: <input type="text" name="username">
        Password: <input type="password" name="password">
        <button type="submit">Log In</button>
    </form>
    <c:if test="${not empty error}">
        <p style="color:red">${error}</p>
    </c:if>
</body>
</html>
