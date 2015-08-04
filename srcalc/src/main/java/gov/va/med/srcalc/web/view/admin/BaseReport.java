package gov.va.med.srcalc.web.view.admin;

import java.util.Objects;

import gov.va.med.srcalc.util.SearchResults;

import org.joda.time.LocalDate;

import com.google.common.base.MoreObjects;

/**
 * <p>Encapsulates basic data for a generic report.</p>
 *
 * @param <P> the type of object specifying parameters for generating the report
 * @param <T> the type of objects to display on the report
 */
public class BaseReport<P, T>
{
    private final LocalDate fGenerationDate;
    private final P fParameters;
    private final SearchResults<T> fResults;
    
    /**
     * Constructs an instance with the given properties and a generationDate of now.
     * @param parameters See {@link #getParameters()}.
     * @param results See {@link #getResults()}.
     * @throws NullPointerException if any argument is null
     */
    public BaseReport(
            final P parameters,
            final SearchResults<T> results)
    {
        fGenerationDate = LocalDate.now();
        fParameters = Objects.requireNonNull(parameters);
        fResults = Objects.requireNonNull(results);
    }
    
    /**
     * Returns the date this report was generated.
     */
    public LocalDate getGenerationDate()
    {
        return fGenerationDate;
    }
    
    /**
     * Returns the search parameters used to generate this report. The returned object
     * is mutable, but please don't modify it.
     */
    public P getParameters()
    {
        return fParameters;
    }
    
    /**
     * Returns the actual search results.
     */
    public SearchResults<T> getResults()
    {
        return fResults;
    }
    
    /**
     * Returns a string containing the generation date, parameters, and results.
     */
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("generationDate", fGenerationDate)
                .add("parameters", fParameters)
                .add("results", fResults)
                .toString();
    }
    
    /**
     * <p>Returns true if the given object is also a BaseReport with the same
     * properties.</p>
     * 
     * <p>Note that this operation relies on parameters object, which may be mutable: take
     * care using these objects in a Set or Map.</p>
     * 
     * <p>This method is final. (See Effective Java, Item 8, for why.) Subclasses will
     * not be able to change equals() semantics if they add attributes.</p>
     */
    @Override
    public final boolean equals(final Object obj)
    {
        // Performance optimization.
        if (obj == this)
        {
            return true;
        }
        
        if (obj instanceof BaseReport)
        {
            final BaseReport<?, ?> other = (BaseReport<?, ?>)obj;

            return Objects.equals(this.fGenerationDate, other.fGenerationDate) &&
                    Objects.equals(this.fParameters, other.fParameters) &&
                    Objects.equals(this.fResults, other.fResults);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public final int hashCode()
    {
        return Objects.hash(fGenerationDate, fParameters, fResults);
    }
}
