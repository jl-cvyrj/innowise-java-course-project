<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.paulouskaya.webproject.model.BookingModel" %>
<%@ page import="by.paulouskaya.webproject.model.PetType" %>
<%@ page import="by.paulouskaya.webproject.model.ServiceType" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Edit Booking</title>
    <style>
        body { font-family: Arial, sans-serif; }
        form { width: 50%; margin: 20px auto; display: flex; flex-direction: column; }
        label { margin-top: 10px; }
        input, select, textarea { padding: 5px; font-size: 14px; margin-top: 5px; }
        .submit-btn { margin-top: 20px; padding: 10px; font-size: 16px; }
        .error { color: red; text-align: center; margin-top: 10px; }
    </style>
</head>
<body>
<h1 style="text-align:center">Edit Booking</h1>

<%
    BookingModel booking = (BookingModel) request.getAttribute("booking");
    if (booking == null) {
%>
<p class="error">Booking not found.</p>
<%
} else {
    List<ServiceType> allServices = List.of(ServiceType.values());
%>

<form action="<%= request.getContextPath() %>/booking/edit" method="post">
    <input type="hidden" name="bookingId" value="<%= booking.getBookingId() %>">

    <label for="petType">Pet Type:</label>
    <select name="petType" id="petType">
        <%
            for (PetType pt : PetType.values()) {
                String selected = pt == booking.getPetType() ? "selected" : "";
        %>
        <option value="<%= pt.name() %>" <%= selected %>><%= pt.name() %></option>
        <%
            }
        %>
    </select>

    <label>Services:</label>
    <%
        for (ServiceType s : allServices) {
            boolean checked = booking.getServices().contains(s);
    %>
    <label>
        <input type="checkbox" name="services" value="<%= s.getDisplayName() %>" <%= checked ? "checked" : "" %>>
        <%= s.getDisplayName() %> ($<%= s.getPrice() %>)
    </label>
    <br>
    <%
        }
    %>

    <label for="preferredDate">Preferred Date:</label>
    <input type="datetime-local" name="preferredDate" id="preferredDate"
           value="<%= booking.getPreferredDate().toString().replace(' ', 'T') %>">

    <label for="notes">Notes:</label>
    <textarea name="notes" id="notes" rows="4"><%= booking.getNotes() != null ? booking.getNotes() : "" %></textarea>

    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
    <p class="error"><%= error %></p>
    <%
        }
    %>

    <button type="submit" class="submit-btn">Update Booking</button>
</form>

<%
    }
%>

</body>
</html>