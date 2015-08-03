<%@ tag language="java" pageEncoding="ISO-8859-1" body-content="scriptless"
    description="Template for an edit variable page. The body content will be included inside the form."%>
<%@ attribute name="variable" required="true"
    type="gov.va.med.srcalc.web.view.admin.EditVar"
    description="The EditVar instance to edit." %>
<%@ attribute name="isNewVariable" required="true"
    description="Set to true to indicate that this page is for creating a new variable, false for editing and existing variable." %>
<%@ attribute name="saveUrl" required="true"
    description="The submission URL of the form." %>

<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:choose>
<c:when test="${isNewVariable}"><c:set var="pageTitle" value="New Variable"/></c:when>
<c:otherwise><c:set var="pageTitle" value="Edit Variable"/></c:otherwise>
</c:choose>

<srcalc:adminPage title="${pageTitle}">
<section>
    <h2>${pageTitle}</h2>
    <%-- Calculate the full URL for form submission. --%>
    <c:url var="fullSaveUrl" value="${saveUrl}"/>
    <form:form id="variableEditForm" cssClass="srcalcForm attributeEditForm"
        method="post" action="${fullSaveUrl}" commandName="variable">
    <%-- Display any object-wide errors --%><form:errors cssClass="error"/>
    <%-- See srcalc.css for why HTML tables. --%>
    <table>
    <tbody>
        <tr>
        <td class="attributeName">Internal Key:</td>
        <c:choose>
        <c:when test="${isNewVariable}">
        <td>
            <form:input path="key" size="${variable.keyMax}" />
            <form:errors path="key" cssClass="error" />
        </td>
        </c:when>
        <c:otherwise>
        <td>${variable.key}</td>
        </c:otherwise>
        </c:choose>
        </tr>
        <tr>
        <td class="attributeName">Type:</td>
        <td>${variable.typeName}</td>
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
            <%--
            Use the given displayNameMax for the text box size, but cap it at 40
            because any bigger is too much longer than the expected length.
            --%>
            <form:input path="displayName" size="${srcalc:min(variable.displayNameMax, 40)}" />
            <form:errors path="displayName" cssClass="error" />
        </td>
        </tr>
        <tr>
        <td class="attributeName">Help Text:</td>
        <td>
            <form:textarea path="helpText" cols="90" rows="8" />
            <form:errors path="helpText" cssClass="error" />
            <ul class="postInstructions">
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
            <form:select path="retriever">
                <form:option value="" label="N/A"></form:option>
                <form:options items="${variable.allRetrievers}" />
            </form:select>
        </td>
        </tr>
        <%-- Include the provided body. --%>
        <jsp:doBody/>
    </tbody>
    </table>
    <div class="actionButtons">
    <ol>
    <li><c:url var="cancelUrl" value="${srcalcUrls.modelAdminHome}" />
        <a class="btn-default" href="${cancelUrl}">Cancel</a></li>
    <li><button class="button-em" type="submit">Save Changes</button></li>
    </ol>
    </div>
    </form:form>
<c:url var="editVariableJs" value="/js/editVariable.js" />
<script type="text/javascript" src="${editVariableJs}"></script>
<script type="text/javascript">
$(document).ready(function() {
    initEditVariablePage();
})
</script>
</section>
</srcalc:adminPage>