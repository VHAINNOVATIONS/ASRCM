<%-- Master page layout --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <link rel="stylesheet" type="text/css" href="css/normalize.min.css">
    <link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css">
    <link rel="stylesheet" type="text/css" href="css/srcalc.css">
    <script type="text/javascript" src="js/vendor/modernizr-2.6.2.min.js"></script>
    <title><tiles:insertAttribute name="title"/></title>
</head>
<body>
    <header>
        <h1><tiles:insertAttribute name="headerText"/></h1>
    </header>
    <script type="text/javascript" src="js/vendor/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="js/vendor/jquery-ui.min.js"></script>
    <section>
        <!--[if lt IE 7]>
            <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
        <![endif]-->
        <tiles:insertAttribute name="bodyContent"/>
    </section>
    <footer>
        <tiles:insertAttribute name="footerContent"/>
    </footer>
</body>
</html>
