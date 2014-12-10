<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
        <h2>Calculation Results</h2>
        <ol class="calculationSummary">
        <li><label class="variableName">User:</label> <sec:authentication property="principal.displayName" /></li>
        <li><label class="variableName">Specialty:</label> ${calculation.specialty}</li>
        <li><label class="variableName">Patient:</label> ${calculation.patient}</li>
        </ol>
        <h3>Calculation Inputs</h3>
        <table class="srcalcTable" id="inputValueTable">
        <tr><th class="main">Variable</th><th class="main">Value</th></tr>
        <c:forEach var="value" items="${calculation.values}">
        <tr><td>${value.variable}</td><td>${value.displayString}</td>
        </c:forEach>
        </table>
        <div class="actionButtons">
        <h3>Results</h3>
        <ol>
        <li><button type="submit" disabled>Sign Calculation</button></li>
        <li><c:url var="newCalcUrl" value="/newCalc" />
        <a class="btn-link" href="${newCalcUrl}">Start New Calculation</a></li>
        </ol>
        </div>
