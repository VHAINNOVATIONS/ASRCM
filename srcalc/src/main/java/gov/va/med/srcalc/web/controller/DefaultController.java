package gov.va.med.srcalc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Simply directs requests to the context root "/" to the first controller in
 * the workflow.
 */
@Controller
public class DefaultController
{
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String defaultPage()
    {
        return "redirect:/newCalc";
    }
}
