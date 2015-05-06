package gov.va.med.srcalc.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import gov.va.med.srcalc.domain.model.AbstractVariable;
import gov.va.med.srcalc.service.*;
import gov.va.med.srcalc.web.view.VariableSummary;
import gov.va.med.srcalc.web.view.Views;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Web MVC controller for the Administration home page.
 */
@Controller
@RequestMapping("/admin")
public class AdminHomeController
{
    private final AdminService fAdminService;
    
    @Inject
    public AdminHomeController(final AdminService adminService)
    {
        fAdminService = adminService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String defaultPage(final Model model)
    {
        // Transform the List of all Variables into a List of VariableSummaries.
        final List<AbstractVariable> variables = fAdminService.getAllVariables();
        final ArrayList<VariableSummary> summaries = new ArrayList<>(variables.size());
        for (final AbstractVariable var : variables)
        {
            summaries.add(VariableSummary.fromVariable(var));
        }
        model.addAttribute("variables", summaries);

        return Views.MODEL_ADMIN_HOME;
    }

}
