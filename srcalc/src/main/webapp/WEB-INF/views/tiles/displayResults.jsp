<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
        <h2>Calculation Results</h2>
        <%@include file="calculationSummary.jspf" %>
        <h3>Calculation Inputs</h3>
        <table class="srcalcTable" id="inputValueTable">
        <tr><th class="main">Variable</th><th class="main">Value</th></tr>
        <c:forEach var="value" items="${calculation.values}">
        <tr><td>${value.variable.displayName}</td><td>${value.displayString}</td>
        </c:forEach>
        </table>
        <div class="actionButtons">
        <h3>Results</h3>
        <ol>
        <c:forEach var="outcome" items="${calculation.outcomes}">
        <li>${outcome.key}: <fmt:formatNumber value="${outcome.value * 100}" minFractionDigits="1" maxFractionDigits="1" />%</li>
        </c:forEach>
        </ol>
        <ol>
        <li><button type="submit" disabled>Sign Calculation</button></li>
        <li><c:url var="newCalcUrl" value="/newCalc" />
        <a class="btn-link" href="${newCalcUrl}">Start New Calculation</a></li>
        </ol>
        </div>
