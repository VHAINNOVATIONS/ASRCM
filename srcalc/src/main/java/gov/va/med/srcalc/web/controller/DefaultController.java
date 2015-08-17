package gov.va.med.srcalc.web.controller;

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
     * This RequestMapping is hit if the user does not launch from CPRS, or attempts to
     * visit the tool without specifying a patient dfn.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String defaultPage()
    {
        return Views.LAUNCH_FROM_CPRS;
    }

    /**
     *  This RequestMapping is hit via the spring-security configuration. Notifies the user
     *  that they have timed out and that they need to launch the tool again.
     */
    @RequestMapping(value = "/sessionTimeout", method = RequestMethod.GET)
    public String sessionTimeout()
    {
        return Views.SESSION_TIMEOUT;
    }
}
