package gov.va.med.srcalc.db;

import gov.va.med.srcalc.domain.model.RiskModel;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

@Repository
public class RiskModelDao
{
    private final SessionFactory fSessionFactory;
    
    private static final Logger fLogger = LoggerFactory.getLogger(RiskModelDao.class);

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
        Query q = getCurrentSession().createQuery("from RiskModel");
        @SuppressWarnings("unchecked")
        final List<RiskModel> list = q.list();
        // sort alphabetically by name
        Collections.sort( list );
        return ImmutableList.copyOf(list);
    }
    
    /**
     * Returns the RiskModel in the database for the given Id
     * @return an RiskModel
     */
    public RiskModel getRiskModelForId( final int mid ) 
    {
    	return (RiskModel)getCurrentSession().get(RiskModel.class, mid);
    }
    
    public RiskModel saveRiskModel( final RiskModel rm )
    {
        fLogger.debug("Merging RiskModel {} into persistence context.", rm.getDisplayName() );

        return (RiskModel)getCurrentSession().merge( rm );
    }
}
