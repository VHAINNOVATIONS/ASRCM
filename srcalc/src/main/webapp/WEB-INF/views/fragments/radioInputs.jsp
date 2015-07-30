<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%-- This file cannot be a jspf extension because it is not included statically. --%>
<%-- This jsp expects for displayItemParam to be defined. --%>
<!-- Generate a radio button for each option -->
<c:forEach var="option" items="${displayItemParam.options}">
    <label class="radioLabel">
        <form:radiobutton path="${displayItemParam.varPath}" value="${option.value}"/>
        <c:out value="${option.value}"/>
    </label>
</c:forEach>
<!-- Display any errors immediately following the input control. -->
<form:errors path="${displayItemParam.varPath}" cssClass="error" />