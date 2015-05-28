/**
 * 
 */
function initESigDialog(){
	// Make the esig with a jQuery UI dialog. 
    var electronicSignatureDialog = $('.eSigDialog').dialog({
    	beforeClose: function(event, ui) {
    		// Reset the controls for the next use of the dialog.
    		$('#eSigInput').val('');
    		$('#eSigErrorSpan').text('');
    	},
        autoOpen: false,
        width: 350,   // body width is 768px
        modal: true
    });
    
    var openESigButton = $('#signCalculationButton');
    openESigButton.on('click', function(event) {
        event.preventDefault();

        var windowHeight = 170;
        electronicSignatureDialog.dialog('option', 'height', windowHeight);
        electronicSignatureDialog.dialog('open');
    });
    
    $('#eSigForm').on('submit', function(event) {
    	event.preventDefault();
    	var eSigCode = $('#eSigInput').val();
    	$.ajax({
			url: "signCalculation",
			type: 'POST',
			dataType: 'json',
			data: {
				eSig: eSigCode
			},
			/*
			 * disable button and add loading text
			 * */
			beforeSend: function() { 
				$('#eSigButton').prop('disabled', true);
				$('#eSigButton').text("Signing");
			},
			success: function(result) {
				if(result.status == "Success") {
					// Redirect to the success page.
					window.location.replace("successfulSign");
				}
				else {
					// Change the span text to the given error text
					$('#eSigErrorSpan').text(result.status);
					$('#eSigButton').prop('disabled', false);
					$('#eSigButton').text("Sign");
				}
			}
		});
    });
    
    var cancelESigButton = $('#cancelESigButton');
    cancelESigButton.on('click', function(event) {
    	event.preventDefault();
    	electronicSignatureDialog.dialog('close');
    });
}