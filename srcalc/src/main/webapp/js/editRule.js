var EDITRULES = function() {
	/*
	 * Enables or disables the text box related to the "Apply" checkbox.
	 */
	function toggleExpressionInput(applyCheckbox) {
        var container = $(applyCheckbox).closest('.matcherInputs');
	    var expressionTextBox = container.find('input.booleanExpression');
	    if ($(applyCheckbox).prop("checked")){
	    	expressionTextBox.removeAttr('disabled');
	    } else {
	    	expressionTextBox.attr('disabled', 'disabled');
	    }
	}
	
	/*** Public API ***/
	return {
		/**
         * Initializes the page. Should be called after the DOM is ready.
         */
        initPage: function() {
        	// Tie the expression text box state to the state of the apply checkbox.
        	var applyCheckboxes = $('.applyCheckbox');
        	applyCheckboxes.on('change', function() {
        		toggleExpressionInput(this);
            });
        	applyCheckboxes.each(function(){
        		toggleExpressionInput(this);
            });
        }
	}
}();