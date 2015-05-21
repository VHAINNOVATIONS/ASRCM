package gov.va.med.srcalc.web.view.admin;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import gov.va.med.srcalc.service.MockModelService;

import org.junit.Test;

public class EditBooleanVariableTest
{
    @Test
    public final void testBasic()
    {
        final EditBooleanVariable ebv = new EditBooleanVariable(new MockModelService());

        // EditBooleanVariable doesn't specify what it returns for getTypeName(),
        // just make sure it returns a non-empty string.
        assertThat(ebv.getTypeName(), not(isEmptyOrNullString()));
    }
    
}
