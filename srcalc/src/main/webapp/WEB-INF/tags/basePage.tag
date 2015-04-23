<%@ tag language="java" pageEncoding="ISO-8859-1"
    description="Encapsulates the master page layout" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="headerText" required="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

<%-- Make the appInfo object (added by AppAttributesInitializer) easily accessible. --%>
<c:set var="appInfo" scope="page" value="${applicationScope['srcalc.appInfo']}" />

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
    <c:url var="dataTablesCss" value="/js/vendor/DataTables-1.10.5/jquery.dataTables.min.css"/>
    <link rel="stylesheet" type="text/css" href="${dataTablesCss}">
    <c:url var="modernizrJs" value="/js/vendor/modernizr-2.6.2.min.js"/>
    <script type="text/javascript" src="${modernizrJs}"></script>
    <title>${title}</title>
</head>
<body>
    <header>
        <h1>${headerText}</h1>
    </header>
    <c:url var="jqueryJs" value="/js/vendor/jquery-1.11.2.min.js"/>
    <script type="text/javascript" src="${jqueryJs}"></script>
    <c:url var="jqueryUiJs" value="/js/vendor/jquery-ui.min.js"/>
    <script type="text/javascript" src="${jqueryUiJs}"></script>
    <!--[if (gte IE 6)&(lte IE 8)]>
        <c:url var="selectivizrJs" value="/js/vendor/selectivizr-min.js"/>
	    <script type="text/javascript" src="${selectivizrJs}"></script>
	<![endif]-->
    <!--[if lt IE 7]>
        <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
    <![endif]-->
    <jsp:doBody/>
    <footer>
    <span class="appInfo">${appInfo.longName} v${appInfo.version}</span>
    </footer>
</body>
</html>