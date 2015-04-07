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
    <li><button id="signCalculationButton" class="button-em" type="submit">Sign Calculation</button>
        <div class="eSigDialog dialog" title="Enter Electronic Signature Code">
	        <!-- Needs a submit, cancel, and input field for the electronic signature code. -->
	        <!-- <form:form id="eSigForm" cssClass="srcalcForm attributeEditForm"
	            method="post" action="displayResults" commandName="variableEntry">
	            <input id="eSigInput" size="10"/>
	        </form:form> -->
	        <form id="eSigForm" method="post">
	            <input id="eSigInput" size="20"/>
	            <br>
	            <div class="actionButtons">
	                <ol>
			            <li><button class="button-em" type="submit">Sign</button></li>
			            <li><button id="cancelESigButton" type="button">Cancel</button></li>
		            </ol>
	            </div>
	        </form>
	    </div>
    </li>
    <li>
    <%-- Add the required patientDfn parameter, preserving the patient from the current calculation. --%>
    <c:url var="newCalcUrl" value="/newCalc"><c:param name="patientDfn" value="${calculation.patient.dfn}"/></c:url>
    <a href="${newCalcUrl}" class="btn-link">Start New Calculation</a></li>
    <li>
    	<c:url var="enterVarsUrl" value="/enterVars"/>
    	<a href="${enterVarsUrl}" class="btn-link">Return to Variable Input Form</a>
    </li>
    </ol>
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