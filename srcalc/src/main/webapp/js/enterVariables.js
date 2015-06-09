/* JavaScript code just for the enterVariables page */

// Note that all of this code assumes there is only one procedureSelectGroup on
// the page.

/**
 * Create our own module with its own scope.
 */
var ENTERVARIABLES = function() {
    
    /*** Private members ***/
        
    // These are jQuery selections for our relevant elements on the page. They
    // are set in initPage() below.
    var hiddenInput;
    var userDisplay;
    var procedureSelectDialog;
    
    // This is a jQuery selection for the actual procedure table. The table is
    // lazy-initialized to avoid hanging up initial page rendering. If this
    // variable is null, then it has not been initialized yet.
    var procedureTable = null;
    
    // These are strings representing the user input from the search boxes for
    // "Any of these words" and "All of these words".
    var procedureAllWords = "";
    var procedureAnyWords = "";
    
    /**
     * Returns the shorter display string for a procedure.
     */
    function makeDisplayString(procedure) {
        return procedure.cptCode + ' - ' +
            procedure.shortDescription + ' - (' + procedure.rvu + ')';
    }
    
	// Insert a hidden input and a textual display as the target
	// of the user selection from the dialog.
	// Get the procedure hidden input, if it exists.
	function selectProcedure(cptCode, displayString) {
		// Get the selected button's value and change the display string
	    hiddenInput.val(cptCode);
	    userDisplay.html(displayString);
	    procedureSelectDialog.dialog("close");
	}
	
	function procedureSearch(rowData) {
		var description = rowData[1];
		var allWords = procedureAllWords;
		// Skip if none specified.
		if(allWords) {
			var words = allWords.split(/\s+/);
			for(var index in words) {
				// Escape any regular expression characters since this is
				// from user input.
				var escapedWord = $.fn.dataTable.util.escapeRegex(words[index]);
				// If the description does not contain the word, just return
				// false (short-circuiting any other tests).
				if(description.search(new RegExp(escapedWord, "i")) < 0) {
					return false;
				}
			}
		}
		
		var anyWords = procedureAnyWords;
		// Also skip if none specified
		if(anyWords) {
			var words = anyWords.split(/\s+/);
			for(var index in words) {
				// Escape any regular expression characters since this is
				// from user input.
				var escapedWord = $.fn.dataTable.util.escapeRegex(words[index]);
				// If the description contains the word, just return true,
				// since it only needs to contain one of these.
				if(description.search(new RegExp(escapedWord, "i")) >= 0) {
					return true;
				}
			}
			// Didn't contain any anyWords. Return false.
			return false;
		}
		
		return true;
	}
		
	function initProcedureTable(procedures) {
            // Set up the properties for the procedures DataTable
            procedureTable = $("#procedureTable").dataTable({
            dom: '<"searchToolbar"> rlftip',
            data: procedures,
            ordering: false, // ordering is not a requirement, disable for performance
            deferRender: true,
            oLanguage: {
            	sSearch: "CPT Search:" // Override the default search text of "Search"
            },
            columns: [
                      { data: 'cptCode' },
                      { data: 'longDescription'},
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
            
            procedureTable.on('click', 'a', function(event){
                event.preventDefault();  // Don't navigate.

                var elem = event.target || event.srcElement;
                selectProcedure($(elem).data('cpt-code'), $(elem).data('display-string'));
            });
            
            // Get a DataTables API instance
            var apiTable = procedureTable.dataTable().api();
            // Find the input within the container div.
            var searchInput = $(apiTable.table().container()).
                find('div.dataTables_filter input');
            // Unbind the default global search and keyup
            searchInput.unbind();
            // Add a "starts with" regex to the global search
            // Enable regex search and disable smart searching
            searchInput.on('keyup', function () {
                    apiTable.column(0).search('^' + this.value, true, false).draw();
            });
	    
            // Set up the custom toolbar for searching procedure descriptions
            // Setting size to 20 allows the text boxes to be the same size as the CPT Search box
            $('div.searchToolbar').html('All of these words:<input id="procedureAllWords" size="20"><br>'
            		+'Any of these words:<input id="procedureAnyWords" size="20">');
            
            var cptFilter = $('#procedureTable_filter input');
            $('#procedureAnyWords').on('keyup paste cut', function() {
            	// Cache the value for quick filtering.
            	procedureAnyWords = $(this).val();
            	// The draw method clears the search filter, so we need to preserve it.
            	var cptFilterText = cptFilter.val();
            	apiTable.draw();
            	cptFilter.val(cptFilterText);
            });
            
            $('#procedureAllWords').on('keyup paste cut', function() {
            	// Cache the value for quick filtering.
            	procedureAllWords = $(this).val();
            	// The draw method clears the search filter, so we need to preserve it.
            	var cptFilterText = cptFilter.val();
            	apiTable.draw();
            	cptFilter.val(cptFilterText);
            });
            
            // The table is done rendering.
            procedureSelectDialog.removeClass('uninitialized');
	}

    /**
     * Initializes the procedure selector control (including the displayed text
     * and the popup dialog).
     */
    function initProcedureSelect(procedures) {
        
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
            
            // Lazy-init the procedureTable.
            if (!procedureTable) {
                window.setTimeout(function() {
                    initProcedureTable(procedures);
                },
                0);  // execute asynchronously, but immediately
            }
        });
        userDisplay.after(' ', openProcedureSelect);
            
    }


    /**
     * Enables or disables the numerical text box of a discrete numerical
     * control group depending on the current radio button selection.
     */
    function toggleNumerical(radioInput) {
            var container = $(radioInput).closest('.attributeValue');
        var radio = container.find('input.numericalRadio');
        var numericalInput = container.find('input.numerical');
        if (radio.prop('checked')) {
            numericalInput.removeAttr('disabled');
        } else {
            numericalInput.attr('disabled', 'disabled');
        }
    }
    
    /*** Public API ***/
    return {
        /**
         * Initializes the page. Should be called after the DOM is ready.
         */
        initPage: function() {

            hiddenInput = $('.procedureHiddenInput');
            userDisplay = $('.procedureDisplay');

            // Make the procedureSelectGroup with a jQuery UI dialog. 
            procedureSelectDialog = $(".procedureSelectGroup").dialog({
                autoOpen: false,
                width: 700,   // body with is 768px
                modal: true
            });

            // Load the procedures list separately via AJAX to enable caching of
            // the large list. We use a manual AJAX request instead of
            // DataTables's built-in functionality for direct access to the
            // procedure list.
            $.getJSON('refdata/procedures', function (procedures) {
                // Success callback.
                initProcedureSelect(procedures);
            });
            
            $.fn.dataTable.ext.search.push(
	    		function(settings, rowData, dataIndex) {
	    			return procedureSearch(rowData);
	    	});
            
            // Tie the numerical text box state to the state of the numerical radio.
            var numericalValueContainers =
                $('.attributeValue').has('input.numerical').has('input.numericalRadio');
            var radios = numericalValueContainers.find('input[type=radio]');
            radios.on('change', function() {
                    toggleNumerical(this);
            });
            radios.each(function(){
                    toggleNumerical(this);
            });
        }
    }
}();
