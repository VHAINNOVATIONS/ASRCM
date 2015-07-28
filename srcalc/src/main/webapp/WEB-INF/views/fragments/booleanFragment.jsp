<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<label class="checkboxLabel">
    <form:checkbox path="${displayItemParam.varPath}" value="true"/>
    <c:out value="${displayItemParam.displayName}"/>
</label>
<!-- Display any errors immediately following the input control. -->
<form:errors path="${displayItemParam.varPath}" cssClass="error" />