<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>

<srcalc:calcPage title="Confirm New Calculation">
<section>
    <h2>Confirm New Calculation</h2>
    <p>Calculation in progress for ${calculation.patient.name}. Starting a new calculation will overwrite the in-progress calculation.</p>
    <p>If you do not wish to start a new calculation, close this browser window or tab.</p>
    <p>Click the link below to start a new calculation.</p>
    <c:url var="newCalcUrl" value="/newCalc">
        <c:param name="patientDfn" value="${patientDfn}"/>
        <c:param name="force" value="true"/>
    </c:url>
    <a href="${newCalcUrl}" class="btn-link">Start New Calculation</a>
</section>
</srcalc:calcPage>