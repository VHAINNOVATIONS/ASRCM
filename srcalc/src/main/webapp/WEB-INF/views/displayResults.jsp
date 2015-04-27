<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>

<srcalc:calcPage title="Calculation Results">
<section>
    <h2>Calculation Results</h2>
    <%@include file="fragments/calculationSummary.jspf" %>
    <h3>Calculation Inputs</h3>
    <table class="srcalcTable" id="inputValueTable">
    <tr><th class="main">Variable</th><th class="main">Value</th></tr>
    <%-- The procedure is more important to the calculation so it should be easily  
        visible to the user. --%>
    <c:forEach var="value" items="${calculation.procedureValues}">
    <tr><td>${value.variable.displayName}</td><td>${value.displayString}</td></tr>
    </c:forEach>
    <c:forEach var="value" items="${calculation.nonProcedureValues}">
    <tr><td>${value.variable.displayName}</td><td>${value.displayString}</td></tr>
    </c:forEach>
    </table>
    <div class="actionButtons">
    <h3>Results</h3>
    <ol>
    <c:forEach var="outcome" items="${calculation.outcomes}">
    <li>${outcome.key}: <fmt:formatNumber value="${outcome.value * 100}" minFractionDigits="1" maxFractionDigits="1" />%</li>
    </c:forEach>
    </ol>
    <span class="warning">*Warning: Signing the calculation will save it to the patient's Electronic Health Record.</span>
    <ol>
    <li>
        <c:url var="enterVarsUrl" value="/enterVars"/>
        <a href="${enterVarsUrl}" class="btn-default">Return to Variable Input Form</a>
    </li>
    <li><button id="signCalculationButton" class="button-em" type="submit">Sign Calculation</button></li>
    </ol>
    <%-- Add the required patientDfn parameter, preserving the patient from the current calculation. --%>
    <c:url var="newCalcUrl" value="/newCalc"><c:param name="patientDfn" value="${calculation.patient.dfn}"/></c:url>
    <a href="${newCalcUrl}" class="btn-link">Start New Calculation</a>
    <br>
    <div class="eSigDialog dialog" title="Enter Electronic Signature Code">
        <form id="eSigForm" method="post" class="srcalcForm">
            <input id="eSigInput" name="eSig" type="password" size="20"/>
            <br><span id="eSigErrorSpan" class="error"></span>
            <div class="actionButtons">
                <ol>
                    <li><button id="cancelESigButton" type="button">Cancel</button></li>
                    <li><button id="eSigButton" class="button-em" type="submit">Sign</button></li>
                </ol>
            </div>
        </form>
    </div>
    </div>
    <c:url var="displayResultsJsUrl" value="/js/displayResults.js"/>
    <script type="text/javascript" src="${displayResultsJsUrl}"></script>
    <script type="text/javascript">
	    $(document).ready(function(){
	    	initESigDialog();
	    });
    </script>
</section>
</srcalc:calcPage>
