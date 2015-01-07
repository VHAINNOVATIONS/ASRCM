package gov.va.med.srcalc.web.view;

import java.io.IOException;

import gov.va.med.srcalc.domain.variable.*;

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
    private JspFragment fLabFragment;
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
    
    public void setNumericalFragment(JspFragment numericalFragment)
    {
        fNumericalFragment = numericalFragment;
    }

    public void setBooleanFragment(JspFragment booleanFragment)
    {
        fBooleanFragment = booleanFragment;
    }

    public void setMultiSelectFragment(JspFragment multiSelectFragment)
    {
        fMultiSelectFragment = multiSelectFragment;
    }

    public void setLabFragment(JspFragment labFragment)
    {
        fLabFragment = labFragment;
    }

    public void setProcedureFragment(JspFragment procedureFragment)
    {
        fProcedureFragment = procedureFragment;
    }

    private class WriterVisitor implements VariableVisitor
    {
        @Override
        public void visitNumerical(NumericalVariable variable)
                throws JspException, IOException
        {
            fNumericalFragment.invoke(null);
        }

        @Override
        public void visitBoolean(BooleanVariable variable)
                throws JspException, IOException
        {
            fBooleanFragment.invoke(null);
        }

        @Override
        public void visitMultiSelect(MultiSelectVariable variable)
                throws JspException, IOException
        {
            fMultiSelectFragment.invoke(null);
        }
        
        @Override
        public void visitDiscreteNumerical(DiscreteNumericalVariable variable) throws Exception
        {
            fLabFragment.invoke(null);
        }

        @Override
        public void visitProcedure(ProcedureVariable variable)
                throws JspException, IOException
        {
            fProcedureFragment.invoke(null);
        }
        
    }
}
