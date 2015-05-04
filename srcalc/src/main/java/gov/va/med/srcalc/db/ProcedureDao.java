package gov.va.med.srcalc.db;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

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
    
    public List<Procedure> getActiveProcedures()
    {
        final Query query = getCurrentSession().createQuery(
                "from Procedure p where p.active = true order by p.cptCode");

        @SuppressWarnings("unchecked") // trust hibernate
        final List<Procedure> procedures = query.list();
        fLogger.debug("Loaded all {} procedures.", procedures.size());
        return procedures;
    }
}
