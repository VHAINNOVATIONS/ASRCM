package gov.va.med.srcalc.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import gov.va.med.srcalc.domain.calculation.HistoricalCalculation;
import gov.va.med.srcalc.domain.calculation.HistoricalRunInfo;
import gov.va.med.srcalc.domain.calculation.SignedResult;
import gov.va.med.srcalc.util.SearchResults;

/**
 * Data Access Object (DAO) for {@link HistoricalCalculation}s and {@link SignedResult}s.
 */
@Repository
public class ResultsDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultsDao.class);
    
    private final SessionFactory fSessionFactory;
    
    /**
     * Constructs an instance.
     * @param sessionFactory the SessionFactory used to get the current session.
     */
    @Inject
    public ResultsDao(final SessionFactory sessionFactory)
    {
        fSessionFactory = sessionFactory;
    }
    
    private Session getCurrentSession()
    {
        return fSessionFactory.getCurrentSession();
    }
    
    /**
     * Persists the given new (aka transient) {@link HistoricalCalculation}. Note that
     * this method does not support updating an already-persistent object (because
     * they are immutable).
     * @param calc the object to persist
     * @throws HibernateException if the given object is already persistent
     */
    public void persistHistoricalCalc(final HistoricalCalculation calc)
    {
        LOGGER.debug("Persisting new HistoricalCalculation: {}", calc);
        getCurrentSession().persist(calc);
    }
    
    /**
     * Persists the given new (aka transient) SignedResult. Note that this method does not
     * support updating an already-persistent object (because they are immutable).
     * @param result the object to persist
     * @throws HibernateException if the given object is already persistent
     */
    public void persistSignedResult(final SignedResult result)
    {
        LOGGER.debug("Persisting new SignedResult: {}", result);
        // persist() is actually more precisely what we want here, but for some reason
        // Hibernate adds cascade=persist to properties with @MapsId (as SignedResult.
        // historicalCalculation does). If we call persist() here, it tries to persist the
        // already-persisted HistoricalCalculation and throws an Exception.
        getCurrentSession().save(result);
    }
    
    /**
     * Returns {@link SignedResult}s from the database that match the given parameters.
     * Outcomes will be eager-loaded, inputs not.
     * @param parameters
     * @return the found items, in descending signed date order
     */
    public SearchResults<SignedResult> getSignedResults(
            final ResultSearchParameters parameters)
    {
        // ResultSearchParameters does all the work for us.
        return parameters.doSearch(getCurrentSession());
    }
    
    /**
     * Merges the given HistoricalCalculations and SignedResults into HistoricalRunInfo
     * objects.
     */
    private ImmutableList<HistoricalRunInfo> makeRunInfos(
            final List<HistoricalCalculation> historicalCalcs,
            final Collection<SignedResult> results)
    {
        // 1. Create a Map{HistoricalCalculation -> SignedResult}
        final HashMap<HistoricalCalculation, SignedResult> resultMap = new HashMap<>(
                results.size());
        for (final SignedResult result : results)
        {
            resultMap.put(result.getHistoricalCalculation(), result);
        }

        // 2. For each HistoricalCalculation, create a HistoricalRunInfo, populating the
        // SignedResult if it exists in the Map.
        final ArrayList<HistoricalRunInfo> runInfos = new ArrayList<>(
                historicalCalcs.size());
        for (final HistoricalCalculation calc : historicalCalcs)
        {
            runInfos.add(new HistoricalRunInfo(
                    calc,
                    // Employ the otherwise-annoying fact that Map.get() returns null for
                    // non-existent keys.
                    Optional.fromNullable(resultMap.get(calc))));
        }
        return ImmutableList.copyOf(runInfos);
    }
    
    /**
     * Returns the historical run information results from the database by searching based
     * on the given parameters.
     * @param parameters the parameters to base the search on
     */
    public SearchResults<HistoricalRunInfo> getHistoricalRunInfos(
            final HistoricalSearchParameters parameters)
    {
        LOGGER.debug("Doing HistoricalRunInfo query with parameters {}.", parameters);
        final Profiler profiler = new Profiler("historicalRunInfo query");
        profiler.setLogger(LOGGER);

        profiler.start("HistoricalCalculation search");
        final DetachedCriteria baseHistoricalCriteria = parameters.makeCriteria();
        final Session session = getCurrentSession();
        
        /* First get the matching HistoricalCalculation objects. */
        final Criteria historicalCriteria = baseHistoricalCriteria
                .getExecutableCriteria(session)
                // The easiest way to detect running into the maximum is to actually query
                // for an extra one and see if we get it.
                .setMaxResults(HistoricalSearchParameters.MAX_RESULTS + 1);
        historicalCriteria.addOrder(Order.desc("startTimestamp"));
        LOGGER.trace("Doing HistoricalCalculation search with Criteria {}.",
                historicalCriteria);
        @SuppressWarnings("unchecked") // trust Hibernate
        final List<HistoricalCalculation> historicals = historicalCriteria.list();
        
        /*
         * Now get any associated SignedResult objects. (If there are n
         * HistoricalCalculations, there will be <= n of these.)
         */
        profiler.start("SignedResult search");
        // Just select the Id from the HistoricalCalculations for the in() subquery.
        // Hibernate requires this though it could probably do it automatically. (See
        // HHH-993.)
        baseHistoricalCriteria.setProjection(Projections.id());
        final Criteria resultCriteria = 
                session.createCriteria(SignedResult.class)
                .add(Property.forName("historicalCalculation").in(baseHistoricalCriteria));
        LOGGER.trace("Doing SignedResult search with Criteria {}.", resultCriteria);
        @SuppressWarnings("unchecked") // trust Hibernate here, too
        final List<SignedResult> results = resultCriteria.list();
        
        /* Now merge the two into HistoricalRunInfo objects. */
        profiler.start("makeRunInfos");
        final ImmutableList<HistoricalRunInfo> runInfos = makeRunInfos(historicals, results);
        profiler.stop().log();

        return SearchResults.fromList(runInfos, HistoricalSearchParameters.MAX_RESULTS);
    }
}
