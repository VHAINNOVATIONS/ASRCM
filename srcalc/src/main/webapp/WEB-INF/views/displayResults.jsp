<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>

<srcalc:calcPage title="Calculation Results">
<section>
    <h2>Calculation Results</h2>
    <%@include file="fragments/calculationSummary.jspf" %>
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
    <li><button class="button-em" type="submit" disabled>Sign Calculation</button></li>
    <li>
    <%-- Add the required patientDfn parameter, preserving the patient from the current calculation. --%>
    <c:url var="newCalcUrl" value="/newCalc"><c:param name="patientDfn" value="${calculation.patient.dfn}"/></c:url>
    <a href="${newCalcUrl}" class="btn-default">Start New Calculation</a></li>
    <li>
        <c:url var="enterVarsUrl" value="/enterVars"/>
        <a href="${enterVarsUrl}" class="btn-default">Return to Variable Input Form</a>
    </li>
    </ol>
    </div>
</section>
</srcalc:calcPage>