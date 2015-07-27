package gov.va.med.srcalc.util.csv;

import java.util.Collection;
import java.util.List;
import java.util.Collections;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

/**
 * Represents the result, including validation errors, of parsing a tabular upload.
 * @param <T> the type of objects representing each row
 */
public final class TabularParseResult<T>
{
    private final List<T> fRowObjects;
    private final ImmutableCollection<TabularUploadError> fErrors;
    
    /**
     * Constructs an instance.
     * @param rowObjects objects created from the tabular content (may contain nulls)
     * @param errors any validation errors (must not contain nulls)
     */
    public TabularParseResult(
            final List<T> rowObjects,
            final Collection<TabularUploadError> errors)
    {
        // We use an unmodifiable list here to permit null elements.
        fRowObjects = Collections.unmodifiableList(rowObjects);
        fErrors = ImmutableList.copyOf(errors);
    }

    /**
     * <p>The objects created from the tabular content.</p>
     * 
     * <p>
     * If there were no validation errors, this completely represents the CSV content.
     * Otherwise, any invalid rows will be represented by a null object. We use
     * nulls here because a List&lt;Optional&lt;T&gt;&gt; would be too
     * awkward.
     * </p>
     * 
     * @return an unmodifiable list of object, in CSV order. May contain nulls.
     */
    public List<T> getRowObjects()
    {
        return fRowObjects;
    }

    /**
     * Contains the validation errors, if any.
     */
    public ImmutableCollection<TabularUploadError> getErrors()
    {
        return fErrors;
    }
    
    /**
     * Returns true if this result has any errors. Equivalent to
     * {@code !getErrors.isEmpty()}.
     */
    public boolean hasErrors()
    {
        return !fErrors.isEmpty();
    }
}