package gov.va.med.srcalc.web.view;

import static org.junit.Assert.*;

import java.util.Arrays;

import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.web.view.InputParserVisitor;
import gov.va.med.srcalc.web.view.VariableEntry;
import static gov.va.med.srcalc.web.view.VariableEntry.makeVariableValuePath;
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
        final MultiSelectVariable var = SampleModels.genderVariable();

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        // Note: Gender never set in dynamicValues.
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitMultiSelect(var);
        
        //Ensure there is no value recorded in {@Link InputParserVisitor}
        assertEquals(
                null,
                variableEntry.getDynamicValues().get(VariableEntry.makeDynamicValuePath(var.getKey())));
    }
    
    @Test
    public final void testInvalidMultiselect() throws Exception
    {
        // Setup variable
        final MultiSelectVariable var = SampleModels.genderVariable();

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        variableEntry.getDynamicValues().put("gender", "Unknown");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitMultiSelect(var);
        
        assertEquals(
                "invalid",
                errors.getFieldError(makeVariableValuePath(var)).getCode());
    }

    @Test
    public final void testUnspecifiedProcedure() throws Exception
    {
        // Setup variable
        final ProcedureVariable var = SampleModels.procedureVariable();

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        // Sometimes no value may be represented as an empty string.
        variableEntry.getDynamicValues().put(var.getKey(), "");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitProcedure(var);
        
        assertEquals(
                "noSelection",
                errors.getFieldError(makeVariableValuePath(var)).getCode());
    }

    @Test
    public final void testInvalidProcedure() throws Exception
    {
        // Setup variable
        final ProcedureVariable var = SampleModels.procedureVariable();

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        variableEntry.getDynamicValues().put(var.getKey(), "ASDFASDFASDF");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitProcedure(var);
        
        assertEquals(
                "invalid",
                errors.getFieldError(makeVariableValuePath(var)).getCode());
    }
    
    @Test
    public final void testUnspecifiedNumerical() throws Exception
    {
        // Setup variable
        final NumericalVariable var = SampleModels.ageVariable();

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        // Note: Age never set in dynamicValues.
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitNumerical(var);
        
        assertEquals(
                null,
                variableEntry.getDynamicValues().get(VariableEntry.makeDynamicValuePath(var.getKey())));
    }
    
    @Test
    public final void testInvalidNumerical() throws Exception
    {
        // Setup variable
        final NumericalVariable var = SampleModels.ageVariable();

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        variableEntry.getDynamicValues().put(var.getKey(), "asdfasdf");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitNumerical(var);
        
        assertEquals(
                "typeMismatch.float",
                errors.getFieldError(makeVariableValuePath(var)).getCode());
    }
    
    @Test
    public final void testTooLowNumerical() throws Exception
    {
        // Setup variable
        final NumericalVariable var = SampleModels.ageVariable();

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        variableEntry.getDynamicValues().put(var.getKey(), "-1");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitNumerical(var);
        
        assertEquals(
                "tooLowInclusive",
                errors.getFieldError(makeVariableValuePath(var)).getCode());
    }
    
    @Test
    public final void testTooHighNumerical() throws Exception
    {
        // Setup variable
        final NumericalVariable var = SampleModels.ageVariable();

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        variableEntry.getDynamicValues().put(var.getKey(), "1000");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitNumerical(var);
        
        assertEquals(
                "tooHighInclusive",
                errors.getFieldError(makeVariableValuePath(var)).getCode());
    }
    
    @Test
    public final void testUnspecifiedBoolean() throws Exception
    {
        // Setup variable
        final BooleanVariable dnrVariable = SampleModels.dnrVariable();

        final VariableEntry variableEntry = new VariableEntry(
                Arrays.asList(dnrVariable));
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
        final BooleanVariable dnrVariable = SampleModels.dnrVariable();

        final VariableEntry variableEntry = new VariableEntry(
                Arrays.asList(dnrVariable));
        variableEntry.getDynamicValues().put(dnrVariable.getKey(), "true");
        
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
        final BooleanVariable dnrVariable = SampleModels.dnrVariable();

        final VariableEntry variableEntry = new VariableEntry(
                Arrays.asList(dnrVariable));
        variableEntry.getDynamicValues().put(dnrVariable.getKey(), "blah");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitBoolean(dnrVariable);
        
        assertEquals(
                "non-true boolean should be false",
                Boolean.FALSE, v.getValues().get(0).getValue());
    }
    
    @Test
    public final void testUnspecifiedDiscreteNumerical() throws Exception
    {
        // Setup variable
        final DiscreteNumericalVariable var = SampleModels.wbcVariable();

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        // Clear out the default value.
        variableEntry.getDynamicValues().put(var.getKey(), "");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitDiscreteNumerical(var);
        
        assertEquals(
                null,
                variableEntry.getDynamicValues().get(VariableEntry.makeDynamicValuePath(var.getKey())));
    }
    @Test
    public final void testInvalidDiscreteNumerical() throws Exception
    {
        // Setup variable
        final DiscreteNumericalVariable var = SampleModels.wbcVariable();

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        variableEntry.getDynamicValues().put(var.getKey(), "Unknown");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitDiscreteNumerical(var);
        
        assertEquals(
                "invalid",
                errors.getFieldError(makeVariableValuePath(var)).getCode());
    }

    @Test
    public final void testValidNumberDiscreteNumerical() throws Exception
    {
        // Setup variable
        final DiscreteNumericalVariable var = SampleModels.wbcVariable();
        final String numericalName = VariableEntry.makeNumericalInputName(var.getKey());

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        variableEntry.getDynamicValues().put(
                var.getKey(), VariableEntry.SPECIAL_NUMERICAL);
        variableEntry.getDynamicValues().put(numericalName, "10.0");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitDiscreteNumerical(var);
        
        assertNotNull("should have a value", v.getValues().get(0).getValue());
    }

    @Test
    public final void testUnspecifiedNumberDiscreteNumerical() throws Exception
    {
        // Setup variable
        final DiscreteNumericalVariable var = SampleModels.wbcVariable();

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        variableEntry.getDynamicValues().put(
                var.getKey(), VariableEntry.SPECIAL_NUMERICAL);
        // Note: no numerical value actually specified
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitDiscreteNumerical(var);
        
        assertEquals(
                null,
                variableEntry.getDynamicValues().get(VariableEntry.makeDynamicValuePath(var.getKey())));
    }
    @Test
    public final void testInvalidNumberDiscreteNumerical() throws Exception
    {
        // Setup variable
        final DiscreteNumericalVariable var = SampleModels.wbcVariable();
        final String numericalName = VariableEntry.makeNumericalInputName(var.getKey());

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        variableEntry.getDynamicValues().put(
                var.getKey(), VariableEntry.SPECIAL_NUMERICAL);
        variableEntry.getDynamicValues().put(numericalName, "foo");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitDiscreteNumerical(var);
        
        assertEquals(
                "typeMismatch.float",
                errors.getFieldError(makeDynamicValuePath(numericalName)).getCode());
    }

    @Test
    public final void testTooLowDiscreteNumerical() throws Exception
    {
        // Setup variable
        final DiscreteNumericalVariable var = SampleModels.wbcVariable();
        final String numericalName = VariableEntry.makeNumericalInputName(var.getKey());

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        variableEntry.getDynamicValues().put(
                var.getKey(), VariableEntry.SPECIAL_NUMERICAL);
        variableEntry.getDynamicValues().put(numericalName, "0.1");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitDiscreteNumerical(var);
        
        assertEquals(
                "tooLowInclusive",
                errors.getFieldError(makeDynamicValuePath(numericalName)).getCode());
    }

    @Test
    public final void testTooHighDiscreteNumerical() throws Exception
    {
        // Setup variable
        final DiscreteNumericalVariable var = SampleModels.wbcVariable();
        final String numericalName = VariableEntry.makeNumericalInputName(var.getKey());

        final VariableEntry variableEntry = new VariableEntry(Arrays.asList(var));
        variableEntry.getDynamicValues().put(
                var.getKey(), VariableEntry.SPECIAL_NUMERICAL);
        variableEntry.getDynamicValues().put(numericalName, "51.0");
        
        final BeanPropertyBindingResult errors =
                new BeanPropertyBindingResult(variableEntry, "variableEntry");
        
        final InputParserVisitor v = new InputParserVisitor(variableEntry, errors);
        v.visitDiscreteNumerical(var);
        
        assertEquals(
                "tooHighInclusive",
                errors.getFieldError(makeDynamicValuePath(numericalName)).getCode());
    }
}
