package gov.va.med.srcalc.web.controller;

import gov.va.med.srcalc.web.SrcalcUrls;
import gov.va.med.srcalc.web.view.Views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for static pages outside of the calculation workflow.
 */
@Controller
public class DefaultController
{
    /**
     * Informs the user that he/she must launch the tool from CPRS.
     */
    @RequestMapping(value = SrcalcUrls.DEFAULT_PAGE, method = RequestMethod.GET)
    public String defaultPage()
    {
        return Views.LAUNCH_FROM_CPRS;
    }
    
    /**
     * Presents a form for VistA user authentication. Hit via the spring-security
     * configuration.
     */
    @RequestMapping(value = SrcalcUrls.VISTA_LOGIN_FORM, method = RequestMethod.GET)
    public String vistaUserLoginForm()
    {
        return Views.VISTA_LOGIN_FORM;
    }

    /**
     *  Notifies the user that they have timed out and that they need to launch the tool
     *  again. Hit via the spring-security configuration.
     */
    @RequestMapping(value = SrcalcUrls.SESSION_TIMEOUT_PAGE, method = RequestMethod.GET)
    public String sessionTimeout()
    {
        return Views.SESSION_TIMEOUT;
    }
}
