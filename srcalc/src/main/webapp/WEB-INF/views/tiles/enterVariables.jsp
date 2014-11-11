<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <h2>Enter Risk Variables</h2>
        <form id="riskVarForm">
        <p>Patient: ${calculation.patient}</p>
        <fieldset>
            <legend>Specialty</legend>
            <!-- Use an ordered list for the list of fields. -->
            <ol>
            <li><label>Specialty:</label> ${calculation.specialty}</li>
            </ol>
        </fieldset>
        </form>
