package gov.va.med.srcalc.db;

import gov.va.med.srcalc.domain.model.RiskModel;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

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
     * @return an ImmutableCollection
     */
    public ImmutableCollection<RiskModel> getAllRiskModels()
    {
        @SuppressWarnings("unchecked") // trust Hibernate
        final List<RiskModel> list =
                getCurrentSession().createCriteria(RiskModel.class).list();
        return ImmutableList.copyOf(list);
    }
    
}
