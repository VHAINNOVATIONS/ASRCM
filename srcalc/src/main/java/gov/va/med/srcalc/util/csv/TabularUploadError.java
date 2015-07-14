package gov.va.med.srcalc.util.csv;

import java.util.*;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.DefaultMessageCodesResolver;

import com.google.common.base.MoreObjects;

/**
 * <p>Similar to Spring's {@link org.springframework.validation.FieldError} class,
 * but specifically represents an error in a tabular file upload.</p>
 * 
 * <p>Immutable.</p>
 */
public final class TabularUploadError implements MessageSourceResolvable
{
    /**
     * Special row number value indicating a global error on the whole upload instead of
     * a specific row.
     */
    public static int ROW_NUMBER_GLOBAL = -1;
    
    /**
     * The object name used for errors on the whole upload.
     */
    public static String OBJECT_NAME = "tabularUpload";
    
    private static final DefaultMessageCodesResolver fCodesResolver =
            new DefaultMessageCodesResolver();
    
    private final int fRowNumber;
    private final String fColumnName;
    private final String[] fCodes;
    private final Object[] fArguments;
    private final String fDefaultMessage;
    
    // Package-private to facilitate testing.
    /**
     * Constructs an instance.
     * @param rowNumber the associated row's number (1-based). May also be {@link
     * #ROW_NUMBER_GLOBAL}
     * @param columnName the associated field name of the target object. Must not be null.
     * @param codes the error codes. Must be a non-empty array.
     * @param arguments any necessary message arguments. May be null.
     * @param defaultMessage the default message to use if no codes could be resolved
     * @throws IllegalArgumentException if codes is empty
     */
    TabularUploadError(
            final int rowNumber,
            final String columnName,
            final String[] codes,
            final Object[] arguments,
            final String defaultMessage)
    {
        fRowNumber = rowNumber;
        fColumnName = Objects.requireNonNull(columnName, "field must not be null");
        fCodes = codes.clone();
        if (fCodes.length < 1)
        {
            throw new IllegalArgumentException("codes must be non-empty");
        }
        // Create a shallow copy. This permits calling code to break immutability by
        // changing the contained Objects, but hopefully that is unlikely.
        fArguments = (arguments != null) ? arguments.clone() : null;
        fDefaultMessage = defaultMessage;
    }
    
    /**
     * Creates an error for the whole upload. (For example, a corrupt uploaded file.)
     * @param code the error code. Must be non-null.
     * @param arguments any necessary message arguments. May be null.
     * @param defaultMessage the default message to use if no codes could be resolved
     * @return a new instance
     */
    public static TabularUploadError global(
            final String code, final Object[] arguments, final String defaultMessage)
    {
        return new TabularUploadError(
                ROW_NUMBER_GLOBAL,
                "",
                fCodesResolver.resolveMessageCodes(code, OBJECT_NAME),
                arguments,
                defaultMessage);
    }
    
    /**
     * <p>Creates an error for a particular cell.</p>
     * 
     * <p>Row numbers are 1-based to correspond to Excel.</p>
     * 
     * @param rowNumber the associated row's number (1-based)
     * @param columnName the associated column's name. Must not be null.
     * @param fieldType the expected type of the target field
     * @param code the primary error code
     * @param arguments any necessary message arguments. May be null.
     * @param defaultMessage the default message to use if no codes could be resolved
     * @return a new instance
     */
    public static TabularUploadError forField(
            final int rowNumber,
            final String columnName,
            final Class<?> fieldType,
            final String code,
            final Object[] arguments,
            final String defaultMessage)
    {
        return new TabularUploadError(
                rowNumber,
                columnName,
                fCodesResolver.resolveMessageCodes(code, OBJECT_NAME, columnName, fieldType),
                arguments,
                defaultMessage);
    }

    @Override
    public String[] getCodes()
    {
        return fCodes;
    }
    
    /**
     * Returns the default code, that is, the last code in the array.
     */
    public String getCode()
    {
        // fCodes is guaranteed to have at least one element.
        return fCodes[fCodes.length-1];
    }

    @Override
    public Object[] getArguments()
    {
        return fArguments;
    }

    @Override
    public String getDefaultMessage()
    {
        return fDefaultMessage;
    }
    
    /**
     * Returns a human-readable String describing the location, such as "Row 3, cptCode: ",
     * or an empty string in the case of a whole-table error.
     */
    public String getLocationPrefix()
    {
        if (fRowNumber == ROW_NUMBER_GLOBAL)
        {
            return "";
        }
        else
        {
            return String.format("Row %d, %s: ", fRowNumber, fColumnName);
        }
    }
    
    /**
     * Compares {@code TabularUploadError}s based on their row numbers, column names,
     * codes, and arguments. Does not consider the default message.
     * 
     * @return true if the above-listed attributes are equal, false otherwise
     */
    @Override
    public boolean equals(final Object o)
    {
        if (o instanceof TabularUploadError)
        {
            final TabularUploadError other = (TabularUploadError)o;
            
            return
                    (this.fRowNumber == other.fRowNumber) &&
                    (Objects.equals(this.fColumnName, other.fColumnName)) &&
                    (Arrays.equals(this.fCodes, other.fCodes)) &&
                    (Arrays.equals(this.fArguments, other.fArguments));
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Returns a hash code based on the row number, column name, codes, and arguments.
     * Does not depend on the default message.
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(
                fRowNumber,
                fColumnName,
                // Calling hashCode() directly on an array is identity-based. Call
                // Arrays.hashCode() to get value equality.
                Arrays.hashCode(fCodes),
                Arrays.hashCode(fArguments));
    }
    
    /**
     * Returns a String representation of this error. The exact format is unspecified,
     * but it will at minimum contain the error codes.
     */
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("rowNumber", fRowNumber)
                .add("columnName", fColumnName)
                .add("codes", Arrays.toString(fCodes))
                .add("arguments", Arrays.toString(fArguments))
                .add("defaultMessage", fDefaultMessage)
                .toString();
    }
}
