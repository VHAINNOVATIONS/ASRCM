package gov.va.med.srcalc.web;

import static org.junit.Assert.*;

import java.util.ArrayList;

import gov.va.med.srcalc.domain.calculation.*;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.web.view.VariableEntry;

import org.junit.Before;
import org.junit.Test;


/**
 * Tests {@link DynamicValueVisitor}.
 */
public class DynamicValueVisitorTest
{
    private DynamicValueVisitor fVisitor;
    
    @Before
    public void visitorSetup()
    {
        final VariableEntry variableEntry = new VariableEntry(new ArrayList<Variable>());
        fVisitor = new DynamicValueVisitor(variableEntry);
    }
    
    @Test
    public final void testNumerical() throws Exception
    {
        final NumericalVariable var = SampleModels.ageVariable();
        final NumericalValue val = new NumericalValue(var, 1.2f);

        fVisitor.visitNumerical(val);
        assertEquals(String.valueOf(1.2f), fVisitor.getVariableEntry().getDynamicValues().get("age"));
    }
    
    @Test
    public final void testDiscreteNumerical() throws Exception
    {
        final DiscreteNumericalVariable var = SampleModels.wbcVariable();
        final MultiSelectOption option = new MultiSelectOption("WNL");
        final DiscreteNumericalVariable.Category wnl =
                new DiscreteNumericalVariable.Category(option, 20.0f, true);
        final DiscreteNumericalValue val = DiscreteNumericalValue.fromCategory(var, wnl);
        
        fVisitor.visitDiscreteNumerical(val);
        assertNotNull("should have a value", fVisitor.getVariableEntry().getDynamicValues().get("wbc"));
    }
    
    @Test
    public final void testBoolean()
    {
        final BooleanVariable var = SampleModels.dnrVariable();
        final BooleanValue val = new BooleanValue(var, true);
        fVisitor.visitBoolean(val);
        assertEquals("true", fVisitor.getVariableEntry().getDynamicValues().get("dnr"));
    }
    
    @Test
    public final void testMultiSelect()
    {
        final MultiSelectVariable var = SampleModels.genderVariable();
        final MultiSelectValue val = new MultiSelectValue(var, new MultiSelectOption("Male"));
        fVisitor.visitMultiSelect(val);
        assertEquals("Male", fVisitor.getVariableEntry().getDynamicValues().get("gender"));
    }
    
    @Test
    public final void testProcedure()
    {
        final ProcedureVariable var = SampleModels.procedureVariable();
        final ProcedureValue val = new ProcedureValue(var, SampleModels.repairLeftProcedure());
        fVisitor.visitProcedure(val);
        assertEquals("26546", fVisitor.getVariableEntry().getDynamicValues().get("procedure"));
    }
}
