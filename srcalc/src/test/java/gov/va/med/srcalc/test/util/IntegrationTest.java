package gov.va.med.srcalc.test.util;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TestRule;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for srcalc Integration Tests. Contains common functionality.
 */
public abstract class IntegrationTest
{
    /**
     * Use the logger for the actual concrete class.
     */
    private final Logger fLogger = LoggerFactory.getLogger(getClass());
    
    @Inject
    SessionFactory fSessionFactory;

    @Rule
    public final TestRule fTestLogger = new TestNameLogger(fLogger);
    
    /**
     * Returns the current Hibernate Session.
     */
    protected Session getHibernateSession()
    {
        return fSessionFactory.getCurrentSession();
    }
    
    /**
     * <p>Simluates a new Hibernate Session by flushing and clearing the current Session.
     * </p>
     * 
     * <p>Many integration tests span multiple user requests, which would normally be
     * executed under separate transactions and therefore separate Hibernate Sessions.
     * These tests, however, run in a single transaction and therefore a single Session,
     * so this method is useful to simulate a new Session to make the tests a bit more
     * realistic.</p>
     */
    protected void simulateNewSession()
    {
        fLogger.info(
                "Flushing and clearing the Hibernate Session to simulate a new one...");
        getHibernateSession().flush();
        getHibernateSession().clear();
        fLogger.info("Session cleared.");
    }
    
    @After
    public void after()
    {
        // Per Spring recommendation, flush the Session to detect any flushing
        // errors. (The Session is not otherwise flushed to due to transaction
        // roll-back.)
        fLogger.info("Flushing Session after test execution...");
        getHibernateSession().flush();
    }
    
    protected void assertCleanSession()
    {
        fLogger.debug("Ensuring clean session...");
        assertFalse("Session should be clean", getHibernateSession().isDirty());
    }
}
