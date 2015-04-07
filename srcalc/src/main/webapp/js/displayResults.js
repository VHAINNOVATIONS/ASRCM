/**
 * 
 */
function initESigDialog(){
	// Make the esig with a jQuery UI dialog. 
    var procedureSelectDialog = $('.eSigDialog').dialog({
        autoOpen: false,
        width: 350,   // body width is 768px
        modal: true
    });
    
    var openESigButton = $('#signCalculationButton');
    openESigButton.on('click', function(event) {
        event.preventDefault();

        var windowHeight = 150;
        // Make the height 90% of the current window height.
        procedureSelectDialog.dialog('option', 'height', windowHeight);
        procedureSelectDialog.dialog('open');
    });
    
    var cancelESigButton = $('#cancelESigButton');
    cancelESigButton.on('click', function(event) {
    	event.preventDefault();
    	procedureSelectDialog.dialog('close');
    });
}

$.ajax({url: "/signCalc", success: function(result) {
	if(result.status = "Success") {
		// Redirect to the success page.
	}
	else {
		// Change the span text to the error text
	}
}});