package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;

/**
 * A mock implementation of {@link VistaPersonDao} that returns sample data.
 */
public class MockVistaPersonDao implements VistaPersonDao
{
    /**
     * Returns {@link SampleCalculations#radiologistPerson()} if the DUZ matches.
     * Otherwise, throws {@link IllegalArgumentException}.
     */
    @Override
    public VistaPerson loadVistaPerson(final String duz)
    {
        final VistaPerson radiologist = SampleCalculations.radiologistPerson();
        if (duz.equals(radiologist.getDuz()))
        {
            return radiologist;
        }
        else
        {
            throw new IllegalArgumentException("mock DAO doesn't know that DUZ");
        }
    }
    
}
