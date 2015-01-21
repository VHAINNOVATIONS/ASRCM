/* JavaScript code just for the enterVariables page */

/**
 * Moves the procedureSelectGroup table into a jQuery UI dialog.
 */
function initProcedureSelect() {
    // Note that this code assumes there is only one procedureSelectGroup
    // on the page.

    // Lookup the procedure variable name and the initially-selected procedure.
    var hiddenInput = $('.procedureHiddenInput');
    var varName = hiddenInput.attr('name');
    var selectedProcedure = hiddenInput.val();
    // We're about to replace the procedureSelectGroup with a jQuery UI
	// dialog. 
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
	    var userDisplay = $('.procedureDisplay');
	    userDisplay.html(displayString);
	    procedureSelectDialog.dialog("close");
	}

	// Set up the properties for the procedures DataTable
	var proceduresTable = $("#procedureTable").dataTable({
		"data": procedureArray,
		"retrieve": true,
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
                              '" data-display-string="' + row.displayString + '">Select</a>';
		              },
		              width: '10%', searchable: false, sortable: false }
              ]
	});
	
	$("#procedureTable").on('click', 'a', function(event){
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

    $('.selectProcedureLink').on('click', function() {
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