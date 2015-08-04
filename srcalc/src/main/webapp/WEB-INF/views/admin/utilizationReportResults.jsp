<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.joda.org/joda/time/tags" prefix="joda" %>

<srcalc:adminPage title="Utilization Report">

<section>
    <h2>ASRC Utilization Report - ${report.generationDate}</h2>
    
    <p>
    The below tables may be copied and pasted into Excel.
    </p>
    
    <c:if test="${report.results.truncated}">
    <p class="error">
    Your search returned more results than could be displayed. Please refine your search.
    </p>
    </c:if>
    
    <h3>Summary Data</h3>
    
    <table class="reportTable">
    <thead>
    <tr>
        <th>Specialty</th>
        <th>Total</th>
        <th>Signed</th>
        <%-- Set the width to make sure these don't get too large. --%>
        <th style="width: 7em;">Time to First Run Average (Minutes)</th>
        <th style="width: 7em;">Time to Sign Average (Minutes)</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="summaryEntry" items="${report.specialtySummaries}">
    <tr>
        <td>${summaryEntry.key}</td>
        <td class="numerical">${summaryEntry.value.totalCount}</td>
        <td class="numerical">${summaryEntry.value.signedCount}</td>
        <td class="numerical">
        <fmt:formatNumber
            value="${summaryEntry.value.secondsToFirstRunAverage / 60}"
            minFractionDigits="1" maxFractionDigits="1"/>
        </td>
        <td class="numerical">
        <fmt:formatNumber
            value="${summaryEntry.value.secondsToSignAverage / 60}"
            minFractionDigits="1" maxFractionDigits="1"/>
        </td>
    </tr>
    </c:forEach>
    </tbody>
    </table>
    
    <h3>Calculation Data</h3>
    
    <table id="utilizationReportTable" class="reportTable">
    <thead>
    <tr>
        <th>Specialty</th>
        <th>Date</th>
        <th>Signed</th>
        <th>Time to First Run<br>(Minutes)</th>
        <th>Time to Sign<br>(Minutes)</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="foundItem" items="${report.results.foundItems}">
    <tr>
        <td>
        <c:out value="${foundItem.historicalCalculation.specialtyName}"/>
        </td>
        <td>
        <joda:format value="${foundItem.historicalCalculation.startTimestamp}"
            pattern="yyyy-MM-dd"/>
        </td>
        <td>${foundItem.signed ? "Yes" : "No"}</td>
        <td class="numerical">
        <fmt:formatNumber
            value="${foundItem.historicalCalculation.secondsToFirstRun / 60}"
            minFractionDigits="1" maxFractionDigits="1"/>
        </td>
        <td class="numerical">
        <c:if test="${foundItem.signed}">
        <fmt:formatNumber
            value="${foundItem.signedResultNullable.secondsToSign / 60}"
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
