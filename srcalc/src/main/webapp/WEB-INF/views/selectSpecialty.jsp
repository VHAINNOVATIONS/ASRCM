<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Select Specialty</title>
</head>
<body>
<h1>NSO Surgical Risk Calculator</h1>
<h2>Select Surgical Specialty</h2>
<form>
<ul id="specialtyRadios">
<c:forEach var="specialty" items="${specialties}">
<li>
    <label><input type="radio" name="specialty" value="${specialty.name}"> ${specialty.name}</label>
</li>
</c:forEach>
</ul>
<button type="submit" name="submit">Continue</button>
</form>
</body>
</html>