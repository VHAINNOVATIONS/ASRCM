package gov.va.med.srcalc.web.view;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.SampleObjects;
import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.web.view.InputParserVisitor;
import gov.va.med.srcalc.web.view.VariableEntry;
import static gov.va.med.srcalc.web.view.VariableEntry.makeDynamicValuePath;

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

        final VariableEntry variableEntry = new VariableEntry();
        // Note: Gender never set in dynamicValues.
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitMultiSelect(var);
        
        assertEquals("noSelection", errors.getFieldError("dynamicValues[Gender]").getCode());
    }

    @Test
    public final void testInvalidMultiselect() throws Exception
    {
        // Setup variable
        final MultiSelectVariable var = SampleObjects.sampleGenderVariable();

        final VariableEntry variableEntry = new VariableEntry();
        variableEntry.getDynamicValues().put("Gender", "Unknown");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitMultiSelect(var);
        
        assertEquals("invalid", errors.getFieldError("dynamicValues[Gender]").getCode());
    }

    @Test
    public final void testUnspecifiedProcedure() throws Exception
    {
        // Setup variable
        final ProcedureVariable procedureVariable = SampleObjects.sampleProcedureVariable();
        final String varName = procedureVariable.getDisplayName();

        final VariableEntry variableEntry = new VariableEntry();
        // Sometimes no value may be represented as an empty string.
        variableEntry.getDynamicValues().put(varName, "");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitProcedure(procedureVariable);
        
        assertEquals(
                "noSelection",
                errors.getFieldError(makeDynamicValuePath(varName)).getCode());
    }

    @Test
    public final void testInvalidProcedure() throws Exception
    {
        // Setup variable
        final ProcedureVariable procedureVariable = SampleObjects.sampleProcedureVariable();
        final String varName = procedureVariable.getDisplayName();

        final VariableEntry variableEntry = new VariableEntry();
        variableEntry.getDynamicValues().put(varName, "ASDFASDFASDF");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitProcedure(procedureVariable);
        
        assertEquals(
                "invalid",
                errors.getFieldError(makeDynamicValuePath(varName)).getCode());
    }
    
    @Test
    public final void testUnspecifiedNumerical() throws Exception
    {
        // Setup variable
        final NumericalVariable ageVariable = SampleObjects.sampleAgeVariable();
        final String varName = ageVariable.getDisplayName();

        final VariableEntry variableEntry = new VariableEntry();
        // Note: Age never set in dynamicValues.
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitNumerical(ageVariable);
        
        assertEquals(
                "noInput.float",
                errors.getFieldError(makeDynamicValuePath(varName)).getCode());
    }
    
    @Test
    public final void testInvalidNumerical() throws Exception
    {
        // Setup variable
        final NumericalVariable ageVariable = SampleObjects.sampleAgeVariable();
        final String varName = ageVariable.getDisplayName();

        final VariableEntry variableEntry = new VariableEntry();
        variableEntry.getDynamicValues().put(varName, "asdfasdf");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitNumerical(ageVariable);
        
        assertEquals(
                "typeMismatch.float",
                errors.getFieldError(makeDynamicValuePath(varName)).getCode());
    }
    
    @Test
    public final void testTooLowNumerical() throws Exception
    {
        // Setup variable
        final NumericalVariable ageVariable = SampleObjects.sampleAgeVariable();
        final String varName = ageVariable.getDisplayName();

        final VariableEntry variableEntry = new VariableEntry();
        variableEntry.getDynamicValues().put(varName, "-1");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitNumerical(ageVariable);
        
        assertEquals(
                "tooLow",
                errors.getFieldError(makeDynamicValuePath(varName)).getCode());
    }
    
    @Test
    public final void testTooHighNumerical() throws Exception
    {
        // Setup variable
        final NumericalVariable ageVariable = SampleObjects.sampleAgeVariable();
        final String varName = ageVariable.getDisplayName();

        final VariableEntry variableEntry = new VariableEntry();
        variableEntry.getDynamicValues().put(varName, "1000");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitNumerical(ageVariable);
        
        assertEquals(
                "tooHigh",
                errors.getFieldError(makeDynamicValuePath(varName)).getCode());
    }
    
    @Test
    public final void testUnspecifiedBoolean() throws Exception
    {
        // Setup variable
        final BooleanVariable dnrVariable = SampleObjects.dnrVariable();

        final VariableEntry variableEntry = new VariableEntry();
        // Note: DNR never set in dynamicValues.
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitBoolean(dnrVariable);
        
        assertEquals(
                "unspecified boolean should be false",
                Boolean.FALSE, v.getValues().get(0).getValue());
    }
    
    @Test
    public final void testTrueBoolean() throws Exception
    {
        // Setup variable
        final BooleanVariable dnrVariable = SampleObjects.dnrVariable();
        final String varName = dnrVariable.getDisplayName();

        final VariableEntry variableEntry = new VariableEntry();
        variableEntry.getDynamicValues().put(varName, "true");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitBoolean(dnrVariable);
        
        assertEquals(
                "true boolean should be true",
                Boolean.TRUE, v.getValues().get(0).getValue());
    }
    
    @Test
    public final void testFalseBoolean() throws Exception
    {
        // Setup variable
        final BooleanVariable dnrVariable = SampleObjects.dnrVariable();
        final String varName = dnrVariable.getDisplayName();

        final VariableEntry variableEntry = new VariableEntry();
        variableEntry.getDynamicValues().put(varName, "blah");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitBoolean(dnrVariable);
        
        assertEquals(
                "non-true boolean should be false",
                Boolean.FALSE, v.getValues().get(0).getValue());
    }
}
