package gov.va.med.srcalc.web.view;

import java.io.IOException;

import gov.va.med.srcalc.domain.model.*;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag handler for srcalc:variableSpecific custom tag.
 */
public class VariableSpecificTag extends SimpleTagSupport
{
    private Variable fVariable;
    
    private JspFragment fNumericalFragment;
    private JspFragment fBooleanFragment;
    private JspFragment fMultiSelectFragment;
    private JspFragment fDiscreteNumericalFragment;
    private JspFragment fProcedureFragment;
    
    @Override
    public void doTag() throws JspException, IOException
    {
        final WriterVisitor visitor = new WriterVisitor();
        try
        {
            fVariable.accept(visitor);
        }
        catch (final IOException e)
        {
            // doTag() API permits re-throwing IOException, so just do that
            // as opposed to the catch-all below.
            throw e;
        }
        catch (final Exception e)
        {
            throw new JspException("Unable to generate variableInput tag.", e);
        }
    }
    
    public void setVariable(final Variable variable)
    {
        fVariable = variable;
    }
    
    public void setNumericalFragment(final JspFragment fragment)
    {
        fNumericalFragment = fragment;
    }

    public void setBooleanFragment(final JspFragment fragment)
    {
        fBooleanFragment = fragment;
    }

    public void setMultiSelectFragment(final JspFragment fragment)
    {
        fMultiSelectFragment = fragment;
    }

    public void setDiscreteNumericalFragment(final JspFragment fragment)
    {
        fDiscreteNumericalFragment = fragment;
    }

    public void setProcedureFragment(final JspFragment fragment)
    {
        fProcedureFragment = fragment;
    }

    private class WriterVisitor implements VariableVisitor
    {
        @Override
        public void visitNumerical(final NumericalVariable variable)
                throws JspException, IOException
        {
            fNumericalFragment.invoke(null);
        }

        @Override
        public void visitBoolean(final BooleanVariable variable)
                throws JspException, IOException
        {
            fBooleanFragment.invoke(null);
        }

        @Override
        public void visitMultiSelect(final MultiSelectVariable variable)
                throws JspException, IOException
        {
            fMultiSelectFragment.invoke(null);
        }
        
        @Override
        public void visitDiscreteNumerical(final DiscreteNumericalVariable variable)
                throws JspException, IOException
        {
            fDiscreteNumericalFragment.invoke(null);
        }

        @Override
        public void visitProcedure(final ProcedureVariable variable)
                throws JspException, IOException
        {
            fProcedureFragment.invoke(null);
        }
        
    }
}
