/* JavaScript code just for the enterVariables page */

/**
 * Moves the procedureSelectGroup table into a jQuery UI dialog.
 */
function initProcedureSelect(procedures) {
    // Note that this code assumes there is only one procedureSelectGroup
    // on the page.
    
    // Returns the shorter display string for a procedure.
    function makeDisplayString(procedure) {
        return procedure.cptCode + ' - ' +
            procedure.shortDescription + ' - (' + procedure.rvu + ')';
    }
    
    // Lookup the procedure variable name and the initially-selected procedure.
    var hiddenInput = $('.procedureHiddenInput');
    var userDisplay = $('.procedureDisplay');

    // Make the procedureSelectGroup with a jQuery UI dialog. 
    var procedureSelectDialog = $(".procedureSelectGroup").dialog({
        autoOpen: false,
        width: 700,   // body with is 768px
        modal: true
    });
    
	// Insert a hidden input and a textual display as the target
	// of the user selection from the dialog.
	// Get the procedure hidden input, if it exists.
	function selectProcedure(cptCode, displayString) {
		// Get the selected button's value and change the display string
	    hiddenInput.val(cptCode);
	    userDisplay.html(displayString);
	    procedureSelectDialog.dialog("close");
	}

	// Set up the properties for the procedures DataTable
	var proceduresTable = $("#procedureTable").dataTable({
	    data: procedures,
		"deferRender": true,
		columns: [
		          { data: 'cptCode' },
		          { data: 'longDescription', searchable: false },
		          { data: 'rvu', searchable: false },
		          {
		              data: 'cptCode',
		              render: function (data, type, row) {
		                  return '<a href="#" class="btn-link"' +
                              '" data-cpt-code="' + row.cptCode +
                              '" data-display-string="' + makeDisplayString(row) + '">Select</a>';
		              },
		              width: '10%', searchable: false, sortable: false }
              ]
	});
	
	$("#procedureTable").on('click', 'a', function(event){
	    event.preventDefault();  // Don't navigate.

		var elem = event.target || event.srcElement;
	    selectProcedure($(elem).data('cpt-code'), $(elem).data('display-string'));
	});
	
	// Get a DataTables API instance
	var apiTable = proceduresTable.dataTable().api();
	// Unbind the default global search and keyup
	$("div.dataTables_filter input").unbind();
	// Add a "starts with" regex to the global search
	// Enable regex search and disable smart searching
	$("div.dataTables_filter input").on('keyup', function () {
		apiTable.column(0).search('^' + this.value, true, false).draw();
	});
	
    
    // Find selected procedure and update the display string.
    var selectedCpt = hiddenInput.val();
    var initialDisplay = "(none)";
    $.each(procedures, function (index, procedure) {
        if (procedure.cptCode == selectedCpt) {
            initialDisplay = makeDisplayString(procedure);
        }
    });
    userDisplay.html(initialDisplay);
    
    // We're done initializing everything. Add the Select link to the page.
    var openProcedureSelect =
        $('<a class="openProcedureSelect" href="#">Select</a>');
    openProcedureSelect.on('click', function(event) {
        event.preventDefault();

        var windowHeight = $(window).height();
        // Make the height 90% of the current window height.
        procedureSelectDialog.dialog("option", "height", windowHeight * 0.9);
        procedureSelectDialog.dialog("open");
    });
    userDisplay.after(' ', openProcedureSelect);
	
}

/**
 * Initializes the page. Should be called after the DOM is ready.
 */
function initEnterVariablesPage() {
    
    // Load the procedures list separately via AJAX to enable caching of the
    // large list. We use a manual AJAX request instead of DataTables's built-in
    // functionality for direct access to the procedure list.
    $.getJSON('procedures', function (procedures) {
        // Success callback.
        initProcedureSelect(procedures);
    });
    
    // Tie the numerical text box state to the state of the numerical radio.
    var numericalValueContainers =
        $('.attributeValue').has('input.numerical').has('input.numericalRadio');
    var radios = numericalValueContainers.find('input[type=radio]');
    radios.on('change', function() {
        var container = $(this).closest('.attributeValue');
        var radio = container.find('input.numericalRadio');
        var numericalInput = container.find('input.numerical');
        if (radio.prop('checked')) {
            numericalInput.removeAttr('disabled');
        } else {
            numericalInput.attr('disabled', 'disabled');
        }
    });
}