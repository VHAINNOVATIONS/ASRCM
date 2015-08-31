package gov.va.med.srcalc.security;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * <p>Unit tests for {@link SrcalcWebAuthnDetails}.</p>
 * 
 * <p>Using BDD-style test names here.</p>
 */
public class SrcalcWebAuthnDetailsTest
{
    @Test
    public final void shouldHaveAStringRepresentationContainingRemoteAddressAndDivision()
    {
        final String remoteAddress = "73.1.4.20";
        final String division = "442";
        final SrcalcWebAuthnDetails details =
                new SrcalcWebAuthnDetails(remoteAddress, division);
        assertThat(details.toString(), allOf(
                containsString(remoteAddress), containsString(division)));
    }
    
}
