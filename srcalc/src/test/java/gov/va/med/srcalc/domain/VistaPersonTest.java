package gov.va.med.srcalc.domain;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Optional;

/**
 * Tests the {@link VistaPerson} class.
 */
public class VistaPersonTest
{
    @Test
    public final void testBasic()
    {
        final String DIVISION = "500";
        final String DUZ = "1";
        final String NAME = "PROGRAMMER,ONE";
        final Optional<String> PROVIDER_TYPE = Optional.of("Person Class");
        final VistaPerson person = new VistaPerson(DIVISION, DUZ, NAME, PROVIDER_TYPE);
        
        assertEquals(DIVISION, person.getStationNumber());
        assertEquals(DUZ, person.getDuz());
        assertEquals(NAME, person.getDisplayName());
        assertEquals(PROVIDER_TYPE, person.getProviderType());
        // We don't specify the format of the string, but make sure it includes
        // the division, DUZ, and name.
        assertThat(
                person.toString(),
                allOf(containsString(DIVISION), containsString(DUZ), containsString(NAME)));
    }
    
}
