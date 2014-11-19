package gov.va.med.srcalc.controller;

import gov.va.med.srcalc.domain.Procedure;
import gov.va.med.srcalc.domain.variable.*;

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
    
    @Override
    public void visitProcedure(final ProcedureVariable variable) throws Exception
    {
        /*-
         * Here's the general idea:
         * 
         * - We have a jQuery UI Dialog containing a table of all the procedures
         *   with a radio button for each procedure to select it and a Select
         *   button to close the dialog.
         *   
         * - When the user clicks Select, the CPT code is saved to a hidden
         *   input and Procedure.toString() is displayed on the variable entry
         *   form.
         *   
         * Of course the below code is coupled with enterVariables.jsp.
         */
        
        fWriter.write(String.format(
                "<div class=\"procedureSelectGroup\" data-var-name=\"%s\" title=\"Select %s\">",
                variable.getDisplayName(), variable.getDisplayName()));
        fWriter.write("<table>");
        fWriter.write("<thead><tr><th>Select</th><th>CPT Code</th><th>Description</th><th>RVU</th></thead>");
        for (Procedure p : variable.getProcedures())
        {
            fWriter.write("<tr>");
            fWriter.write(String.format(
                    "<td class=\"selectRadio\"><input type=\"radio\" name=\"%s\" value=\"%s\" data-display-string=\"%s\"></td>",
                    variable.getDisplayName(),
                    p.getCptCode(),
                    p.toString()));
            fWriter.write(String.format(
                    "<td>%s</td><td>%s</td><td>%s</td>",
                    p.getCptCode(),
                    p.getLongDescription(),
                    Float.toString(p.getRvu())));
            fWriter.write("</tr>");
        }
        fWriter.write("</table>");
        fWriter.write("</div>");
        // TODO: write this using Javascript instead
        fWriter.write(String.format(
                "<input type=\"hidden\" name=\"%s\">",
                variable.getDisplayName()));
        fWriter.write(String.format(
                "<span id=\"selectedProcedureDisplay\"></span> <a id=\"selectProcedure_%s\" class=\"selectProcedureLink\" href=\"#\">Select</a>",
                variable.getDisplayName(), variable.getDisplayName()));
    }
}