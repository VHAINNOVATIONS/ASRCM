<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<srcalc:adminPage title="Edit Variable">
<section>
    <h2>Edit Variable</h2>
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
        <td class="attributeName">Dependent Models:</td>
        <td>
        <ul>
        <c:forEach var="model" items="${variable.dependentModels}">
            <li>${model.displayName}</li>
        </c:forEach>
        </ul>
        </td>
        </tr>
        <tr>
        <td class="attributeName">Display Text:</td>
        <td>
            <%-- Use the DISPLAY_NAME_MAX for the text box size, but cap it at
                 40 because any bigger is too much longer than the expected
                 length. --%>
            <form:input path="displayName" size="${srcalc:min(DISPLAY_NAME_MAX, 40)}" />
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
        <tr>
        <td class="attributeName">Group:</td>
        <td>
            <form:select path="groupId">
                <form:options items="${variable.allGroups}" itemValue="id" itemName="name" />
            </form:select>
            <form:errors path="groupId" cssClass="error" />
        </td>
        </tr>
        <tr>
        <td class="attributeName">VistA Value:</td>
        <td>
            <%-- There are no retrievers for Boolean variables. --%>
            <select><option>N/A</option></select>
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