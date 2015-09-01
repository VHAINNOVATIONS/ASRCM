package gov.va.med.srcalc.security;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * Unit tests for {@link SrcalcWebAuthnDetailsSource}. Using BDD-style tests.
 */
public class SrcalcWebAuthnDetailsSourceTest
{
    @Test
    public final void shouldCreateAuthnDetailsFromServletRequest()
    {
        final String remoteAddr = "172.108.3.10";
        final String division = "618";
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(remoteAddr);
        request.setParameter(SrcalcWebAuthnDetailsSource.DIVISION_PARAM, division);
        
        /* Behavior & Verification */
        final SrcalcWebAuthnDetails authDetails =
                new SrcalcWebAuthnDetailsSource().buildDetails(request);
        assertEquals(remoteAddr, authDetails.getRemoteAddress());
        assertEquals(division, authDetails.getDivision());
    }
    
}
