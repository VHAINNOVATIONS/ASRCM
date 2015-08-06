<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>

<srcalc:adminPage title="Summary Report">

<section>
    <h2>ASRC Summary Report - ${report.generationDate}</h2>
    
    <p>
    The below table may be copied and pasted into Excel. Paste into "Text" cells to
    preserve leading 0's.
    </p>
    
    <c:if test="${report.results.truncated}">
    <p class="error">
    Your search returned more results than could be displayed. Please refine your search.
    </p>
    </c:if>
    
    <table id="summaryReportTable" class="srcalcTable reportTable">
    <thead>
    <tr>
        <th>CPT Code</th>
        <th>Surgical Specialty</th>
        <th>Facility</th>
        <th>Provider Type</th>
        <th>Signed</th>
        <th>Risk Model</th>
        <th>Outcome</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="foundItem" items="${report.results.foundItems}">
    <tr>
        <td>${foundItem.cptCode}</td>
        <td>${foundItem.specialtyName}</td>
        <td>${foundItem.userStation}</td>
        <td>${foundItem.providerType}</td>
        <td><joda:format value="${foundItem.signatureTimestamp}" pattern="yyyy-MM-dd HH:mm"/></td>
        <td>${foundItem.riskModelName}</td>
        <td><fmt:formatNumber value="${foundItem.outcome}" type="percent" minFractionDigits="1" maxFractionDigits="1"/></td>
    </tr>
    </c:forEach>
    </tbody>
    </table>
    
    <!-- Include DataTables JS at the bottom so as not to delay page loading. -->
    <c:url var="dataTablesUrl" value="/js/vendor/DataTables-1.10.5/jquery.dataTables.min.js"/>
    <script type="text/javascript" src="${dataTablesUrl}"></script>
    <script>
    $(document).ready(function() {
        $('#summaryReportTable').dataTable({
            paging: false,
            searching: false,
            // The search comes back in a defined order already: don't re-order unless
            // the user requests it.
            order: []
        });
    });
    </script>

    <c:url var="adminHomeUrl" value="${srcalcUrls.adminHome}" />
    <p><a href="${adminHomeUrl}">Return to Administration Home</a></p>
    
</section>
</srcalc:adminPage>
