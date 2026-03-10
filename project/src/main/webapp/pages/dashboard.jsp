<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>

<h1>Welcome!</h1>

<p>You are logged in.</p>

<a href="<%=request.getContextPath()%>/booking/new">Create booking</a>
<a href="<%=request.getContextPath()%>/booking">Show my bookings</a>

</body>
</html>