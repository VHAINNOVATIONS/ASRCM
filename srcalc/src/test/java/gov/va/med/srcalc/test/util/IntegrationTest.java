package gov.va.med.srcalc.test.util;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TestRule;
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
    
    @After
    public void after()
    {
        // Per Spring recommendation, flush the Session to detect any flushing
        // errors. (The Session is not otherwise flushed to due to transaction
        // roll-back.)
        fLogger.info("Flushing Session after test execution...");
        fSessionFactory.getCurrentSession().flush();
    }
}
