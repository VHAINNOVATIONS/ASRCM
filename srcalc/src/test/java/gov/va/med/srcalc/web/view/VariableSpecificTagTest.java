package gov.va.med.srcalc.web.view;

import static org.mockito.Mockito.*;
import gov.va.med.srcalc.domain.model.SampleModels;

import javax.servlet.jsp.tagext.JspFragment;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link VariableSpecificTag} class.
 */
public class VariableSpecificTagTest
{
    // A set of mock JspFragments to verify behavior.
    private JspFragment fNumericalFragment;
    private JspFragment fBooleanFragment;
    private JspFragment fMultiSelectFragment;
    private JspFragment fProcedureFragment;
    private JspFragment fDiscreteNumericalFragment;
    private VariableSpecificTag fTag;
    
    @Before
    public void setup()
    {
        // Create the tag with a bunch of mock fragments.
        fTag = new VariableSpecificTag();
        fNumericalFragment = mock(JspFragment.class);
        fTag.setNumericalFragment(fNumericalFragment);
        fBooleanFragment = mock(JspFragment.class);
        fTag.setBooleanFragment(fBooleanFragment);
        fMultiSelectFragment = mock(JspFragment.class);
        fTag.setMultiSelectFragment(fMultiSelectFragment);
        fDiscreteNumericalFragment = mock(JspFragment.class);
        fTag.setDiscreteNumericalFragment(fDiscreteNumericalFragment);
        fProcedureFragment = mock(JspFragment.class);
        fTag.setProcedureFragment(fProcedureFragment);
    }

    @Test
    public final void testDoTagNumerical() throws Exception
    {
        // Behavior verification.
        fTag.setVariable(SampleModels.ageVariable());
        fTag.doTag();
        verify(fNumericalFragment).invoke(null);
        verify(fBooleanFragment, never()).invoke(null);
        verify(fMultiSelectFragment, never()).invoke(null);
        verify(fDiscreteNumericalFragment, never()).invoke(null);
        verify(fProcedureFragment, never()).invoke(null);
    }

    @Test
    public final void testDoTagBoolean() throws Exception
    {
        // Behavior verification.
        fTag.setVariable(SampleModels.dnrVariable());
        fTag.doTag();
        verify(fNumericalFragment, never()).invoke(null);
        verify(fBooleanFragment).invoke(null);
        verify(fMultiSelectFragment, never()).invoke(null);
        verify(fDiscreteNumericalFragment, never()).invoke(null);
        verify(fProcedureFragment, never()).invoke(null);
    }

    @Test
    public final void testDoTagMultiSelect() throws Exception
    {
        // Behavior verification.
        fTag.setVariable(SampleModels.functionalStatusVariable());
        fTag.doTag();
        verify(fNumericalFragment, never()).invoke(null);
        verify(fBooleanFragment, never()).invoke(null);
        verify(fMultiSelectFragment).invoke(null);
        verify(fDiscreteNumericalFragment, never()).invoke(null);
        verify(fProcedureFragment, never()).invoke(null);
    }

    @Test
    public final void testDoTagProcedure() throws Exception
    {
        // Behavior verification.
        fTag.setVariable(SampleModels.procedureVariable());
        fTag.doTag();
        verify(fNumericalFragment, never()).invoke(null);
        verify(fBooleanFragment, never()).invoke(null);
        verify(fMultiSelectFragment, never()).invoke(null);
        verify(fDiscreteNumericalFragment, never()).invoke(null);
        verify(fProcedureFragment).invoke(null);
    }

    @Test
    public final void testDoTagDiscreteNumerical() throws Exception
    {
        // Behavior verification.
        fTag.setVariable(SampleModels.wbcVariable());
        fTag.doTag();
        verify(fNumericalFragment, never()).invoke(null);
        verify(fBooleanFragment, never()).invoke(null);
        verify(fMultiSelectFragment, never()).invoke(null);
        verify(fDiscreteNumericalFragment).invoke(null);
        verify(fProcedureFragment, never()).invoke(null);
    }
    
}
