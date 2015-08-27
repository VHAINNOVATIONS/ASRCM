package gov.va.med.srcalc.vista.vistalink;

import gov.va.med.vistalink.institution.InstitutionMapNotInitializedException;
import gov.va.med.vistalink.institution.InstitutionMappingDelegate;
import gov.va.med.vistalink.institution.InstitutionMappingNotFoundException;

/**
 * Provides VistALink utility methods. These are not srcalc-specific: VistALink could have
 * provided them itself.
 */
public class VistaLinkUtil
{
    private VistaLinkUtil()
    {
        // No construction.
    }

    /**
     * Returns true if the given division is known to VistALink, false otherwise.
     */
    public static boolean isDivisionKnown(final String division)
    {
        try
        {
            InstitutionMappingDelegate.getJndiConnectorNameForInstitution(division);
        }
        catch (InstitutionMapNotInitializedException | InstitutionMappingNotFoundException e)
        {
            return false;
        }
        
        return true;
    }
    
}
