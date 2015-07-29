package gov.va.med.srcalc.db;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import gov.va.med.srcalc.domain.calculation.SignedResult;
import gov.va.med.srcalc.util.SearchResults;

/**
 * Encapsulates search parameters for {@link SignedResult}s.
 */
public class ResultSearchParameters
{
    public static final int MAX_RESULTS = 1000;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(
            ResultSearchParameters.class);
    
    // TODO: add dates
    // TODO: add station number
    private final Set<String> fSpecialtyNames = new HashSet<>();
    private Optional<String> fCptCode = Optional.absent();

    /**
     * Returns the set of specialty names to filter the results. An empty set means no
     * specialty name filter will be applied. Default: empty set.
     * @return a mutable set
     */
    public Set<String> getSpecialtyNames()
    {
        return fSpecialtyNames;
    }

    /**
     * Returns the CPT Code to filter the results. An absent Optional means no CPT Code
     * filter will be applied. Default: absent.
     * @return never null
     */
    public Optional<String> getCptCode()
    {
        return fCptCode;
    }

    public void setCptCode(final Optional<String> cptCode)
    {
        fCptCode = cptCode;
    }
    
    SearchResults<SignedResult> doSearch(final Session session)
    {
        LOGGER.debug("Doing SignedResult search with parameters {}", this);

        final Criteria criteria = session.createCriteria(SignedResult.class);
        criteria.setReadOnly(true);  // We are loading immutable objects.
        criteria.addOrder(Order.desc("signatureTimestamp"));
        // See <http://tinyurl.com/oucuyt3>. I have no idea why this isn't Hibernate's
        // default.
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        final Criteria historicalCriteria =
                criteria.createCriteria("historicalCalculation");
        
        if (fCptCode.isPresent())
        {
            criteria.add(Restrictions.eq("cptCodeNullable", fCptCode.get()));
        }
        
        if (!fSpecialtyNames.isEmpty())
        {
            historicalCriteria.add(Restrictions.in("specialtyName", fSpecialtyNames));
        }

        // The easiest way to detect running into the maximum is to actually query for an
        // extra one and see if we get it.
        criteria.setMaxResults(MAX_RESULTS + 1);

        @SuppressWarnings("unchecked")  // trust Hibernate
        final List<SignedResult> foundItems = criteria.list();
        
        if (foundItems.size() > MAX_RESULTS)
        {
            return new SearchResults<>(
                    ImmutableList.copyOf(foundItems.subList(0, MAX_RESULTS)),
                    true);
        }
        else
        {
            return new SearchResults<>(ImmutableList.copyOf(foundItems), false);
        }
    }
    
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("cptCode", fCptCode)
                .add("specialtyName", fSpecialtyNames)
                .toString();
    }
}
