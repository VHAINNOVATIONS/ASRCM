/* JavaScript code just for the enterVariables page */

/**
 * Moves the procedureSelectGroup table into a jQuery UI dialog.
 */
function initProcedureSelect() {
    // Note that this code assumes there is only one procedureSelectGroup
    // on the page.

    var procedureSelectGroup = $(".procedureSelectGroup");
    // Determine the variable name from the first radio button.
    var procedureVarName =
            procedureSelectGroup.find('input[type=radio]').first().attr('name');
    
    // Returns a jQuery object wrapping the selected procedure radio
    // button. (May be empty for no selection.)
    function getSelectedRadio() {
            return procedureSelectGroup.find('input[type=radio]:checked');
    }
    
    // We're about to replace the procedureSelectGroup with a jQuery UI
    // dialog. Insert a hidden input and a textual display as the target
    // of the user selection from the dialog.
    // Get the procedure hidden input, if it exists.
    var hiddenInput = $('.procedureHiddenInput');
    var userDisplay = $('.procedureDisplay');
    var selectLink = $('<a class="selectProcedureLink" href="#">Select</a>');
    procedureSelectGroup.after(' ', selectLink);
    
    function onSelectProcedure() {
            var selectedRadio = getSelectedRadio();
            hiddenInput.val(selectedRadio.val());
            userDisplay.html(selectedRadio.data('display-string'));
            procedureSelectDialog.dialog("close");
    }
    
    var procedureSelectDialog = procedureSelectGroup.dialog({
            autoOpen: false,
            width: 700,   // body with is 768px
            modal: true,
            buttons: {
                    "Select": onSelectProcedure
            }
    });
    
    selectLink.on('click', function() {
    var windowHeight = $(window).height();
    // Make the height 90% of the current window height.
    procedureSelectDialog.dialog("option", "height", windowHeight * 0.9);
    procedureSelectDialog.dialog("open");
    });
	
}

/**
 * Initializes the page. Should be called after the DOM is ready.
 */
function initEnterVariablesPage() {
    initProcedureSelect();
    
    // If an attributeValue contains both a numerical input and a radio button
    // to select the numerical input, automatically check the radio button when
    // the user changes the numerical value.
    var numericalValueContainers =
        $('.attributeValue').has('input.numerical').has('input.numericalRadio');
    var numericalInputs = numericalValueContainers.find('input.numerical');
    // The 'input' event is new in HTML5 and supported by IE9+.
    numericalInputs.on('input', function() {
        var radio = $(this).closest('.attributeValue').find('input.numericalRadio');
        radio.attr('checked', 'checked');
    })
}