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
        <form:form id="riskVarForm" cssClass="srcalcForm attributeEditForm"
            method="post" action="enterVars" commandName="variableEntry">
        <c:forEach var="variableGroup" items="${calculation.variableGroups}">
        <fieldset>
            <legend>${variableGroup.name}</legend>
            <!-- Use an ordered list for the list of fields. -->
            <ol>
            <c:forEach var="variable" items="${variableGroup.variables}">
            <li><label class="attributeName">${variable.displayName}:</label>
            <!-- TODO: can I preserve the inputted value even if invalid? -->
            <srcalc:variableInput variable="${variable}"/>
            <%-- This errors tag would be better suited in the above variableInput
                 tag but I haven't figured out how to nest other custom tags in
                 my custom tags yet. --%>
            <form:errors path="dynamicValues[${variable.displayName}]" cssClass="error" />
            </li>
            </c:forEach>
            </ol>
        </fieldset>
        </c:forEach>
        <div class="actionButtons">
        <ol>
        <li><button type="submit">Run Calculation</button></li>
        <li><c:url var="newCalcUrl" value="/newCalc" />
        <a class="btn-link" href="${newCalcUrl}">Start New Calculation</a></li>
        </ol>
        </div>
        <script>
        $(document).ready(function(){
        	
        	// Note that this code assumes there is only one procedureSelectGroup
        	// on the page.

        	var procedureSelectGroup = $(".procedureSelectGroup");
        	// Determine the variable name from the first radio button.
        	var procedureVarName =
        		procedureSelectGroup.find('input[type=radio]').first().attr('name');
        	
        	// We're about to replace the procedureSelectGroup with a jQuery UI
        	// dialog. Insert a hidden input and a textual display as the target
        	// of the user selection from the dialog.
        	var hiddenInput = $('<input type="hidden" name="' + procedureVarName + '">');
        	var userDisplay = $('<span>(none)</span>');
        	var selectLink = $('<a class="selectProcedureLink" href="#">Select</a>');
        	procedureSelectGroup.after(hiddenInput, userDisplay, ' ', selectLink);
        	
        	function selectProcedure() {
        		var selectedRadio = procedureSelectGroup.find('input[type=radio]:checked');
        		hiddenInput.val(selectedRadio.val());
        		userDisplay.html(selectedRadio.data('display-string'));
        		procedureSelectDialog.dialog("close");
        	}
        	
        	var procedureSelectDialog = procedureSelectGroup.dialog({
        		autoOpen: false,
        		width: 700,   // body with is 768px
        		modal: true,
        		buttons: {
        			"Select": selectProcedure
        		}
        	});
        	
        	selectLink.on('click', function() {
                var windowHeight = $(window).height();
                // Make the height 90% of the current window height.
                procedureSelectDialog.dialog("option", "height", windowHeight * 0.9);
                procedureSelectDialog.dialog("open");
        	})
        })
        </script>
        </form:form>
