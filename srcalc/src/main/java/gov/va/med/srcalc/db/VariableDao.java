package gov.va.med.srcalc.db;

import java.util.*;

import javax.inject.Inject;

import org.hibernate.*;
import org.springframework.stereotype.Repository;

import gov.va.med.srcalc.domain.model.AbstractVariable;
import gov.va.med.srcalc.domain.model.DisplayNameComparator;

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
     * Returns the persistent Variable with the given key, or null
     * if no such Variable exists.
     * 
     * @return a <i>persistent</i> object: any changes made during the current
     * transaction will automatically be persisted
     */
    public AbstractVariable getByKey(final String variableKey)
    {
        final Query q =  getCurrentSession().createQuery(
                "from AbstractVariable v where v.key = :variableKey");
        q.setString("variableKey", variableKey);
        return (AbstractVariable)q.uniqueResult();
    }
    
    /**
     * <p>Overwrites a variable in the database with the given object (matched
     * by Id).</p>
     * 
     * <p>As stated in {@link #getByKey(String) getByKey}, changes are usually
     * automatically persisted. Call this only to persist changes made after the
     * transaction in which the object was loaded.</p>
     * @param variable represents the state to persist
     */
    public void updateVariable(final AbstractVariable variable)
    {
        getCurrentSession().update(variable);
    }
}
