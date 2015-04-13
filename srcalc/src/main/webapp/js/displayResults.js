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

        var windowHeight = 170;
        // Make the height 90% of the current window height.
        procedureSelectDialog.dialog('option', 'height', windowHeight);
        procedureSelectDialog.dialog('open');
    });
    
    $('#eSigForm').on('submit', function(event) {
    	event.preventDefault();
    	var eSigCode = $('#eSigInput').val();
    	$.ajax({
			url: "displayResults",
			type: 'POST',
			dataType: 'json',
			data: {
				eSig: eSigCode
			},
			beforeSend: function() { 
				$('#eSigButton').prop('disabled', true);
				$('#eSigButton').text("Signing");
			},
			/*
			 * disable button and add loading text
			 * */
			success: function(result) {
				if(result[0].status == "Success") {
					// Redirect to the success page.
					window.location.replace("successfulSign");
				}
				else {
					// Change the span text to the given error text
					$("#eSigErrorSpan").text(result[0].status);
					$('#eSigButton').prop('disabled', false);
					$('#eSigButton').text("Sign");
				}
			}
		});
    });
    
    var cancelESigButton = $('#cancelESigButton');
    cancelESigButton.on('click', function(event) {
    	event.preventDefault();
    	procedureSelectDialog.dialog('close');
    });
}