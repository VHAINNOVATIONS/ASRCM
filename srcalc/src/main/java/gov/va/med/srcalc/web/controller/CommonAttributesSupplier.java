package gov.va.med.srcalc.web.controller;

import javax.inject.Inject;

import gov.va.med.srcalc.SrcalcInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * <p>A {@link WebRequestInterceptor} to add common attributes, such as the
 * application version, to the MVC Model for view rendering.</p>
 * 
 * <p>Adds the attributes after the request handler call and before view
 * rendering, preventing Spring from trying to bind request parameters to any
 * beans added here.</p>
 */
public class CommonAttributesSupplier implements WebRequestInterceptor
{
    /**
     * The name of the model attribute which contains the application info, a
     * {@link SrcalcInfo} instance.
     */
    public static final String MODEL_ATTRIBUTE_APP_INFO = "appInfo";
    
    private static final Logger fLogger = LoggerFactory.getLogger(CommonAttributesSupplier.class);
    
    private final SrcalcInfo fAppInfo;
    
    /**
     * Constructs an instance.
     * @param appInfo information about the running application
     */
    @Inject
    public CommonAttributesSupplier(final SrcalcInfo appInfo)
    {
        fAppInfo = appInfo;
    }

    /**
     * Does nothing.
     */
    @Override
    public void preHandle(WebRequest request) throws Exception
    {
    }
    
    /**
     * Adds the attributes to the model.
     */
    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception
    {
        // The model may not exist.
        if (model != null)
        {
            fLogger.debug("Adding the common attributes to the model.");
            model.addAttribute(MODEL_ATTRIBUTE_APP_INFO, fAppInfo);
        }
        else
        {
            fLogger.debug("Null model: not adding attributes.");
        }
    }
    
    /**
     * Does nothing.
     */
    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception
    {
    }
    
}
