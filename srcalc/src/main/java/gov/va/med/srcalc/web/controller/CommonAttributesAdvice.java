package gov.va.med.srcalc.web.controller;

import javax.inject.Inject;

import gov.va.med.srcalc.SrcalcInfo;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Adds common attributes, such as the application version, to the Spring MVC
 * Model.
 */
@ControllerAdvice
public class CommonAttributesAdvice
{
    /**
     * The name of the model attribute which contains the app version.
     * @see SrcalcInfo#getVersion()
     */
    public static final String MODEL_ATTRIBUTE_APP_VERSION = "appVersion";
    
    private final SrcalcInfo fAppInfo;
    
    /**
     * Constructs an instance.
     * @param appInfo information about the running application
     */
    @Inject
    public CommonAttributesAdvice(final SrcalcInfo appInfo)
    {
        fAppInfo = appInfo;
    }

    /**
     * Adds the attributes to the given model.
     */
    @ModelAttribute
    public void populateModel(final Model springModel)
    {
        springModel.addAttribute(
                MODEL_ATTRIBUTE_APP_VERSION, fAppInfo.getVersion());
    }
    
}
