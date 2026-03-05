<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Registration</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .error { color: red; }
        form { max-width: 400px; margin: 50px auto; }
        label { display: block; margin-top: 15px; }
        input, select { width: 100%; padding: 8px; margin-top: 5px; }
        button { margin-top: 20px; padding: 10px; width: 100%; }
    </style>
</head>
<body>
<h1>Register</h1>

<%
    String error = (String) request.getAttribute("error");
    String preservedUsername = (String) request.getAttribute("preservedUsername");
    String preservedEmail = (String) request.getAttribute("preservedEmail");
    if (error != null && !error.isEmpty()) {
%>
<p class="error"><%= error %></p>
<%
    }
%>

<form action="<%= request.getContextPath() %>/register" method="post">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username"
           value="<%= preservedUsername != null ? preservedUsername : "" %>" required>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email"
           value="<%= preservedEmail != null ? preservedEmail : "" %>" required>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required>

    <label for="role">Role:</label>
    <select id="role" name="role">
        <option value="CLIENT" selected>Client</option>
        <option value="ADMIN">Admin</option>
    </select>

    <button type="submit">Register</button>
</form>

<p>Already have an account? <a href="<%= request.getContextPath() %>/login">Login here</a></p>

</body>
</html>