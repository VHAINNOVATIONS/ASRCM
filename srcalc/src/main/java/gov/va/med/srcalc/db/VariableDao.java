package gov.va.med.srcalc.db;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
}
