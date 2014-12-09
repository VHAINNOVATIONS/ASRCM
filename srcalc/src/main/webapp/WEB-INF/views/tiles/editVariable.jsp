<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        <h2>Edit ${variable.displayName} Variable</h2>
        <c:url var="editVariableUrl"
            value="/admin/models/editVariable/${variable.displayName}"/>
        <form:form id="variableEditForm" cssClass="srcalcForm attributeEditForm"
            method="post" action="${editVariableUrl}" commandName="variable">
        <fieldset>
            <!-- Use an ordered list for the list of attributes. -->
            <ol>
            <li><label class="attributeName">Display Name:</label>
            <form:input path="displayName" />
            <form:errors path="displayName" cssClass="error" />
            </li>
            </ol>
        </fieldset>
        <div class="actionButtons">
        <c:url var="cancelUrl" value="/admin/models" />
        <button type="submit">Save Changes</button> <a href="${cancelUrl}">Cancel</a>
        </div>
        </form:form>
