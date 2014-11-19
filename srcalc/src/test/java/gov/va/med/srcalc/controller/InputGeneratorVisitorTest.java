package gov.va.med.srcalc.controller;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.domain.variable.MultiSelectVariable.DisplayType;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class InputGeneratorVisitorTest
{
    private InputGeneratorVisitor fVisitor;
    private StringWriter fWriter;
    
    @Before
    public void setUp()
    {
        fWriter = new StringWriter();
        fVisitor = new InputGeneratorVisitor(fWriter);
    }

    // not yet implemented, placeholder test
    @Test(expected = UnsupportedOperationException.class) 
    public final void testVisitDropdown()
        throws Exception
    {
        // Setup variable
        List<MultiSelectOption> options = Arrays.asList(
                new MultiSelectOption("ddvalue1"),
                new MultiSelectOption("ddvalue2"));
        MultiSelectVariable var = new MultiSelectVariable("ddTest", DisplayType.Dropdown, options);
        
        // Verify behavior
        var.accept(fVisitor);
    }
    
    @Test
    public final void testVisitRadio()
        throws Exception
    {
        // Setup variable
        List<MultiSelectOption> options = Arrays.asList(
                new MultiSelectOption("value1"),
                new MultiSelectOption("value2"));
        MultiSelectVariable var = new MultiSelectVariable("radioTest", DisplayType.Radio, options);
        
        // Verify behavior
        var.accept(fVisitor);
        final String EXPECTED =
                "<label class=\"radioLabel\"><input type=\"radio\" name=\"radioTest\" value=\"value1\"> value1</label>" +
                "<label class=\"radioLabel\"><input type=\"radio\" name=\"radioTest\" value=\"value2\"> value2</label>";
        assertEquals(EXPECTED, fWriter.toString());
    }
    
    @Test
    public final void testVisitNumerical()
        throws Exception
    {
        // Setup variable
        NumericalVariable var = new NumericalVariable("numTest");
        var.setMinValue(2);
        var.setMaxValue(100);
        
        // Verify behavior
        var.accept(fVisitor);
        assertEquals("<input type=\"text\" name=\"numTest\" size=\"8\">", fWriter.toString());
    }
    
    @Test
    public final void testVisitProcedure()
        throws Exception
    {
        // Setup variable
        ProcedureVariable var = new ProcedureVariable("procedure");
        
        // Verify behavior
        var.accept(fVisitor);
        final String EXPECTED =
                "<select name=\"procedure\">" +
                "<option value=\"26545\">26545 - Repair right hand (5.05)</option>" +
                "<option value=\"26546\">26546 - Repair left hand (10.06)</option>" +
                "</select>";
        assertEquals(EXPECTED, fWriter.toString());
    }
    
}
