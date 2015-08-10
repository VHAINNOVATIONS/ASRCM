<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%-- This file cannot be a jspf extension because it is not included statically. --%>
<%-- This jsp expects for displayItemParam to be defined. --%>
<!-- Generate a radio button for each option -->
<form:select path="${displayItemParam.varPath}">
    <form:options items="${displayItemParam.options}"/>
</form:select>
<%-- There cannot be any errors since the only options given are valid. --%>