<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
        <h2>Enter Risk Variables</h2>
        <ol class="calculationSummary">
        <li><label class="variableName">Specialty:</label> ${calculation.specialty}</li>
        <li><label class="variableName">Patient:</label> ${calculation.patient}</li>
        </ol>
        <form id="riskVarForm" method="post" action="enterVars">
        <fieldset>
            <legend>Variables</legend>
            <!-- Use an ordered list for the list of fields. -->
            <ol>
            <c:forEach var="variable" items="${calculation.variables}">
            <li><label class="variableName">${variable.displayName}:</label>
            <srcalc:variableInput variable="${variable}"/>
            </li>
            </c:forEach>
            </ol>
        </fieldset>
        <button type="submit">Run Calculation</button>
        </form>
