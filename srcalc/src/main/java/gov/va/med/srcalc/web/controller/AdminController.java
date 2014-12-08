package gov.va.med.srcalc.web.controller;

import gov.va.med.srcalc.web.view.Tile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * TODO
 */
@Controller
public class AdminController
{
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String defaultPage()
    {
        return Tile.ADMIN_HOME;
    }
}
