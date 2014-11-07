<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <link rel="stylesheet" type="text/css" href="css/normalize.min.css">
    <link rel="stylesheet" type="text/css" href="css/srcalc.css">
    <title>Enter Variables</title>
</head>
<body>
    <header>
        <h1>NSO Surgical Risk Calculator</h1>
    </header>
    <section>
        <!--[if lt IE 7]>
            <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
        <![endif]-->
        <h2>Enter Risk Variables</h2>
        <form id="riskVarForm">
        <fieldset>
            <ol>
            <li><label class="variableName">Specialty:</label> ${calculation.specialty}</li>
            <li><label class="variableName">Patient:</label> ${calculation.patient}</li>
            </ol>
        </fieldset>
        <fieldset>
            <legend>Variables</legend>
            <!-- Use an ordered list for the list of fields. -->
            <ol>
            <c:forEach var="variable" items="${variables}">
            <li><label class="variableName">${variable.displayName}:</label>
            <srcalc:variableInput variable="${variable}"/>
            </li>
            </c:forEach>
            </ol>
        </fieldset>
        </form>
    </section>
</body>
</html>
