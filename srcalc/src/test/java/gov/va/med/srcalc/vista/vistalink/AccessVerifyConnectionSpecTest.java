package gov.va.med.srcalc.vista.vistalink;

import static org.junit.Assert.assertEquals;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import com.google.common.hash.HashCode;

/**
 * Unit tests for {@link AccessVerifyConnectionSpec}.
 */
public class AccessVerifyConnectionSpecTest
{
    @Test
    public final void testEqualsObject()
    {
        EqualsVerifier.forClass(AccessVerifyConnectionSpec.class)
                // EqualsVerifier can't construct HashCodes.
                .withPrefabValues(HashCode.class, HashCode.fromInt(123), HashCode.fromInt(456))
                // Divison is mutable: can't fix that.
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
    
    @Test
    public final void testBasic()
    {
        final String division = "512";
        final String accessCode = "bob";
        final String clientIp = "10.0.2.4";
        final AccessVerifyConnectionSpec cs = new AccessVerifyConnectionSpec(
                division, accessCode, "robert", clientIp);
        
        assertEquals(division, cs.getDivision());
        assertEquals(accessCode, cs.getAccessCode());
        assertEquals(clientIp, cs.getClientIp());
        assertEquals(AccessVerifyConnectionSpec.SECURITY_TYPE, cs.getSecurityType());
    }
}
