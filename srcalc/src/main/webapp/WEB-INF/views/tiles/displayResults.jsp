<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
        <h2>Calculation Results</h2>
        <ol class="calculationSummary">
        <li><label class="variableName">Specialty:</label> ${calculation.specialty}</li>
        <li><label class="variableName">Patient:</label> ${calculation.patient}</li>
        </ol>
        <h3>Calculation Inputs</h3>
        <table id="inputValueTable">
        <tr><th>Variable</th><th>Value</th></tr>
        <c:forEach var="value" items="${calculation.values}">
        <tr><td>${value.variable}</td><td>${value.value}</td>
        </c:forEach>
        </table>
