package gov.va.med.srcalc.web.view.admin;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.calculation.ValueRetriever;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.MockModelService;
import gov.va.med.srcalc.web.view.Views;

import org.junit.Test;

public class EditBooleanVarTest
{
    private final MockModelService fModelService = new MockModelService();
    
    @Test
    public final void testBasic()
    {
        final EditBooleanVar ebv = new EditBooleanVar(fModelService);

        // EditBooleanVar doesn't specify what it returns for getTypeName(),
        // just make sure it returns a non-empty string.
        assertThat(ebv.getTypeName(), not(isEmptyOrNullString()));
        
        assertEquals(Views.NEW_BOOLEAN_VARIABLE, ebv.getNewViewName());
        assertEquals(0, ebv.getAllRetrievers().size());
    }
    
    @Test
    public final void testBuildNew()
    {
        // Values to set
        final String key = "myKey";
        final String displayName = "myDisplayName";
        final VariableGroup group = fModelService.getAllVariableGroups().iterator().next();
        final String helpText = "myHelpText";

        // Behavior
        final EditBooleanVar ebv = new EditBooleanVar(fModelService);
        ebv.setKey(key);
        ebv.setDisplayName(displayName);
        ebv.setGroupId(group.getId());
        ebv.setHelpText(helpText);
        final BooleanVariable createdVariable = ebv.buildNew();
        
        // Verification
        assertEquals(key, createdVariable.getKey());
        assertEquals(displayName, createdVariable.getDisplayName());
        assertEquals(group, createdVariable.getGroup());
        assertEquals(helpText, createdVariable.getHelpText().get());
    }
    
    @Test(expected = IllegalStateException.class)
    public final void testSetRetrieverInvalid()
    {
        final EditBooleanVar ev =
                new EditBooleanVar(SampleModels.dnrVariable(), fModelService);
        
        ev.setRetriever(ValueRetriever.BMI);
        
        ev.buildNew();
    }
}
