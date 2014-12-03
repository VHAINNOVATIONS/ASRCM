package gov.va.med.srcalc.test.util;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;

/**
 * A JUnit {@link TestWatcher} that simply logs the name of each test as it is
 * starting.
 */
public class TestNameLogger extends TestWatcher
{
    private final Logger fLogger;
    
    public TestNameLogger(final Logger logger)
    {
        fLogger = logger;
    }
    
    @Override
    protected void starting(Description description)
    {
        fLogger.info("Starting test {}...", description.getDisplayName());
    }
    
}
