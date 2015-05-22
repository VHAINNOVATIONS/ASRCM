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
        <tr>
        <td class="attributeName">Internal Key:</td>
        <td>
            <form:input path="key" size="${variable.keyMax}" />
            <form:errors path="key" cssClass="error" />
        </td>
        </tr>
        <%--
        Boolean variables only have properties common to all variables. Simply
        include the common properties here.
        --%>
        <%@ include file="fragments/commonEditVariableProperties.jspf" %>
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