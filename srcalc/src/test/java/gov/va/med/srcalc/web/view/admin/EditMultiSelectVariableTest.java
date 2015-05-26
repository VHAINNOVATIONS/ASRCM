package gov.va.med.srcalc.web.view.admin;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.MockModelService;
import gov.va.med.srcalc.web.view.Views;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class EditMultiSelectVariableTest
{
    private final MockModelService fModelService = new MockModelService();

    @Test
    public final void testBasic()
    {
        final EditMultiSelectVariable ev = new EditMultiSelectVariable(fModelService);

        // EditMultiSelectVariable doesn't specify what it returns for
        // getTypeName(), just make sure it returns a non-empty string.
        assertThat(ev.getTypeName(), not(isEmptyOrNullString()));
        
        assertEquals(Views.NEW_MULTI_SELECT_VARIABLE, ev.getNewViewName());
        
        assertEquals(
                // Use sets because order doesn't matter.
                ImmutableSet.copyOf(MultiSelectVariable.DisplayType.values()),
                ImmutableSet.copyOf(ev.getAllDisplayTypes()));
        
        // The constructor specifies that the default list of options is 3 blanks.
        assertEquals(ImmutableList.of("", "", ""), ev.getOptions());
        
        assertEquals(20, ev.getMaxOptions());
    }
    
    @Test
    public final void testTrimmedOptions()
    {
        final ImmutableList<String> fullList =
                ImmutableList.of("a", "b", "", "c", "");
        final EditMultiSelectVariable ev = new EditMultiSelectVariable(fModelService);
        ev.getOptions().clear();
        ev.getOptions().addAll(fullList);
        
        assertEquals(
                ImmutableList.of("a", "b", "", "c"),
                ev.getTrimmedOptions());
        // Ensure getOptions() still returns the full list.
        assertEquals(fullList, ev.getOptions());
    }
    
    @Test
    public final void testBuildNew()
    {
        // Values to set
        final String key = "msKey";
        final String displayName = "msDisplayName";
        final VariableGroup group = fModelService.getAllVariableGroups().iterator().next();
        final String helpText = "msHelpText";
        final MultiSelectVariable.DisplayType displayType =
                MultiSelectVariable.DisplayType.Dropdown;
        final ImmutableList<String> options =
                ImmutableList.of("option1", "option2", "option3");
        final ImmutableList<MultiSelectOption> multiSelectOptions = ImmutableList.of(
                new MultiSelectOption(options.get(0)),
                new MultiSelectOption(options.get(1)),
                new MultiSelectOption(options.get(2)));
        
        // Behavior
        final EditMultiSelectVariable ev = new EditMultiSelectVariable(fModelService);
        ev.setKey(key);
        ev.setDisplayName(displayName);
        ev.setGroupId(group.getId());
        ev.setHelpText(helpText);
        ev.setDisplayType(displayType);
        ev.getOptions().clear();
        ev.getOptions().addAll(options);
        final MultiSelectVariable createdVariable = ev.buildNew();
        
        // Verification
        assertEquals(key, createdVariable.getKey());
        assertEquals(displayName, createdVariable.getDisplayName());
        assertEquals(group, createdVariable.getGroup());
        assertEquals(helpText, createdVariable.getHelpText().get());
        assertEquals(displayType, createdVariable.getDisplayType());
        assertEquals(multiSelectOptions, createdVariable.getOptions());
    }
    
}
