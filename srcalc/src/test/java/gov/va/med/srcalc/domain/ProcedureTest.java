package gov.va.med.srcalc.domain;

import static org.junit.Assert.*;
import static gov.va.med.srcalc.domain.SampleObjects.sampleRepairLeftProcedure;
import gov.va.med.srcalc.domain.model.Procedure;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class ProcedureTest
{
    @Test
    public final void testToString()
    {
        final Procedure p = sampleRepairLeftProcedure();
        assertEquals("26546 - Repair left hand (10.06)", p.toString());
    }

    @Test
    public final void testLongString()
    {
        final Procedure p = sampleRepairLeftProcedure();
        assertEquals(
                "26546 - Repair left hand - you know, the thing with fingers (10.06)",
                p.getLongString());
    }
    
    @Test
    public final void testEquals()
    {
        EqualsVerifier.forClass(Procedure.class).verify();
    }
    
}
