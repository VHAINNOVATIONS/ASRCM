package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.Procedure;
import gov.va.med.srcalc.domain.variable.*;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

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
         * Write an HTML table with a row for each procedure containing the
         * CPT code, long description, RVU, and a radio button for selection.
         * 
         * Javascript code in enterVariables.jsp transforms this table into
         * a jQuery UI dialog for a much better user experience.
         */
        
        // Cap the table at 100 rows for now.
        final int numToDisplay = Math.min(variable.getProcedures().size(), 100);
        final List<Procedure> truncatedProcedures =
                variable.getProcedures().subList(0, numToDisplay);
        
        // Put the variable name in a "data" attribute to make it accessible to
        // Javascript.
        fWriter.write(String.format(
                "<div class=\"procedureSelectGroup dialog\" data-var-name=\"%s\" title=\"Select %s\">",
                variable.getDisplayName(), variable.getDisplayName()));
        fWriter.write("<table>");
        fWriter.write("<thead><tr><th>Select</th><th>CPT Code</th><th>Description</th><th>RVU</th></tr></thead>\n");
        for (Procedure p : truncatedProcedures)
        {
            fWriter.write("<tr>");
            fWriter.write(String.format(
                    // Write Procedure.toString() as a "data" attribute to make
                    // it accessible to Javascript.
                    "<td class=\"selectRadio\"><input type=\"radio\" name=\"%s\" value=\"%s\" data-display-string=\"%s\"></td>",
                    variable.getDisplayName(),
                    p.getCptCode(),
                    p.toString()));
            fWriter.write(String.format(
                    "<td>%s</td><td>%s</td><td>%s</td>",
                    p.getCptCode(),
                    p.getLongDescription(),
                    Float.toString(p.getRvu())));
            fWriter.write("</tr>\n");
        }
        fWriter.write("</table>");
        fWriter.write("</div>");
    }
}