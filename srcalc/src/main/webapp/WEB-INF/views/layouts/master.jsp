<%-- Master page layout --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <%--
    Note: all resource URLs are absolute to the context root to allow for pages
    under /admin, etc.
    --%>
    <c:url var="normalizeCss" value="/css/normalize.min.css"/>
    <link rel="stylesheet" type="text/css" href="${normalizeCss}">
    <c:url var="jqueryUiCss" value="/css/jquery-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${jqueryUiCss}">
    <c:url var="srcalcCss" value="/css/srcalc.css"/>
    <link rel="stylesheet" type="text/css" href="${srcalcCss}">
    <c:url var="modernizrJs" value="/js/vendor/modernizr-2.6.2.min.js"/>
    <script type="text/javascript" src="${modernizrJs}"></script>
    <title><tiles:insertAttribute name="title"/></title>
</head>
<body>
    <header>
        <h1><tiles:insertAttribute name="headerText"/></h1>
    </header>
    <c:url var="jqueryJs" value="/js/vendor/jquery-1.11.1.min.js"/>
    <script type="text/javascript" src="${jqueryJs}"></script>
    <c:url var="jqueryUiJs" value="/js/vendor/jquery-ui.min.js"/>
    <script type="text/javascript" src="${jqueryUiJs}"></script>
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
