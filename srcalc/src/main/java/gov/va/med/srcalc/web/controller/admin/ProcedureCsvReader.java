package gov.va.med.srcalc.web.controller.admin;

import gov.va.med.srcalc.domain.model.Procedure;
import gov.va.med.srcalc.util.*;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Collections;

import org.apache.commons.csv.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

/**
 * Reads Procedure objects from a Comma-Separated Value (CSV) file.
 */
public class ProcedureCsvReader
{
    /**
     * The error code used when the provided content is not actually a CSV file.
     */
    public static final String ERROR_CORRUPT_CSV = "corruptCsv";

    private static final Logger fLogger =
            LoggerFactory.getLogger(ProcedureCsvReader.class);
    
    /**
     * Returns the indexed value for the CSVRecord, as {@link CSVRecord#get(int)}, but
     * returns an empty string for a missing value.
     * @return the value at the specified index, if it exists, or an empty string
     * otherwise
     */
    private String tryGetValue(final CSVRecord record, final int i)
    {
        return (record.size() > i) ? record.get(i) : "";
    }
    
    /**
     * Parses a Y/N string into a boolean value.
     * @param s must be "Y" or "N" (case-insensitive)
     * @return true for "Y", false for "N"
     * @throws IllegalArgumentException if s is not "Y" or "N"
     */
    private boolean parseCsvBoolean(final String s)
    {
        if (s.equalsIgnoreCase("Y"))
        {
            return true;
        }
        else if (s.equalsIgnoreCase("N"))
        {
            return false;
        }
        else
        {
            throw new IllegalArgumentException("The String value must be Y or N.");
        }
    }
    
    /**
     * Translates a single CSVRecord into a single Procedure object.
     * @param record the CSVRecord to translate
     * @param errors to record validation errors
     * @param rowNumber the row number for any validation errors (1-based)
     * @return the translated Procedure, if there were no validation errors
     */
    private Optional<Procedure> translateRecord(
            final CSVRecord record,
            final Collection<TabularUploadError> errors,
            final int rowNumber)
    {
        // Construct our own error list to detect if we added any errors.
        final ArrayList<TabularUploadError> rowErrors = new ArrayList<>();

        /* Get all the string values first since they don't involve exceptions */
        
        final String cptCode = tryGetValue(record, 0);
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
        
        final String complexity = tryGetValue(record, 3);
        ValidationUtils2.validateRequiredString(
                complexity, rowNumber, "complexity", Procedure.COMPLEXITY_MAX, rowErrors);
        
        final String longDesc = tryGetValue(record, 4);
        ValidationUtils2.validateRequiredString(
                longDesc, rowNumber, "longDescription", Procedure.DESCRIPTION_MAX, rowErrors);
        
        final String shortDesc = tryGetValue(record, 5);
        ValidationUtils2.validateRequiredString(
                shortDesc, rowNumber, "shortDescription", Procedure.DESCRIPTION_MAX, rowErrors);
        
        final String rvuString = tryGetValue(record, 1);
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

        final String eligibleString = tryGetValue(record, 2);
        boolean eligible = false;
        try
        {
            eligible = parseCsvBoolean(eligibleString);
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

    /**
     * Parses the given CSVRecords from a CSV file into a List of Procedures.
     * @param records must be non-empty
     * @param errors for recording validation errors
     */
    private ParseResult readProcedures(
            final List<CSVRecord> records, final Collection<TabularUploadError> errors)
    {
        int firstRow = 0;
        
        // Detect if the first row is a header and strip if so.
        if (records.get(0).get(0).equalsIgnoreCase("cpt"))
        {
            fLogger.debug("Ignoring a header row in the CSV.");
            firstRow = 1;
        }

        // Parse each line and record errors.
        fLogger.trace(
                "About to translate {} records from CSV into Procedure objects.",
                records.size());
        final ArrayList<Procedure> procedures = new ArrayList<>(records.size());
        for (int i = firstRow; i < records.size(); ++i)
        {
            procedures.add(
                    translateRecord(records.get(i), errors, i + 1).orNull());
        }

        return new ParseResult(procedures, errors);
    }
    
    /**
     * <p>Translates the given CSV content into a List of Procedure objects.</p>
     * 
     * <p><strong>Warning:</strong> the given CSV content is read completely into memory
     * while parsing. This method does not therefore support very large CSV files (i.e.,
     * 50MB+).</p>
     * 
     * @param csvReader a Reader providing the CSV content. This method will close the
     * reader when done.
     * @return a ParseResult object containing the parsed Procedures and any validation
     * errors
     */
    public ParseResult readProcedures(final Reader csvReader)
    {
        final ArrayList<TabularUploadError> errors = new ArrayList<>();

        try
        {
            final List<CSVRecord> records = CSVFormat.EXCEL.parse(csvReader).getRecords();
            fLogger.debug("The procedure CSV has {} rows.", records.size());

            if (records.isEmpty())
            {
                errors.add(TabularUploadError.global(
                        ValidationCodes.NO_VALUE, null, "no records"));
            }
            else
            {
                return readProcedures(records, errors);
            }
        }
        catch (final IOException ex)
        {
            errors.add(TabularUploadError.global(
                    ERROR_CORRUPT_CSV, null, "Could not parse the CSV content."));
        }
        finally
        {
            try
            {
                csvReader.close();
            }
            catch (final IOException ex)
            {
                fLogger.warn("Failed to close the CSV reader.", ex);
            }
        }

        return new ParseResult(Collections.<Procedure>emptyList(), errors);
    }
    
    public final class ParseResult
    {
        private final List<Procedure> fProcedures;
        private final ImmutableCollection<TabularUploadError> fErrors;
        
        ParseResult(
                final List<Procedure> procedures,
                final Collection<TabularUploadError> errors)
        {
            // We use an unmodifiable list here to permit null elements.
            fProcedures = Collections.unmodifiableList(procedures);
            fErrors = ImmutableList.copyOf(errors);
        }

        /**
         * <p>The parsed list of Procedures.</p>
         * 
         * <p>
         * If there were no validation errors, this completely represents the CSV content.
         * Otherwise, any invalid rows will be represented by a null Procedure. We use
         * nulls here because a List&lt;Optional&lt;Procedure&gt;&gt; would be too
         * awkward.
         * </p>
         * 
         * @return an unmodifiable list of Procedures, in CSV order. May contain nulls.
         */
        public List<Procedure> getProcedures()
        {
            return fProcedures;
        }

        /**
         * Contains the validation errors, if any.
         */
        public ImmutableCollection<TabularUploadError> getErrors()
        {
            return fErrors;
        }
    }
}
