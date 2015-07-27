package gov.va.med.srcalc.db;

import javax.inject.Inject;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import gov.va.med.srcalc.domain.calculation.HistoricalCalculation;
import gov.va.med.srcalc.domain.calculation.SignedResult;

/**
 * Data Access Object (DAO) for {@link HistoricalCalculation}s and {@link SignedResult}s.
 */
@Repository
public class ResultsDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultsDao.class);
    
    private final SessionFactory fSessionFactory;
    
    @Inject
    public ResultsDao(final SessionFactory sessionFactory)
    {
        fSessionFactory = sessionFactory;
    }
    
    protected Session getCurrentSession()
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
        getCurrentSession().persist(result);
    }
    
}
