package gov.va.med.srcalc.util.csv;

import gov.va.med.srcalc.util.ValidationCodes;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

/**
 * General-purpose class to parse a CSV file into an object per row.
 * @param <T> the type of object that represents each row
 */
public class CsvReader<T>
{
    private static final Logger fLogger = LoggerFactory.getLogger(CsvReader.class);
    
    private final RowTranslator<T> fRowTranslator;
    
    public CsvReader(final RowTranslator<T> rowTranslator)
    {
        fRowTranslator = rowTranslator;
    }

    /**
     * Parses the given CSVRecords from a CSV file into a List of objects.
     * @param records must be non-empty
     * @param errors for recording validation errors
     */
    private TabularParseResult<T> readProcedures(
            final List<CSVRecord> records, final Collection<TabularUploadError> errors)
    {
        int firstRow = 0;
        
        // Detect if the first row is a header and strip if so.
        if (fRowTranslator.isHeaderRow(records.get(0)))
        {
            fLogger.debug("Ignoring a header row in the CSV.");
            firstRow = 1;
        }

        // Parse each line and record errors.
        fLogger.trace(
                "About to translate {} records from CSV into Procedure objects.",
                records.size());
        final ArrayList<T> procedures = new ArrayList<>(records.size());
        for (int i = firstRow; i < records.size(); ++i)
        {
            final CSVRecord row = records.get(i);
            final Optional<T> rowObject = fRowTranslator.translateRow(row, errors, i + 1);
            // Per TabularParseResult, represent invalid objects as nulls.
            procedures.add(rowObject.orNull());
        }

        return new TabularParseResult<>(procedures, errors);
    }
    
    /**
     * <p>Translates the given CSV content into a List of objects.</p>
     * 
     * <p>Translation of each row into its representative object is handled by the
     * {@link RowTranslator} provided during construction. This class only handles the
     * generic iteration logic and global error handling.</p>
     * 
     * <p><strong>Warning:</strong> the given CSV content is read completely into memory
     * while parsing. This method does not therefore support very large CSV files (i.e.,
     * 50MB+).</p>
     * 
     * @param csvReader a Reader providing the CSV content. This method will close the
     * reader when done.
     */
    public TabularParseResult<T> readObjects(final Reader csvReader)
    {
        final ArrayList<TabularUploadError> errors = new ArrayList<>();

        try
        {
            final List<CSVRecord> records = CSVFormat.EXCEL.parse(csvReader).getRecords();
            fLogger.debug("The CSV file has {} rows.", records.size());

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
                    ValidationCodes.INVALID_CONTENTS,
                    null,
                    "Could not parse the CSV content."));
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

        return new TabularParseResult<>(Collections.<T>emptyList(), errors);
    }
    
}
