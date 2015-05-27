/*** Javascript code for editing variables ***/

/**
 * Initializes dynamic content on the edit variable pages.
 */
function initEditVariablePage() {
    
    // Initialize the link to add new MultiSelectOption inputs.
    $('#multiSelectOptions #addNewOption').on('click', function(e){
        e.preventDefault();
        var clickedLink = $(this);
        var ol = clickedLink.closest('td').find('ol');
        // Calculate the index of the new input (0-based).
        var index = ol.find('li').length;
        var newElement = $(
                '<li><input id="options' + index + '" name="options[' + index + ']"></li>');
        ol.append(newElement);
        // If we just added the last permitted option, replace the Add link
        // with a message stating so.
        var maxOptions = clickedLink.data('max-options');
        if (index + 1 >= maxOptions) {
            clickedLink.replaceWith('Only 20 options are permitted.');
        }
    });
}