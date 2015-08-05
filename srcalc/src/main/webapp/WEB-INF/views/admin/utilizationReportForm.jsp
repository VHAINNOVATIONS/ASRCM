<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<srcalc:adminPage title="Utilization Report Parameters">

<section>
    <h2>Utilization Report Parameters</h2>
    <form:form id="utilizationReportParametersForm" cssClass="srcalcForm attributeEditForm"
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
