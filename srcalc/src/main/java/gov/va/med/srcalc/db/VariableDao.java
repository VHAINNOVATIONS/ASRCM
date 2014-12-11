package gov.va.med.srcalc.db;

import java.util.*;

import javax.inject.Inject;

import org.hibernate.*;
import org.springframework.stereotype.Repository;

import gov.va.med.srcalc.domain.variable.Variable;

/**
 * DAO for {@link Variable}s.
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
    public List<Variable> getAllVariables()
    {
        // As far as I can tell, HQL "order by" simply translates to a SQL
        // "ORDER BY", thus making the case-insensitivity up the database's
        // responsibility. So we do the sorting as a post-processing step in
        // Java to ensure portability.
        final Query q = getCurrentSession().createQuery(
                "from Variable v order by v.displayName");
        final List<Variable> vars = q.list();
        Collections.sort(vars, new Comparator<Variable>()
        {
            @Override
            public int compare(final Variable v1, final Variable v2)
            {
                return v1.getDisplayName().compareToIgnoreCase(v2.getDisplayName());
            }
        });
        return vars;
    }
    
    /**
     * Returns the persistent Variable with the given display name, or null
     * if no such Variable exists.
     */
    public Variable getByName(final String displayName)
    {
        final Query q =  getCurrentSession().createQuery(
                "from Variable v where v.displayName = :displayName");
        q.setString("displayName", displayName);
        return (Variable)q.uniqueResult();
    }
}
