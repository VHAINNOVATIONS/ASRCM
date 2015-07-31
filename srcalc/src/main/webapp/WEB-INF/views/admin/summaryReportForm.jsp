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
        <tr>
        <td class="attributeName">Date Range</td>
        <td>
            <form:input path="minDate" cssClass="dateEntry" size="10" />
            <form:errors path="minDate" cssClass="error" />
            through
            <form:input path="maxDate" cssClass="dateEntry" size="10" />
            <form:errors path="maxDate" cssClass="error" />
        </td>
        </tr>
        <tr>
        <td class="attributeName">Specialties</td>
        <td>
            <span class="springGeneratedCheckboxes">
            <form:checkboxes path="specialtyNames" cssClass="springFormGenerated"
                items="${specialtyList}" itemValue="name" itemLabel="name"/>
            </span>
            <form:errors path="specialtyNames" cssClass="error" />
        </td>
        </tr>
        <tr>
        <td class="attributeName">CPT Code</td>
        <td>
            <form:select path="cptCode">
                <form:option value="">--No Filter--</form:option>
                <form:options items="${procedureList}" itemLabel="shortString" itemValue="cptCode" />
            </form:select>
        </td>        
        <tr>
        <td class="attributeName">Station Number</td>
        <td>
            <form:input path="stationNumber" size="6" />
            <form:errors path="stationNumber" cssClass="error" />
        </td>
        </tr>
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
    
    <script>
    $(document).ready(function() {
        $('.dateEntry').datepicker();
    })
    </script>
    
</section>
</srcalc:adminPage>
