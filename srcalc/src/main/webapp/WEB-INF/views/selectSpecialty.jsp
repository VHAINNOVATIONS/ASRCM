<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<!-- Note: modernizr removes the no-js class. -->
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <link rel="stylesheet" type="text/css" href="css/normalize.min.css">
    <link rel="stylesheet" type="text/css" href="css/srcalc.css">
    <!-- modernizr needs to be in the <head> -->
    <script src="js/vendor/modernizr-2.6.2.min.js"></script>
    <title>Select Specialty</title>
</head>
<body>
    <header>
        <h1>NSO Surgical Risk Calculator</h1>
    </header>
    <section>
        <!--[if lt IE 7]>
            <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
        <![endif]-->
        <p>Risk Calculation for ${calculation.patient}.</p>
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
    </section>
</body>
</html>