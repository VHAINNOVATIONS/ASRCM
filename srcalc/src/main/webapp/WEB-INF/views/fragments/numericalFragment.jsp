<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:input path="${displayItemParam.varPath}" size="6"/>
<c:out value="${displayItemParam.units} ${variableEntryParam.getMeasureDate(displayItemParam.key)}"/>
<%-- Display any errors immediately following the input control.--%>
<form:errors path="${displayItemParam.varPath}" cssClass="error" />