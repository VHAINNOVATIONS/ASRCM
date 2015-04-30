<%@ taglib tagdir="/WEB-INF/tags" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<srcalc:adminPage title="Edit Variable">
<section>
    <h2>Edit <c:out value="${variable.displayName}"/> Variable</h2>
    <%-- Calculate the URL for form submission. --%>
    <c:url var="editVariableUrl" value="/admin/variables/${variable.key}"/>
    <form:form id="variableEditForm" cssClass="srcalcForm attributeEditForm"
        method="post" action="${editVariableUrl}" commandName="variable">
    <%-- See srcalc.css for why HTML tables. --%>
    <table>
    <tbody>
        <tr>
        <td class="attributeName">Internal Key:</td>
        <td>${variable.key}</td>
        </tr>
        <tr>
        <td class="attributeName">Type:</td>
        <td>Checkbox</td><%-- This JSP is only used for boolean vars. --%>
        </tr>
        <tr>
        <%--
        <tr>
        <td class="attributeName">Currently Used By:</td>
        <td>
            <ul>
            <c:forEach var="model" items="${variable.dependentModels}">
                <li>${model.displayName}</li>
            </c:forEach>
            </ul>
        </td>
        </tr>
        --%>
        <tr>
        <td class="attributeName">Display Text:</td>
        <td>
            <form:input path="displayName" />
            <form:errors path="displayName" cssClass="error" />
        </td>
        </tr>
        <tr>
        <td class="attributeName">Help Text:</td>
        <td>
            <form:textarea path="helpText" cols="90" rows="8" />
            <form:errors path="helpText" cssClass="error" />
            <ul class="helpText">
            <li>Add a blank line between paragraphs.</li>
            <li>Bullet lists can be created by starting lines with an asterisk (* Example item).</li>
            <li>Numbered lists can be created by starting lines with a number (1. Example item).</li>
            </ul>
        </td>
        </tr>
    </tbody>
    </table>
    <div class="actionButtons">
    <c:url var="cancelUrl" value="/admin/models" />
    <a class="btn-default" href="${cancelUrl}">Cancel</a>
    <button class="button-em" type="submit">Save Changes</button>
    </div>
    </form:form>
</section>
</srcalc:adminPage>