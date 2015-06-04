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
    
    var categoriesList = $('ol#categoriesList');

    // The link to add new Category inputs.
    var addCategoryLink = $('#addNewCategory');
    
    /**
     * Adds a new group of inputs to the categoriesList.
     */
    function addNewCategoryInputs() {
        // Calculate the index of the new input (0-based).
        var index = categoriesList.find('li').length;
        // These category inputs are pretty complex: clone an existing input and modify
        // it for the new index.
        var existingInputs = categoriesList.find('.categoryInputs:first');
        var newInputs = existingInputs.clone();
        // Just iterate through the contrcategoriesLists and update the category index.
        newInputs.find('input').each(function() {
            var input = $(this);
            // Spring contrcategoriesLists put the path in the 'name' attribute and sometimes the 'id'
            // attribute. Update them both.

            // name
            var categoriesListdName = input.attr('name');
            var newName = categoriesListdName.replace(/categories\[\d\d*\]\./, 'categories[' + index + '].');
            input.attr('name', newName);
            
            // id
            var categoriesListdId = input.attr('id');
            if (categoriesListdId) {
                var newId = categoriesListdId.replace(/categories\d\d*\./, 'categories' + index + '.');
                input.attr('id', newId);
            }
        });
        var newLi = $('<li/>').append(newInputs);
        categoriesList.append(newLi);
    }

    function toggleAddCategoryLink() {
        var currentCount = categoriesList.find('li').length;
        var maxCategories = categoriesList.data('max-categories');
        if (currentCount >= maxCategories) {
            var msg = $('<span id="maxCategoriesMessage"/>');
            msg.text('Only ' + maxCategories + ' categories are permitted.');
            addCategoryLink.replaceWith(msg);
        }
    }
    
    addCategoryLink.on('click', function (e) {
        e.preventDefault();
        addNewCategoryInputs();
        toggleAddCategoryLink();
    });
    
    // Do an initial toggle to replace the link with the max message if necessary.
    toggleAddCategoryLink();
}