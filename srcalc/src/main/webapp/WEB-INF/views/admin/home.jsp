<%@ taglib tagdir="/WEB-INF/tags" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<srcalc:adminPage title="Administration Home">
<section>
    <h2>Administration Home</h2>

    <h3>Model Administration</h3>    
    
    <c:url var="modelHomeUrl" value="${srcalcUrls.modelAdminHome}" />
    <p><a href="${modelHomeUrl}">Model Administration</a></p>
    
    <h3>Reports</h3>
    
    <c:url var="summaryReportUrl" value="${srcalcUrls.summaryReport}" />
    <ul>
    <li><a href="${summaryReportUrl}">Summary Report</a></li>
    </ul>
    
</section>
</srcalc:adminPage>
