package gov.va.med.srcalc.util.csv;

import java.util.Collection;

import org.apache.commons.csv.CSVRecord;

import com.google.common.base.Optional;

/**
 * Defines a type that translates a row from a CSV file into an object.
 * @param <T> the type of object representing each row
 */
public interface RowTranslator<T>
{
    /**
     * Returns true if the given CSV row is a header row and should be skipped. False
     * otherwise.
     */
    public boolean isHeaderRow(final CSVRecord record);

    /**
     * Translates a single CSVRecord into a single object of type T.
     * @param record the record to translate
     * @param errors a mutable collection for recording validation errors
     * @param rowNumber the row number for any validation errors (1-based)
     * @return the translated object, if there were no validation errors
     */
    public Optional<T> translateRow(
            final CSVRecord record,
            final Collection<TabularUploadError> errors,
            final int rowNumber);
}
