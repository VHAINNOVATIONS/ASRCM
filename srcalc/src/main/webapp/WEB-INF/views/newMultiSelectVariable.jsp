<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<srcalc:adminPage title="Edit Variable">
<section>
    <h2>New Variable</h2>
    <%-- Calculate the URL for form submission. --%>
    <c:url var="newVariableUrl" value="${SAVE_URL}"/>
    <form:form id="variableEditForm" cssClass="srcalcForm attributeEditForm"
        method="post" action="${newVariableUrl}" commandName="variable">
    <%-- Display any object-wide errors --%><form:errors cssClass="error"/>
    <%-- See srcalc.css for why HTML tables. --%>
    <table>
    <tbody>
        <%@ include file="fragments/editVariableKey.jspf" %>
        <%--
        Boolean variables only have properties common to all variables. Simply
        include the common properties here.
        --%>
        <%@ include file="fragments/commonEditVariableProperties.jspf" %>
        <tr>
        <td>Display Type:</td>
        <td>
            <span class="springGeneratedRadios">
            <form:radiobuttons cssClass="springFormGenerated" path="displayType" items="${variable.allDisplayTypes}"/>
            </span>
        </td>
        </tr>
        <tr>
        <td>Options:</td>
        <td>
            <%-- Display any overall errors --%><form:errors path="options" cssClass="error" />
            <ol id="multiSelectOptions">
            <%--
            Insert the existing options. Javascript will handle adding/removing
            as the user desires.
            --%>
            <c:set var="i" value="0" />
            <c:forEach items="${variable.options}">
            <li><form:input path="options[${i}]"/> <form:errors path="options[${i}]" cssClass="error"/></li>
            <c:set var="i" value="${i+1}" />
            </c:forEach>
            </ol>
        </td>
        </tr>
    </tbody>
    </table>
    <div class="actionButtons">
    <ol>
    <li><c:url var="cancelUrl" value="/admin" />
        <a class="btn-default" href="${cancelUrl}">Cancel</a></li>
    <li><button class="button-em" type="submit">Save Changes</button></li>
    </ol>
    </div>
    </form:form>
</section>
</srcalc:adminPage>