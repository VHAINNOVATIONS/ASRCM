package gov.va.med.srcalc.web;

import gov.va.med.srcalc.SrcalcInfo;

import javax.servlet.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Adds informational global attributes to the {@link ServletContext}. Adds:</p>
 * <ul>
 * <li>{@link #ATTRIBUTE_APP_INFO}: an SrcalcInfo object
 * </ul>
 */
public class AppAttributesInitializer implements ServletContextListener
{
    /**
     * The name of the SrcalcInfo attribute that we add to the ServletContext.
     * This has a '.' in it per the Java recommendation.
     */
    public static final String ATTRIBUTE_APP_INFO = "srcalc.appInfo";
    
    private static final Logger LOGGER =
            LoggerFactory.getLogger(AppAttributesInitializer.class);
    
    /**
     * Adds the attributes to the {@link ServletContext}.
     */
    @Override
    public void contextInitialized(final ServletContextEvent event)
    {
        final ServletContext servletContext = event.getServletContext();
        final SrcalcInfo srcalcInfo = WebUtils.readSrcalcInfo(servletContext);
        servletContext.setAttribute(ATTRIBUTE_APP_INFO, srcalcInfo);
        LOGGER.info("Put {} into the ServletContext: {}", ATTRIBUTE_APP_INFO, srcalcInfo);
    }
    
    /**
     * Does nothing.
     */
    @Override
    public void contextDestroyed(final ServletContextEvent event)
    {
    }
    
}
