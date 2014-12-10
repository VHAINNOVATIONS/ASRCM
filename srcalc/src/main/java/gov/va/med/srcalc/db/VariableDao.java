package gov.va.med.srcalc.db;

import java.util.List;

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

    @SuppressWarnings("unchecked") // trust Hibernate
    public List<Variable> getAllVariables()
    {
        return getCurrentSession().createQuery("from Variable v order by v.displayName").list();
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
