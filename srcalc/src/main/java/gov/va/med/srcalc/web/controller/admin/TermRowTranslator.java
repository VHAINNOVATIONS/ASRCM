package gov.va.med.srcalc.web.controller.admin;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.csv.CSVRecord;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.csv.ParseUtils;
import gov.va.med.srcalc.util.csv.RowTranslator;
import gov.va.med.srcalc.util.csv.TabularUploadError;
import gov.va.med.srcalc.web.view.admin.EditModelTerm;

/**
 * Translates rows form a Comma-Separated Value (CSV) file into EditModelTerm objects.
 */
class TermRowTranslator implements RowTranslator<EditModelTerm>
{
    private static final String FIRST_HEADER_CELL = "Term Type";
    
    @Override
    public boolean isHeaderRow(final CSVRecord record)
    {
        return record.get(0).equalsIgnoreCase(FIRST_HEADER_CELL);
    }

    /**
     * Translates a single row into an EditModelTerm object. Validation errors will be
     * recorded only for translation. This method does not actually validate the
     * EditModelTerm object itself.
     */
    @Override
    public Optional<EditModelTerm> translateRow(
            final CSVRecord record,
            final Collection<TabularUploadError> errors,
            final int rowNumber)
    {
        // Construct our own error list to detect if we added any errors.
        final ArrayList<TabularUploadError> rowErrors = new ArrayList<>();

        final EditModelTerm editTerm = new EditModelTerm();

        final String termTypeString = ParseUtils.tryGetValue(record, 0).toUpperCase();
        try
        {
            editTerm.setTermType(EditModelTerm.TermType.valueOf(termTypeString));
        }
        catch (final IllegalArgumentException ex)
        {
            rowErrors.add(TabularUploadError.forField(
                    rowNumber,
                    "termType",
                    EditModelTerm.TermType.class,
                    ValidationCodes.INVALID_OPTION,
                    new Object[] {ImmutableList.copyOf(EditModelTerm.TermType.values())},
                    ex.toString()));
        }
        
        editTerm.setKey(ParseUtils.tryGetValue(record, 1));
        editTerm.setOptionValue(ParseUtils.tryGetValue(record, 2));

        final String coeffString = ParseUtils.tryGetValue(record, 3);
        try
        {
            editTerm.setCoefficient(Float.parseFloat(coeffString));
        }
        catch (final NumberFormatException ex)
        {
            rowErrors.add(TabularUploadError.forField(
                    rowNumber,
                    "coefficient",
                    float.class,
                    ValidationCodes.TYPE_MISMATCH,
                    null,
                    ex.getMessage()));
        }
        
        // If we did not record any errors, created the EditModelTerm object and return
        // it.
        if (rowErrors.isEmpty())
        {
            return Optional.of(editTerm);
        }
        else
        {
            errors.addAll(rowErrors);
            return Optional.absent();
        }
    }
    
}
