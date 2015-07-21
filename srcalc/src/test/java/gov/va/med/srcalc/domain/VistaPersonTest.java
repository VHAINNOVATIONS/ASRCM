package gov.va.med.srcalc.domain;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

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
        final ImmutableSet<String> PROVIDER_TYPES = ImmutableSet.of("Person Class");
        final VistaPerson person = new VistaPerson(DIVISION, DUZ, NAME, PROVIDER_TYPES);
        
        assertEquals(DIVISION, person.getStationNumber());
        assertEquals(DUZ, person.getDuz());
        assertEquals(NAME, person.getDisplayName());
        assertEquals(PROVIDER_TYPES, person.getProviderTypes());
        // We don't specify the format of the string, but make sure it includes
        // the division, DUZ, and name.
        assertThat(
                person.toString(),
                allOf(containsString(DIVISION), containsString(DUZ), containsString(NAME)));
    }
    
}
