package gov.va.med.srcalc.web.view;

import static org.mockito.Mockito.*;
import gov.va.med.srcalc.domain.SampleObjects;

import javax.servlet.jsp.tagext.JspFragment;

import org.junit.Before;
import org.junit.Test;

public class VariableSpecificTagTest
{
    // A set of mock JspFragments to verify behavior.
    private JspFragment numericalFragment;
    private JspFragment booleanFragment;
    private JspFragment multiSelectFragment;
    private JspFragment procedureFragment;
    private JspFragment discreteNumericalFragment;
    private VariableSpecificTag tag;
    
    @Before
    public void setup()
    {
        // Create the tag with a bunch of mock fragments.
        tag = new VariableSpecificTag();
        numericalFragment = mock(JspFragment.class);
        tag.setNumericalFragment(numericalFragment);
        booleanFragment = mock(JspFragment.class);
        tag.setBooleanFragment(booleanFragment);
        multiSelectFragment = mock(JspFragment.class);
        tag.setMultiSelectFragment(multiSelectFragment);
        discreteNumericalFragment = mock(JspFragment.class);
        tag.setDiscreteNumericalFragment(discreteNumericalFragment);
        procedureFragment = mock(JspFragment.class);
        tag.setProcedureFragment(procedureFragment);
    }

    @Test
    public final void testDoTagNumerical() throws Exception
    {
        // Behavior verification.
        tag.setVariable(SampleObjects.sampleAgeVariable());
        tag.doTag();
        verify(numericalFragment).invoke(null);
        verify(booleanFragment, never()).invoke(null);
        verify(multiSelectFragment, never()).invoke(null);
        verify(discreteNumericalFragment, never()).invoke(null);
        verify(procedureFragment, never()).invoke(null);
    }

    @Test
    public final void testDoTagBoolean() throws Exception
    {
        // Behavior verification.
        tag.setVariable(SampleObjects.dnrVariable());
        tag.doTag();
        verify(numericalFragment, never()).invoke(null);
        verify(booleanFragment).invoke(null);
        verify(multiSelectFragment, never()).invoke(null);
        verify(discreteNumericalFragment, never()).invoke(null);
        verify(procedureFragment, never()).invoke(null);
    }

    @Test
    public final void testDoTagMultiSelect() throws Exception
    {
        // Behavior verification.
        tag.setVariable(SampleObjects.functionalStatusVariable());
        tag.doTag();
        verify(numericalFragment, never()).invoke(null);
        verify(booleanFragment, never()).invoke(null);
        verify(multiSelectFragment).invoke(null);
        verify(discreteNumericalFragment, never()).invoke(null);
        verify(procedureFragment, never()).invoke(null);
    }

    @Test
    public final void testDoTagProcedure() throws Exception
    {
        // Behavior verification.
        tag.setVariable(SampleObjects.sampleProcedureVariable());
        tag.doTag();
        verify(numericalFragment, never()).invoke(null);
        verify(booleanFragment, never()).invoke(null);
        verify(multiSelectFragment, never()).invoke(null);
        verify(discreteNumericalFragment, never()).invoke(null);
        verify(procedureFragment).invoke(null);
    }

    @Test
    public final void testDoTagDiscreteNumerical() throws Exception
    {
        // Behavior verification.
        tag.setVariable(SampleObjects.wbcVariable());
        tag.doTag();
        verify(numericalFragment, never()).invoke(null);
        verify(booleanFragment, never()).invoke(null);
        verify(multiSelectFragment, never()).invoke(null);
        verify(discreteNumericalFragment).invoke(null);
        verify(procedureFragment, never()).invoke(null);
    }
    
}
