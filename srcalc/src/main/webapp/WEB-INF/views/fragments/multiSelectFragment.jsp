<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:choose>
    <c:when test="${displayItemParam.displayType == 'Radio'}">
    <!-- Generate a radio button for each option -->
    <c:forEach var="option" items="${displayItemParam.options}">
        <label class="radioLabel">
            <form:radiobutton path="${displayItemParam.varPath}" value="${option.value}"/>
            <c:out value="${option.value}"/>
        </label>
    </c:forEach>
    </c:when>
    <c:when test="${displayItemParam.displayType == 'Dropdown'}">
    Drop-down variables not supported yet.
    </c:when>
    <c:otherwise>Error: unexpected display type "${displayItemParam.displayType}".</c:otherwise>
</c:choose>
<!-- Display any errors immediately following the input control. -->
<form:errors path="${displayItemParam.varPath}" cssClass="error" />