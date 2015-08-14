package gov.va.med.srcalc.db;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;

import gov.va.med.srcalc.domain.calculation.SignedResult;
import gov.va.med.srcalc.util.SearchResults;

/**
 * <p>Encapsulates search parameters for {@link SignedResult}s.</p>
 * 
 * <p>Note: in most classes, we define the interface very tightly (using Optional objects
 * instead of nulls, etc.), but here we are pretty flexible in what we accept to allow
 * Spring to bind forms to these objects.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class ResultSearchParameters
{
    /**
     * The maximum results that can be returned.
     */
    public static final int MAX_RESULTS = 1000;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(
            ResultSearchParameters.class);
    
    private Optional<LocalDate> fMinDate = Optional.absent();
    private Optional<LocalDate> fMaxDate = Optional.absent();
    private Optional<String> fStationNumber = Optional.absent();
    private ImmutableSet<String> fSpecialtyNames = ImmutableSet.of();
    private Optional<String> fCptCode = Optional.absent();
    
    /**
     * Returns the minimum signature date to filter the results. In other words, no
     * results will be returned with a signature date before this date. Null means no
     * minimum date filter. Default: null.
     * @return nullable
     */
    public LocalDate getMinDate()
    {
        return fMinDate.orNull();
    }
    
    /**
     * Sets the minimum signature date to filter the results.
     * @param startDate may be null
     * @see #getMinDate()
     */
    public void setMinDate(final LocalDate startDate)
    {
        fMinDate = Optional.fromNullable(startDate);
    }
    
    /**
     * Returns the maximum signature date to filter the results. In other words, no
     * results will be returned with a signature date after this date. Null means no
     * maximum date filter. Default: null.
     * @return nullable
     */
    public LocalDate getMaxDate()
    {
        return fMaxDate.orNull();
    }
    
    /**
     * Sets the maximum signature date to filter the results.
     * @param endDate may be null
     * @see #getMaxDate()
     */
    public void setMaxDate(final LocalDate endDate)
    {
        fMaxDate = Optional.fromNullable(endDate);
    }
    
    /**
     * Returns the station number to filter the results. Null means no station number
     * filter. Default: null.
     * @return nullable
     */
    public String getStationNumber()
    {
        return fStationNumber.orNull();
    }
    
    /**
     * Sets the station number to filter the results. Null or empty string means no
     * station number filter will be applied.
     */
    public void setStationNumber(final String stationNumber)
    {
        fStationNumber = Optional.fromNullable(Strings.emptyToNull(stationNumber));
    }

    /**
     * Returns the set of specialty names to filter the results. An empty set means no
     * specialty name filter will be applied. Default: empty set.
     * @return an immutable set
     */
    public Set<String> getSpecialtyNames()
    {
        return fSpecialtyNames;
    }
    
    /**
     * Replaces the set of specialty names to filter the results.
     * @param specialtyNames may be null to indicate an empty set
     * @see #getSpecialtyNames()
     */
    public void setSpecialtyNames(final Set<String> specialtyNames)
    {
        if (specialtyNames == null)
        {
            fSpecialtyNames = ImmutableSet.of();
        }
        else
        {
            fSpecialtyNames = ImmutableSet.copyOf(specialtyNames);
        }
    }

    /**
     * Returns the CPT Code to filter the results. Null means no CPT Code filter will be
     * applied. Default: null.
     * @return never null
     */
    public String getCptCode()
    {
        return fCptCode.orNull();
    }

    /**
     * Sets the CPT Code to filter the results. Null or empty String means no CPT Code
     * filter will be applied.
     */
    public void setCptCode(final String cptCode)
    {
        fCptCode = Optional.fromNullable(Strings.emptyToNull(cptCode));
    }
    
    /**
     * Perform the search based off of the parameters contained in the calling instance.
     * @param session the current Hibernate session
     * @return the results of the search
     */
    SearchResults<SignedResult> doSearch(final Session session)
    {
        LOGGER.debug("Doing SignedResult search with parameters {}", this);

        final Criteria criteria = session.createCriteria(SignedResult.class);
        criteria.setReadOnly(true);  // We are loading immutable objects.
        criteria.setFetchMode("outcomes", FetchMode.JOIN);
        criteria.addOrder(Order.desc("signatureTimestamp"));
        // See <http://tinyurl.com/oucuyt3>. I have no idea why this isn't Hibernate's
        // default.
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        final Criteria historicalCriteria =
                criteria.createCriteria("historicalCalculation");
        
        if (fMinDate.isPresent())
        {
            criteria.add(Restrictions.ge(
                    "signatureTimestamp", fMinDate.get().toDateTimeAtStartOfDay()));
        }
        
        if (fMaxDate.isPresent())
        {
            // There is no toDateTimeAtEndOfDay(), so simulate it by using less than (not
            // equal) to start of the next day.
            criteria.add(Restrictions.lt(
                    "signatureTimestamp",
                    fMaxDate.get().plusDays(1).toDateTimeAtStartOfDay()));
        }
        
        if (fCptCode.isPresent())
        {
            criteria.add(Restrictions.eq("cptCodeNullable", fCptCode.get()));
        }
        
        if (!fSpecialtyNames.isEmpty())
        {
            historicalCriteria.add(Restrictions.in("specialtyName", fSpecialtyNames));
        }
        
        if (fStationNumber.isPresent())
        {
            historicalCriteria.add(Restrictions.eq("userStation", fStationNumber.get()));
        }

        // The easiest way to detect running into the maximum is to actually query for an
        // extra one and see if we get it.
        criteria.setMaxResults(MAX_RESULTS + 1);
        
        LOGGER.trace("Searching with Criteria {}", criteria);

        @SuppressWarnings("unchecked")  // trust Hibernate
        final List<SignedResult> foundItems = criteria.list();
        
        return SearchResults.fromList(foundItems, MAX_RESULTS);
    }
    
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("minDate", fMinDate)
                .add("maxDate", fMaxDate)
                .add("stationNumber", fStationNumber)
                .add("cptCode", fCptCode)
                .add("specialtyNames", fSpecialtyNames)
                .toString();
    }
    
    /**
     * Returns true if the given Object is also an instance of ResultSearchParameters
     * with equal parameters, false otherwise. Note that this operation depends entirely
     * on mutable properties: take care using these objects in Sets.
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof ResultSearchParameters)
        {
            final ResultSearchParameters other = (ResultSearchParameters)obj;
            
            return Objects.equals(this.fMinDate, other.fMinDate) &&
                    Objects.equals(this.fMaxDate, other.fMaxDate) &&
                    Objects.equals(this.fStationNumber, other.fStationNumber) &&
                    Objects.equals(this.fCptCode, other.fCptCode) &&
                    Objects.equals(this.fSpecialtyNames, other.fSpecialtyNames);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(
                fMinDate, fMaxDate, fStationNumber, fCptCode, fSpecialtyNames);
    }
}
