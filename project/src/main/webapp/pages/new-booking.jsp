<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>New Booking</title>
</head>
<body>
<h1>Create New Booking</h1>

<form action="<%= request.getContextPath() %>/booking/new" method="post">
    <label for="petType">Pet Type:</label>
    <select id="petType" name="petType" required>
        <option value="DOG">Dog</option>
        <option value="CAT">Cat</option>
    </select>

    <label>Services:</label>
    <select name="services" multiple>
        <option value="BATHING">Bathing</option>
        <option value="HAIRCUT">Haircut</option>
        <option value="NAIL_TRIMMING">Nail trimming</option>
        <option value="EAR_CLEANING">Ear cleaning</option>
        <option value="TEETH_BRUSHING">Teeth brushing</option>
        <option value="FUR_TRIMMING">Fur trimming</option>
        <option value="GROOMING">Full grooming</option>
        <option value="PAW_CARE">Paw care</option>
        <option value="FLEA_TREATMENT">Flea treatment</option>
    </select>

    <label for="preferredDate">Preferred Date & Time:</label>
    <input type="datetime-local" id="preferredDate" name="preferredDate" required>

    <label for="notes">Notes:</label>
    <textarea id="notes" name="notes"></textarea>

    <button type="submit">Create Booking</button>
</form>

<a href="<%= request.getContextPath() %>/dashboard">Back to Dashboard</a>
</body>
</html>