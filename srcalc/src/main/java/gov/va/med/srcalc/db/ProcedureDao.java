package gov.va.med.srcalc.db;

import javax.inject.Inject;

import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableList;

import gov.va.med.srcalc.domain.model.Procedure;

/**
 * DAO for {@link Procedure} objects.
 */
@Repository
public class ProcedureDao
{
    private static final Logger fLogger = LoggerFactory.getLogger(ProcedureDao.class);
    
    private final SessionFactory fSessionFactory;
    
    @Inject // Allow arguments to be autowired.
    public ProcedureDao(final SessionFactory sessionFactory)
    {
        fSessionFactory = sessionFactory;
    }
    
    protected Session getCurrentSession()
    {
        return fSessionFactory.getCurrentSession();
    }
    
    /**
     * Loads and returns all procedures defined in the database, in ascending CPT code
     * order.
     * @return an ImmutableList
     */
    public ImmutableList<Procedure> getAllProcedures()
    {
        final Criteria criteria = getCurrentSession().createCriteria(Procedure.class);
        // This ordering may be case-sensitive, but CPT codes are normalized to upper-case
        // so that's not an issue.
        criteria.addOrder(Order.asc("cptCode"));

        @SuppressWarnings("unchecked") // trust hibernate
        final ImmutableList<Procedure> procedures = ImmutableList.copyOf(criteria.list());
        fLogger.debug("Loaded all {} procedures.", procedures.size());
        return procedures;
    }
}
