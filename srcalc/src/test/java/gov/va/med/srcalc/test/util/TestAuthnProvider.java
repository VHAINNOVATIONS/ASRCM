package gov.va.med.srcalc.test.util;

import gov.va.med.srcalc.domain.calculation.SampleCalculations;
import gov.va.med.srcalc.security.VistaUserDetails;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * JUnit rule that sets the current authenticated principal to {@link
 * SampleCalculations#radiologistPerson()} for each test and clears it afterwards.
 */
public class TestAuthnProvider extends TestWatcher
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TestAuthnProvider.class);
    
    @Override
    protected void starting(final Description description)
    {
        LOGGER.debug("Setting mock authentication for test {}.", description);
        final Authentication authn = new UsernamePasswordAuthenticationToken(
                new VistaUserDetails(SampleCalculations.radiologistPerson()),
                null);
        SecurityContextHolder.getContext().setAuthentication(authn);
    }
    
    @Override
    protected void finished(final Description description)
    {
        LOGGER.debug("Clearing mock authentication for test {}.", description);
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
