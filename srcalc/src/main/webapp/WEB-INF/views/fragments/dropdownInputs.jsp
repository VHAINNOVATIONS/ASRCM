<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%-- This file cannot be a jspf extension because it is not included statically. --%>
<%-- This jsp expects for displayItemParam to be defined. --%>
<form:select path="${displayItemParam.varPath}">
    <form:options items="${displayItemParam.options}"/>
</form:select>
<!-- Display any errors immediately following the input control. -->
<form:errors path="${displayItemParam.varPath}" cssClass="error" />