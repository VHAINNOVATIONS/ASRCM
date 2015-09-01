package gov.va.med.srcalc.vista.vistalink;

import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import gov.va.med.srcalc.ConfigurationException;
import gov.va.med.vistalink.adapter.spi.ConfigurationReader;
import gov.va.med.vistalink.institution.*;

/**
 * Provides VistALink utility methods. These are not srcalc-specific: VistALink could have
 * provided them itself.
 */
public class VistaLinkUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VistaLinkUtil.class);
    
    private static final IPrimaryStationRules STATION_RULES;
    
    // Eagerly get IPrimaryStationRules implementation to find configuration issues early.
    static
    {
        try
        {
            STATION_RULES = PrimaryStationRulesFactory.getPrimaryStationRulesImplementation();
        }
        catch (final ReflectiveOperationException e)
        {
            // getPrimaryStationRulesImplementation() does not document its exceptions,
            // so at least be as specific as we can.
            throw new ConfigurationException(
                    "Could not get configured IPrimaryStationRules implementation.", e);
        }
    }
    
    private VistaLinkUtil()
    {
        // No construction.
    }
    
    /**
     * Tries to load the VistALink ConnectionFactory for the given division by explicitly
     * reading the configuration file.
     */
    private static void tryForceLoad(final String division)
    {
        // The below technique is tighter coupling with VistALink than I would like, but
        // I couldn't find a better way. - David Tombs, 31-Aug-2015
        try
        {
            final String station = STATION_RULES.getPrimaryStationLookupString(division);
            final String configuredJndiName =
                    ConfigurationReader.getJndiNameForStationNumber(station);
            LOGGER.trace(
                    "Got JNDI name {} for division {} from configuration",
                    configuredJndiName, division);
            if (configuredJndiName != null)
            {
                final InitialContext ic = new InitialContext();
                ic.lookup(configuredJndiName);
            }
        }
        catch (final Exception ex)
        {
            LOGGER.info(
                    "Swallowing Exception while trying to force-load division {}",
                    division,
                    ex);
        }
    }
    
    /**
     * Returns the JNDI name of the VistALink ConnectionFactory for the given division,
     * if known.
     */
    public static Optional<String> getJndiNameForDivision(final String division)
    {
        try
        {
            return Optional.of(
                    InstitutionMappingDelegate.getJndiConnectorNameForInstitution(division));
        }
        catch (InstitutionMapNotInitializedException |
                InstitutionMappingNotFoundException ex)
        {
            /*
             * This isn't pretty: VistALink relies on the connectors being eagerly-loaded
             * like WebLogic does, but it isn't standard across application servers. The
             * below hack supports other application servers (e.g., GlassFish and probably
             * Websphere).
             */
            tryForceLoad(division);

            try
            {
                return Optional.of(
                        InstitutionMappingDelegate.getJndiConnectorNameForInstitution(division));
            }
            catch (InstitutionMapNotInitializedException |
                    InstitutionMappingNotFoundException e)
            {
                LOGGER.debug("Unknown division {}", division, e);
                return Optional.absent();
            }
        }
    }

    /**
     * Returns true if the given division is known to VistALink, false otherwise.
     */
    public static boolean isDivisionKnown(final String division)
    {
        return getJndiNameForDivision(division).isPresent();
    }
    
}
