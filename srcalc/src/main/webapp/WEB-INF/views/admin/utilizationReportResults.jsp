<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>

<srcalc:adminPage title="Utilization Report">

<section>
    <h2>ASRC Utilization Report - ${report.generationDate}</h2>
    
    <p>Applied parameters:</p>
    <ul>
    <c:choose>
    <c:when test="${empty report.parameters.appliedParameters}">
    <li>None. (Unrestricted search.)</li>
    </c:when>
    <c:otherwise>
    <c:forEach var="paramEntry" items="${report.parameters.appliedParameters}">
    <li>${paramEntry.key}: ${paramEntry.value}</li>
    </c:forEach>
    </c:otherwise>
    </c:choose>
    </ul>
    
    <p>
    The below table may be copied and pasted into Excel.
    </p>
    
    <c:if test="${report.results.truncated}">
    <p class="error">
    Your search returned more results than could be analyzed. Please refine your search.
    </p>
    </c:if>
    
    <h3>Summary Data</h3>
    
    <table id="utilizationSummaryTable" class="srcalcTable reportTable">
    <thead>
    <tr>
        <th>Specialty</th>
        <th>Total</th>
        <th>Signed</th>
        <th class="longHeaderShortContent">Time to First Run Average (Minutes)</th>
        <th class="longHeaderShortContent">Time to Sign Average (Minutes)</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="summaryEntry" items="${report.specialtySummaries}">
    <tr>
        <td>${summaryEntry.key}</td>
        <td class="numerical">${summaryEntry.value.totalCount}</td>
        <td class="numerical">${summaryEntry.value.signedCount}</td>
        <td class="numerical">
        <%-- secondsToFirstRunAverage can be -1 if there were no signed calcs, but in that
             case there will be no summary row. --%>
        <fmt:formatNumber
            value="${summaryEntry.value.secondsToFirstRunAverage / 60}"
            minFractionDigits="1" maxFractionDigits="1"/>
        </td>
        <td class="numerical">
        <%-- secondsToSignAverage will be -1 if there were no signed calcs. --%>
        <c:if test="${summaryEntry.value.secondsToSignAverage >= 0}">
        <fmt:formatNumber
            value="${summaryEntry.value.secondsToSignAverage / 60}"
            minFractionDigits="1" maxFractionDigits="1"/>
        </c:if>
        </td>
    </tr>
    </c:forEach>
    </tbody>
    </table>

    <c:url var="adminHomeUrl" value="${srcalcUrls.adminHome}" />
    <p><a href="${adminHomeUrl}">Return to Administration Home</a></p>
    
</section>
</srcalc:adminPage>
