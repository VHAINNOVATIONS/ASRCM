package gov.va.med.srcalc.ccow;

import static org.junit.Assert.*;

import javax.ws.rs.client.WebTarget;

import org.junit.Test;

public class ComponentLocationTest
{
    
    public ComponentLocationTest()
    {
    }
    
    @Test
    public final void testMakeWebTarget()
    {
        final ComponentLocation cl = new ComponentLocation(
                MockVergenceCmr.CANNED_CM_URL,
                MockVergenceCmr.CANNED_CM_PARAMETERS,
                MockVergenceCmr.CANNED_CM_SITE);
        
        final WebTarget target = cl.makeWebTarget();
        
        assertEquals(
                MockVergenceCmr.CANNED_CM_URL + "?" + MockVergenceCmr.CANNED_CM_PARAMETERS,
                target.getUri().toString());
    }
    
}
