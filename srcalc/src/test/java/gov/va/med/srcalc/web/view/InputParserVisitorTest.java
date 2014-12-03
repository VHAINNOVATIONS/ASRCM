package gov.va.med.srcalc.web.view;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.web.view.InputParserVisitor;
import gov.va.med.srcalc.web.view.SubmittedValues;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;

/**
 * Tests {@link InputParserVisitor}, mainly validation.
 */
public class InputParserVisitorTest
{
    @Test
    public final void testUnspecifiedMultiselect() throws Exception
    {
        // Setup variable
        final MultiSelectVariable var = SampleObjects.sampleGenderVariable();

        final SubmittedValues values = new SubmittedValues();
        values.setGender(null);
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(values, "submittedValues");
        
        final InputParserVisitor v = new InputParserVisitor(values, errors);
        v.visitMultiSelect(var);
        
        assertEquals("noSelection", errors.getFieldError("Gender").getCode());
    }

    @Test
    public final void testUnspecifiedProcedure() throws Exception
    {
        // Setup variable
        final ProcedureVariable procedureVariable = SampleObjects.sampleProcedureVariable();

        final SubmittedValues values = new SubmittedValues();
        values.setProcedure("");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(values, "submittedValues");
        
        final InputParserVisitor v = new InputParserVisitor(values, errors);
        v.visitProcedure(procedureVariable);
        
        assertEquals("noSelection", errors.getFieldError("Procedure").getCode());
    }

    @Test
    public final void testInvalidProcedure() throws Exception
    {
        // Setup variable
        final ProcedureVariable procedureVariable = SampleObjects.sampleProcedureVariable();

        final SubmittedValues values = new SubmittedValues();
        values.setProcedure("ASDFASDFASDF");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(values, "submittedValues");
        
        final InputParserVisitor v = new InputParserVisitor(values, errors);
        v.visitProcedure(procedureVariable);
        
        assertEquals("invalid", errors.getFieldError("Procedure").getCode());
    }
    
    
}
