package gov.va.med.srcalc.db;

import java.util.*;

import javax.inject.Inject;

import org.hibernate.*;
import org.springframework.stereotype.Repository;

import gov.va.med.srcalc.domain.variable.AbstractVariable;
import gov.va.med.srcalc.domain.variable.DisplayNameComparator;

/**
 * DAO for {@link AbstractVariable}s.
 */
@Repository
public class VariableDao
{
    private final SessionFactory fSessionFactory;
    
    @Inject // Allow arguments to be autowired.
    public VariableDao(final SessionFactory sessionFactory)
    {
        fSessionFactory = sessionFactory;
    }
    
    protected Session getCurrentSession()
    {
        return fSessionFactory.getCurrentSession();
    }

    /**
     * <p>Returns all of the Variables defined in the database, sorted by display
     * name (case insensitive).</p>
     */
    @SuppressWarnings("unchecked") // trust Hibernate
    public List<AbstractVariable> getAllVariables()
    {
        // As far as I can tell, HQL "order by" simply translates to a SQL
        // "ORDER BY", thus making the case-insensitivity up the database's
        // responsibility. So we do the sorting as a post-processing step in
        // Java to ensure portability.
        final Query q = getCurrentSession().createQuery(
                "from AbstractVariable v order by v.displayName");
        final List<AbstractVariable> vars = q.list();
        Collections.sort(vars, new DisplayNameComparator());
        return vars;
    }
    
    /**
     * Returns the persistent Variable with the given display name, or null
     * if no such Variable exists.
     */
    public AbstractVariable getByName(final String displayName)
    {
        final Query q =  getCurrentSession().createQuery(
                "from AbstractVariable v where v.displayName = :displayName");
        q.setString("displayName", displayName);
        return (AbstractVariable)q.uniqueResult();
    }
}
