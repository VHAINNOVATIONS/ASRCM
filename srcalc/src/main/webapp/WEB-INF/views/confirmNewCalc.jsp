<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>

<srcalc:calcPage title="Confirm New Calculation">
<section>
    <h2>Confirm New Calculation</h2>
    <p>Calculation in progress for ${calculation.patient.name}. Starting a new calculation will overwrite the in-progress calculation.</p>
    <p>If you do not wish to start a new calculation, close this browser window or tab.</p>
    <p>Click the link below to start a new calculation.</p>
    <c:url var="newCalcUrl" value="/newCalc">
        <c:param name="patientDfn" value="${newPatientDfn}"/>
        <c:param name="force" value="true"/>
    </c:url>
    <a href="${newCalcUrl}" id="startNewCalcLink" class="btn-link">Start New Calculation</a>
    <script type="text/javascript">
        $("#startNewCalcLink").on("click", function() {
        	alert('All other ASRC sessions are no longer valid and should be closed');
        });
    </script>
</section>
</srcalc:calcPage>