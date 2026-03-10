<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .error { color: red; }
        form { max-width: 400px; margin: 50px auto; }
        label { display: block; margin-top: 15px; }
        input { width: 100%; padding: 8px; margin-top: 5px; }
        button { margin-top: 20px; padding: 10px; width: 100%; }
        a { text-decoration: none; color: blue; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<h1>Login</h1>

<%
    String error = (String) request.getAttribute("error");
    String preservedUsername = (String) request.getAttribute("preservedUsername");
    if (error != null && !error.isEmpty()) {
%>
<p class="error"><%= error %></p>
<%
    }
%>

<form action="<%= request.getContextPath() %>/login" method="post">
    <label for="username">Username or Email:</label>
    <input type="text" id="username" name="username"
           value="<%= preservedUsername != null ? preservedUsername : "" %>" required>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required>

    <button type="submit">Log In</button>
</form>

<p>Don't have an account? <a href="<%= request.getContextPath() %>/register">Register here</a></p>

</body>
</html>