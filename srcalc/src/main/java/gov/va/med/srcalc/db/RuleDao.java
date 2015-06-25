package gov.va.med.srcalc.db;

import gov.va.med.srcalc.domain.model.Rule;
import gov.va.med.srcalc.domain.model.RuleDisplayNameComparator;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * DAO for {@link Rule}s.
 */
@Repository
public class RuleDao
{
    private static final Logger fLogger = LoggerFactory.getLogger(RuleDao.class);
    
    private final SessionFactory fSessionFactory;
    
    @Inject // Allow arguments to be autowired.
    public RuleDao(final SessionFactory sessionFactory)
    {
        fSessionFactory = sessionFactory;
    }
    
    protected Session getCurrentSession()
    {
        return fSessionFactory.getCurrentSession();
    }
    
    /**
     * Returns all of the Rules defined in the database, sorted by display
     * name (case insensitive).
     */
    @SuppressWarnings("unchecked") // trust Hibernate
    public List<Rule> getAllRules()
    {
        fLogger.debug("Retrieving all Rules from the database.");
        // As far as I can tell, HQL "order by" simply translates to a SQL
        // "ORDER BY", thus making the case-insensitivity up the database's
        // responsibility. So we do the sorting as a post-processing step in
        // Java to ensure portability.
        final Query q = getCurrentSession().createQuery(
                "from Rule v order by v.displayName");
        final List<Rule> rules = q.list();
        Collections.sort(rules, new RuleDisplayNameComparator());
        
        return rules;
    }
    
    /**
     * Returns the persistent Rule with the given display name, or null
     * if no such Rule exists.
     * 
     * @return a <i>persistent</i> object: any changes made during the current
     * transaction will automatically be persisted
     */
    public Rule getByDisplayName(final String ruleName)
    {
        final Query q =  getCurrentSession().createQuery(
                "from Rule v where v.displayName = :ruleName");
        q.setString("ruleName", ruleName);
        return (Rule)q.uniqueResult();
    }
    
    /**
     * <p>Persists the given variable to the database using <a
     * href="http://en.wikibooks.org/wiki/Java_Persistence/Persisting#Merge">JPA
     * merge semantics</a>.</p>
     * 
     * <p>As stated in {@link #getByDisplayName(String) getByDisplayName}, changes are usually
     * automatically persisted. Call this only to persist a brand-new object or
     * to persist changes made after the transaction in which the object was
     * loaded.</p>
     * 
     * <p>Note that the given object is not added to the persistence context,
     * but the returned object is. If you want to modify further state, use the
     * returned object.</p>
     * 
     * @param rule represents the state to persist
     */
    public Rule mergeRule(final Rule rule)
    {
        fLogger.debug("Merging {} into persistence context.", rule);
        // Trust Hibernate with this cast here. (I wish it was generic.)
        return (Rule)getCurrentSession().merge(rule);
    }
}
