package gov.va.med.srcalc.web.controller.admin;

import gov.va.med.srcalc.domain.model.AbstractVariable;
import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.web.view.VariableSummary;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.EditRiskModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.ImmutableCollection;

/**
 * Web MVC controller for the Administration home page.
 */
@Controller
@RequestMapping(AdminHomeController.BASE_URL)
public class AdminHomeController
{
    /**
     * Specifies the base URL for this controller.
     */
    public static final String BASE_URL = "/admin";
    private static final Logger fLogger = LoggerFactory.getLogger(AdminHomeController.class);

    private final AdminService fAdminService;
    
    @Inject
    public AdminHomeController(final AdminService adminService)
    {
        fAdminService = adminService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String defaultPage(final Model model)
    {
        List<RiskModel> riskModels = new ArrayList<RiskModel>( fAdminService.getAllRiskModels() );
        Collections.sort( riskModels );
        fLogger.debug( "There are {} Risk Models in the DB.", riskModels.size());

        // Transform the List of all Variables into a List of VariableSummaries.
        final List<AbstractVariable> variables = fAdminService.getAllVariables();
        final ArrayList<VariableSummary> summaries = new ArrayList<>(variables.size());
        for (final AbstractVariable var : variables)
        {
            summaries.add(VariableSummary.fromVariable(var));
        }

        model.addAttribute("riskModels", riskModels );
        model.addAttribute("variables", summaries);
        
        return Views.MODEL_ADMIN_HOME;
    }

}
