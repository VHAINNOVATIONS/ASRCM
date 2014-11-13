package gov.va.med.srcalc.controller;

import gov.va.med.srcalc.domain.variable.MultiSelectOption;
import gov.va.med.srcalc.domain.variable.MultiSelectVariable;
import gov.va.med.srcalc.domain.variable.NumericalVariable;
import gov.va.med.srcalc.domain.variable.VariableVisitor;

import java.io.IOException;
import java.io.Writer;

/**
 * Writes an HTML input control for visited {@link Variable}s.
 */
class InputGeneratorVisitor implements VariableVisitor
{
    private final Writer fWriter;
    
    public InputGeneratorVisitor(final Writer writer)
    {
        fWriter = writer;
    }
    
    protected void writeRadio(final MultiSelectVariable variable) throws IOException
    {
        for (final MultiSelectOption option : variable.getOptions())
        {
            fWriter.write(String.format(
                "<label class=\"radioLabel\">" +
                    "<input type=\"radio\" name=\"%s\" value=\"%s\"> " +
                    "%s</label>",
                variable.getDisplayName(),
                option.getValue(),
                option.getValue()));
        }
    }
    
    protected void writeDropdown(final MultiSelectVariable variable)
    {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public void visitMultiSelect(final MultiSelectVariable variable) throws IOException
    {
        // Display differently based on configured displayType.
        switch (variable.getDisplayType())
        {
            case Radio:
                writeRadio(variable);
                break;
            case Dropdown:
                writeDropdown(variable);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported displayType.");
        }
    }
    
    @Override
    public void visitNumerical(final NumericalVariable variable) throws Exception
    {
        // The maximum number of digits we expect.
        final int maxExpectedDigits = 8;
        
        fWriter.write(String.format(
                "<input type=\"text\" name=\"%s\" size=\"%d\">",
                variable.getDisplayName(), maxExpectedDigits));
    }
}