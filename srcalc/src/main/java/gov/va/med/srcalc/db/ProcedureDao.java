package gov.va.med.srcalc.db;

import java.util.Set;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcedureDao.class);
    
    /**
     * The number of Procedures that will be updated in a single batch before flushing
     * and clearing the Session.
     */
    private static final int BATCH_SIZE = 50;
    
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
        LOGGER.debug("Loaded all {} procedures.", procedures.size());
        return procedures;
    }
    
    /**
     * <p>Completely replaces all Procedures in the database with the given set.</p>
     * 
     * <p><strong>Warning:</strong> the current Hibernate Session will be cleared by this
     * method.</p>
     * @param newProcedures the new procedure set
     * @return the number of procedures that were replaced
     */
    public int replaceAllProcedures(final Set<Procedure> newProcedures)
    {
        final Session session = getCurrentSession();
        LOGGER.debug("Deleting all Procedures from the database.");
        final Query deleteQuery = session.createQuery("delete Procedure");
        final int deleteCount = deleteQuery.executeUpdate();
        LOGGER.debug("Deleted {} Procedures from the database.", deleteCount);

        LOGGER.debug("About to save {} Procedures to the database.", newProcedures.size());
        int i = 1;
        for (final Procedure p : newProcedures)
        {
            LOGGER.trace("Saving {}", p);
            session.save(p);
            // Per Hibernate recommendation, divide the saves into batches and clear the
            // Session between them.
            if (i % BATCH_SIZE == 0)
            {
                session.flush();
                session.clear();
            }
            ++i;
        }
        // Do a final flush and clear for consistency.
        session.flush();
        session.clear();
        LOGGER.debug("Done saving procedures.");
        
        return deleteCount;
    }
}
