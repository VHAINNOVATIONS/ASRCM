package gov.va.med.srcalc.controller;

import java.io.IOException;

import gov.va.med.srcalc.domain.variable.Variable;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag handler for srcalc:variableInput custom tag.
 */
public class VariableInputTag extends SimpleTagSupport
{
    private Variable fVariable;
    
    @Override
    public void doTag() throws JspException, IOException
    {
        final JspWriter out = getJspContext().getOut();
        final InputGeneratorVisitor visitor = new InputGeneratorVisitor(out);
        try
        {
            fVariable.accept(visitor);
        }
        catch (IOException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new JspException("Unable to generate variableInput tag.", e);
        }
    }
    
    public void setVariable(Variable variable)
    {
        fVariable = variable;
    }
}
