package gov.va.med.srcalc.controller;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.domain.variable.MultiSelectVariable.DisplayType;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;

/**
 * Tests {@link InputParserVisitor}, mainly validation.
 */
public class InputParserVisitorTest
{
    protected ProcedureVariable sampleProcedureVariable()
    {
        final ProcedureVariable var = new ProcedureVariable("Procedure");
        var.setProcedures(SampleObjects.sampleProcedureList());
        return var;
    }

    protected MultiSelectVariable sampleGenderVariable()
    {
        final List<MultiSelectOption> options = Arrays.asList(
                new MultiSelectOption("Male"),
                new MultiSelectOption("Female"));
        return new MultiSelectVariable("Gender", DisplayType.Radio, options);
    }

    @Test
    public final void testUnspecifiedMultiselect() throws Exception
    {
        // Setup variable
        final MultiSelectVariable var = sampleGenderVariable();

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
        final ProcedureVariable procedureVariable = sampleProcedureVariable();

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
        final ProcedureVariable procedureVariable = sampleProcedureVariable();

        final SubmittedValues values = new SubmittedValues();
        values.setProcedure("ASDFASDFASDF");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(values, "submittedValues");
        
        final InputParserVisitor v = new InputParserVisitor(values, errors);
        v.visitProcedure(procedureVariable);
        
        assertEquals("invalid", errors.getFieldError("Procedure").getCode());
    }
    
    
}
