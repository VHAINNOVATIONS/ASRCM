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
    private JspFragment labFragment;
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
        labFragment = mock(JspFragment.class);
        tag.setLabFragment(labFragment);
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
        verify(labFragment, never()).invoke(null);
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
        verify(labFragment, never()).invoke(null);
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
        verify(labFragment, never()).invoke(null);
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
        verify(labFragment, never()).invoke(null);
        verify(procedureFragment).invoke(null);
    }

    @Test
    public final void testDoTagLab() throws Exception
    {
        // Behavior verification.
        tag.setVariable(SampleObjects.wbcVariable());
        tag.doTag();
        verify(numericalFragment, never()).invoke(null);
        verify(booleanFragment, never()).invoke(null);
        verify(multiSelectFragment, never()).invoke(null);
        verify(labFragment).invoke(null);
        verify(procedureFragment, never()).invoke(null);
    }
    
}
