package gov.va.med.srcalc.security;

import org.junit.Test;

/**
 * Unit tests for {@link SecurityUtil}.
 */
public class SecurityUtilTest
{
    /**
     * Tests that {@link SecurityUtil#getCurrentPrincipal()} throws an {@link
     * IllegalStateException} if there is not current principal.
     */
    @Test(expected = IllegalStateException.class)
    public final void testNoCurrentPrincipal()
    {
        SecurityUtil.getCurrentPrincipal();
    }
    
}
