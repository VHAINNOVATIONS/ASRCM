package gov.va.med.srcalc.db;

import gov.va.med.srcalc.domain.model.RiskModel;

import java.util.Collection;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class RiskModelDao
{
    private final SessionFactory fSessionFactory;
    
    @Inject // Allow arguments to be autowired.
    public RiskModelDao(final SessionFactory sessionFactory)
    {
        fSessionFactory = sessionFactory;
    }
    
    protected Session getCurrentSession()
    {
        return fSessionFactory.getCurrentSession();
    }
    
    /**
     * Returns all RiskModels in the database, in arbitrary order.
     */
    @SuppressWarnings("unchecked") // trust Hibernate
    public Collection<RiskModel> getAllRiskModels()
    {
        return getCurrentSession().createQuery("from RiskModel").list();
    }
    
}
