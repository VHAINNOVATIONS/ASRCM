<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>

<srcalc:calcPage title="Confirm New Calculation">
<section>
    <h2>Confirm New Calculation</h2>
    Calculation in progress for ${calculation.patient.name}. Starting a new calculation will overwrite the in-progress calculation.<br>
    Close this browser window or tab if you do not wish to start a new calculation.<br>
    Click the link below to start a new calculation.<br>
    <c:url var="newCalcUrl" value="/newCalc">
        <c:param name="patientDfn" value="${calculation.patient.dfn}"/>
        <c:param name="force" value="true"/>
    </c:url>
    <a href="${newCalcUrl}" class="btn-link">Start New Calculation</a>
</section>
</srcalc:calcPage>