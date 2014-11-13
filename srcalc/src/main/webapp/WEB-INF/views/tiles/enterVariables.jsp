<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        <h2>Enter Risk Variables</h2>
        <ol class="calculationSummary">
        <li><label class="variableName">Specialty:</label> ${calculation.specialty}</li>
        <li><label class="variableName">Patient:</label> ${calculation.patient}</li>
        </ol>
        <form:form id="riskVarForm" method="post" action="enterVars" commandName="submittedValues">
        <fieldset>
            <legend>Variables</legend>
            <!-- Use an ordered list for the list of fields. -->
            <ol>
            <c:forEach var="variable" items="${calculation.variables}">
            <li><label class="variableName">${variable.displayName}:</label>
            <!-- TODO: can I preserve the inputted value even if invalid? -->
            <srcalc:variableInput variable="${variable}"/>
            <!-- FIXME: hardcoded path to displayName, this may not always be true -->
            <form:errors path="${variable.displayName}" cssClass="error" />
            </li>
            </c:forEach>
            </ol>
        </fieldset>
        <button type="submit">Run Calculation</button>
        </form:form>
