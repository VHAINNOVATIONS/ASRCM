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
    @RequestMapping(value = "/sessionTimeout", method = RequestMethod.GET)
    public String defaultPage()
    {
        return Views.SESSION_TIMEOUT;
    }
}
