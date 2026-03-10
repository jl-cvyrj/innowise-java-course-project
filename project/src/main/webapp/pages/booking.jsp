<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="by.paulouskaya.webproject.model.BookingModel" %>
<html>
<head>
    <title>Your Bookings</title>
    <style>
        body { font-family: Arial, sans-serif; }
        table { width: 80%; margin: 20px auto; border-collapse: collapse; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
        th { background-color: #f0f0f0; }
        .status-pending { color: orange; }
        .status-confirmed { color: green; }
        .status-cancelled { color: red; }
        .status-completed { color: blue; }
        .button { padding: 5px 10px; margin: 2px; }
        .new-booking { margin: 20px auto; display: block; padding: 10px 20px; }
    </style>
</head>
<body>
<h1>Your Bookings</h1>

<a class="new-booking" href="<%= request.getContextPath() %>/booking/new">Create New Booking</a>

<%
    List<BookingModel> bookingList = (List<BookingModel>) request.getAttribute("booking");
    if (bookingList != null && !bookingList.isEmpty()) {
%>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Pet Type</th>
        <th>Services</th>
        <th>Preferred Date</th>
        <th>Status</th>
        <th>Notes</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (BookingModel b : bookingList) {
    %>
    <tr>
        <td><%= b.getBookingId() %></td>
        <td><%= b.getPetType() %></td>
        <td><%= String.join(", ", b.getServicesAsString()) %></td>
        <td><%= b.getPreferredDate() %></td>
        <td class="status-<%= b.getStatus().name().toLowerCase() %>"><%= b.getStatus() %></td>
        <td><%= b.getNotes() %></td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>
<%
} else {
%>
<p style="text-align:center">You have no bookings yet.</p>
<%
    }
%>

</body>
</html>