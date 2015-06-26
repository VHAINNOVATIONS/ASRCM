<%@ tag language="java" pageEncoding="ISO-8859-1" body-content="scriptless"
    description="Template for an edit Model page. "%>
<%@ attribute name="riskModel" required="true"
    type="gov.va.med.srcalc.web.view.admin.EditRiskModel"
    description="The EditRiskModel instance to edit." %>
<%@ attribute name="saveUrl" required="true"
    description="The submission URL of the form." %>

<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<c:set var="pageTitle" value="Edit Model"/>
<srcalc:adminPage title="${pageTitle}">

<section>
    <h2>${pageTitle} ${riskModel.modelName}</h2>
    <%-- Calculate the full URL for form submission. --%>
    <c:url var="fullSaveUrl" value="${saveUrl}"/>
    <form:form id="editModelForm" cssClass="srcalcForm attributeEditForm"
        method="post" action="${fullSaveUrl}" commandName="riskModel">
    <%-- Display any object-wide errors --%><form:errors cssClass="error"/>
    <%-- See srcalc.css for why HTML tables. --%>
    <table>
    <tbody>
        <tr>
        <td class="attributeName">Display Text</td>
        <td>
            <form:input autocomplete="false" path="modelName" size="${srcalc:min( riskModel.maxDisplayNameLength,60) }" />
            <form:errors path="modelName" cssClass="error" />
        </td>
        </tr>
        <tr>
        <td class="attributeName">Specialties</td>
        <td>
        <ul>
        <!-- 
        <c:forEach var="specialty" items="${riskModel.specialties }">
            <li>${specialty.name}</li>
        </c:forEach>
         -->
        </ul>
        </td>
        </tr>
        <%-- Include the provided body. --%>
        <jsp:doBody/>
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