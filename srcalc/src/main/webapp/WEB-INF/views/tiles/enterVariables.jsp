<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/srcalc.tld" prefix="srcalc" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
        <h2>Enter Risk Variables</h2>
        <ol class="calculationSummary">
        <li><label class="variableName">User:</label> <sec:authentication property="principal.displayName" /></li>
        <li><label class="variableName">Specialty:</label> ${calculation.specialty}</li>
        <li><label class="variableName">Patient:</label> ${calculation.patient}</li>
        </ol>
        <form:form id="riskVarForm" cssClass="srcalcForm" method="post" action="enterVars" commandName="submittedValues">
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
        <script>
        $(document).ready(function(){
        	var procedureSelectGroup = $(".procedureSelectGroup");
        	
        	function selectProcedure() {
        		var selectedRadio = procedureSelectGroup.find('input[type=radio]:checked');
        		$('input[name=' + procedureSelectGroup.data("var-name") + ']').val(selectedRadio.val());
        		$('#selectedProcedureDisplay').html(selectedRadio.data('display-string'));
        		procedureSelectDialog.dialog("close");
        	}
        	
        	var procedureSelectDialog = procedureSelectGroup.dialog({
        		autoOpen: false,
        		width: 700,   // body with is 768px
        		modal: true,
        		buttons: {
        			"Select": selectProcedure
                    // Maybe I can add a cancel button when refining the dialog.
        		}
        	});
        	
        	$('a.selectProcedureLink').on('click', function() {
                var windowHeight = $(window).height();
                // Make the height 90% of the current window height.
                procedureSelectDialog.dialog("option", "height", windowHeight * 0.9);
                procedureSelectDialog.dialog("open");
        	})
        })
        </script>
        </form:form>
