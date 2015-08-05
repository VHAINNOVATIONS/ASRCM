package gov.va.med.srcalc.db;

import java.util.Objects;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;

import gov.va.med.srcalc.domain.calculation.HistoricalCalculation;

/**
 * <p>Encapsulates search parameters for {@link HistoricalCalculation}s.</p>
 * 
 * <p>Note: in most classes, we define the interface very tightly (using Optional objects
 * instead of nulls, etc.), but here we are pretty flexible in what we accept to allow
 * Spring to bind forms to these objects.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class HistoricalSearchParameters
{
    /**
     * The maximum number of results a search will return.
     */
    public static final int MAX_RESULTS = 1000;
    
    private Optional<LocalDate> fMinDate = Optional.absent();
    private Optional<LocalDate> fMaxDate = Optional.absent();
    
    /**
     * Returns the minimum start date to filter the results. In other words, no results
     * will be returned with a start date before this date. Null means no minimum date
     * filter. Default: null.
     * @return nullable
     */
    public LocalDate getMinDate()
    {
        return fMinDate.orNull();
    }
    
    /**
     * Sets the minimum start date to filter the results.
     * @param startDate may be null
     * @see #getMinDate()
     */
    public void setMinDate(final LocalDate startDate)
    {
        fMinDate = Optional.fromNullable(startDate);
    }
    
    /**
     * Returns the maximum start date to filter the results. In other words, no results
     * will be returned with a start date after this date. Null means no maximum date
     * filter. Default: null.
     * @return nullable
     */
    public LocalDate getMaxDate()
    {
        return fMaxDate.orNull();
    }
    
    /**
     * Sets the maximum start date to filter the results.
     * @param endDate may be null
     * @see #getMaxDate()
     */
    public void setMaxDate(final LocalDate endDate)
    {
        fMaxDate = Optional.fromNullable(endDate);
    }
    
    /**
     * Constructs a Hibernate {@link DetachedCriteria} that will perform the search. No
     * maximum is set (because the {@link DetachedCriteria} interface does not allow it):
     * that is the calling code's responsibility.
     * @return a DetachedCriteria on {@link HistoricalCalculation} objects
     */
    DetachedCriteria makeCriteria()
    {
        final DetachedCriteria criteria =
                DetachedCriteria.forClass(HistoricalCalculation.class);
        criteria.addOrder(Order.desc("startTimestamp"));
        
        if (fMinDate.isPresent())
        {
            criteria.add(Restrictions.ge(
                    "startTimestamp", fMinDate.get().toDateTimeAtStartOfDay()));
        }
        
        if (fMaxDate.isPresent())
        {
            // There is no toDateTimeAtEndOfDay(), so simulate it by using less than (not
            // equal) to start of the next day.
            criteria.add(Restrictions.lt(
                    "startTimestamp",
                    fMaxDate.get().plusDays(1).toDateTimeAtStartOfDay()));
        }
        
        return criteria;
    }
    
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("minDate", fMinDate)
                .add("maxDate", fMaxDate)
                .toString();
    }
    
    /**
     * Returns true if the given object is also an instance of HistoricalSearchParameters
     * with equal parameters, false otherwise. Note that this operation depends entirely
     * on mutable properties: take care using these objects in Sets.
     */
    @Override
    public boolean equals(Object obj)
    {
        // Performance optimization.
        if (obj == this)
        {
            return true;
        }
        
        if (obj instanceof HistoricalSearchParameters)
        {
            final HistoricalSearchParameters other = (HistoricalSearchParameters)obj;
            
            return Objects.equals(this.fMinDate, other.fMinDate) &&
                    Objects.equals(this.fMaxDate, other.fMaxDate);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(fMinDate, fMaxDate);
    }
}
