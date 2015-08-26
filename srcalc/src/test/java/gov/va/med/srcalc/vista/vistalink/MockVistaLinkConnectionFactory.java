package gov.va.med.srcalc.vista.vistalink;

import javax.resource.cci.ConnectionSpec;

import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionFactory;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpec;
import gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnectionFactory;

/**
 * <p>A simple mock VistaLinkConnectionFactory that returns {@link
 * MockVistaLinkConnection}s. We use a real class for this instead of Mockito because it
 * is actually easier.</p>
 * 
 * <p><strong>Warning:</strong> the only supported method is {@link
 * #getConnection(ConnectionSpec)}. Calling any other method has undefined consequences,
 * up to and including termination of employment.</p>
 */
public class MockVistaLinkConnectionFactory extends VistaLinkConnectionFactory
{
    /**
     * Change this when changing the class.
     */
    private static final long serialVersionUID = 1L;

    public MockVistaLinkConnectionFactory()
    {
        // These values shouldn't be used.
        super(
                new VistaLinkManagedConnectionFactory(),
                null);
    }
    
    /**
     * Returns a {@link MockVistaLinkConnection} for the given ConnectionSpec.
     * @param connectionSpec must be a {@link VistaLinkConnectionSpec}
     */
    @Override
    public MockVistaLinkConnection getConnection(final ConnectionSpec connectionSpec)
    {
        return new MockVistaLinkConnection((VistaLinkConnectionSpec)connectionSpec);
    }
}
