<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<srcalc:adminPage title="Summary Report Parameters">

<section>
    <h2>Summary Report Parameters</h2>
    <form:form id="summaryReportParametersForm" cssClass="srcalcForm attributeEditForm"
        method="post" commandName="reportParameters">
    <%-- Display any object-wide errors --%><form:errors cssClass="error"/>
    <%-- See srcalc.css for why HTML tables. --%>
    <table>
    <tbody>
    <%--
        <tr>
        <td class="attributeName">Specialties</td>
        <td>
            <form:checkboxes items="${specialtyList}" path="specialties"/>
            <form:errors path="specialties" cssClass="error" />
        </td>
        </tr>  --%>
        <%--
        <tr>
        <td class="attributeName">CPT Code</td>
        <td>
            <form:select path="cptCode">
                <form:options items="cptCodeList"/>
            </form:select>
        </td>        
        </tr>  --%>
    </tbody>
    </table>
   
    <div class="actionButtons">
    <ol>
    <li><c:url var="cancelUrl" value="${srcalcUrls.adminHome}" />
        <a class="btn-default" href="${cancelUrl}">Cancel</a></li>
    <li><button class="button-em" type="submit">Generate Report</button></li>
    </ol>
    </div>
    
    </form:form>
    
</section>
</srcalc:adminPage>
