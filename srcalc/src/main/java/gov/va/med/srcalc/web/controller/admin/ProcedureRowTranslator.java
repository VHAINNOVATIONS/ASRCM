package gov.va.med.srcalc.web.controller.admin;

import gov.va.med.srcalc.domain.model.Procedure;
import gov.va.med.srcalc.util.ValidationCodes;
import gov.va.med.srcalc.util.ValidationUtils2;
import gov.va.med.srcalc.util.csv.*;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.csv.CSVRecord;

import com.google.common.base.Optional;

/**
 * Reads Procedure objects from a Comma-Separated Value (CSV) file.
 */
class ProcedureRowTranslator implements RowTranslator<Procedure>
{
    private static final String FIRST_HEADER_CELL = "CPT";
    
    @Override
    public boolean isHeaderRow(final CSVRecord record)
    {
        return record.get(0).equalsIgnoreCase(FIRST_HEADER_CELL);
    }
    
    @Override
    public Optional<Procedure> translateRow(
            final CSVRecord record,
            final Collection<TabularUploadError> errors,
            final int rowNumber)
    {
        // Construct our own error list to detect if we added any errors.
        final ArrayList<TabularUploadError> rowErrors = new ArrayList<>();

        /* Get all the string values first since they don't involve exceptions */
        
        final String cptCode = ParseUtils.tryGetValue(record, 0);
        if (cptCode.length() != Procedure.CPT_CODE_LENGTH)
        {
            rowErrors.add(TabularUploadError.forField(
                    rowNumber,
                    "cptCode",
                    String.class,
                    ValidationCodes.BAD_FIXED_LENGTH,
                    new Object[] { Procedure.CPT_CODE_LENGTH },
                    "invalid length"));
        }
        
        final String complexity = ParseUtils.tryGetValue(record, 3);
        ValidationUtils2.validateRequiredString(
                complexity, rowNumber, "complexity", Procedure.COMPLEXITY_MAX, rowErrors);
        
        final String longDesc = ParseUtils.tryGetValue(record, 4);
        ValidationUtils2.validateRequiredString(
                longDesc, rowNumber, "longDescription", Procedure.DESCRIPTION_MAX, rowErrors);
        
        final String shortDesc = ParseUtils.tryGetValue(record, 5);
        ValidationUtils2.validateRequiredString(
                shortDesc, rowNumber, "shortDescription", Procedure.DESCRIPTION_MAX, rowErrors);
        
        final String rvuString = ParseUtils.tryGetValue(record, 1);
        float rvu = Float.NaN;
        try
        {
            rvu = Float.parseFloat(rvuString);
        }
        catch (final NumberFormatException ex)
        {
            rowErrors.add(TabularUploadError.forField(
                    rowNumber,
                    "rvu",
                    float.class,
                    ValidationCodes.TYPE_MISMATCH,
                    null,
                    ex.getMessage()));
        }

        final String eligibleString = ParseUtils.tryGetValue(record, 2);
        boolean eligible = false;
        try
        {
            eligible = ParseUtils.parseCsvBoolean(eligibleString);
        }
        catch (final IllegalArgumentException ex)
        {
            // Error on parseCsvBoolean()
            rowErrors.add(TabularUploadError.forField(
                    rowNumber,
                    "eligible",
                    boolean.class,
                    ValidationCodes.TYPE_MISMATCH,
                    null,
                    ex.getMessage()));
        }
            
        // Only create the procedure if we did not record any validation errors for
        // this row--otherwise we would get an Exception when trying to construct
        // the Procedure.
        if (rowErrors.isEmpty())
        {
            return Optional.of(new Procedure(
                    cptCode, rvu, shortDesc, longDesc, complexity, eligible));
        }

        errors.addAll(rowErrors);

        return Optional.absent();
    }
}
