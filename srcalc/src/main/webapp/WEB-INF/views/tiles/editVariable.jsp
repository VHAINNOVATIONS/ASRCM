<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        <h2>Edit ${variable.displayName} Variable</h2>
        <c:if test="${variable.integratedVariable}">
        <p>This is a VistA-integrated variable. Some fields may not be edited.</p>
        </c:if>
        <c:url var="editVariableUrl"
            value="/admin/models/editVariable/${variable.displayName}"/>
        <form:form id="variableEditForm" cssClass="srcalcForm attributeEditForm"
            method="post" action="${editVariableUrl}" commandName="variable">
        <fieldset>
            <!-- Use an ordered list for the list of attributes. -->
            <ol>
            <li><label class="attributeName">Display Name:</label>
            <%-- In the final solution, we may allow editing display names of
                 integrated variables, but disallow it until we know for sure. --%>
            <c:choose>
            <c:when test="${variable.integratedVariable}">
            ${variable.displayName} (Not editable)
            </c:when>
            <c:otherwise>
            <form:input path="displayName" />
            <form:errors path="displayName" cssClass="error" />
            </c:otherwise>
            </c:choose>
            </li>
            </ol>
        </fieldset>
        <div class="actionButtons">
        <c:url var="cancelUrl" value="/admin/models" />
        <button type="submit">Save Changes</button> <a href="${cancelUrl}">Cancel</a>
        </div>
        </form:form>
