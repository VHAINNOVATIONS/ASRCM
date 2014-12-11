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
    var initialCpt = "";
    var initialDisplayString = "(none)";
    // If we have a procedure radio selected already, populate the
    // hidden input and display field with that info.
    var initialSelection = getSelectedRadio();
    if (initialSelection.length) {
            initialCpt = initialSelection.val();
            initialDisplayString = initialSelection.data('display-string');
    }
    var hiddenInput = $('<input type="hidden" name="' + procedureVarName + '" value="' + initialCpt + '">');
    var userDisplay = $('<span>' + initialDisplayString + '</span>');
    var selectLink = $('<a class="selectProcedureLink" href="#">Select</a>');
    procedureSelectGroup.after(hiddenInput, userDisplay, ' ', selectLink);
    
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