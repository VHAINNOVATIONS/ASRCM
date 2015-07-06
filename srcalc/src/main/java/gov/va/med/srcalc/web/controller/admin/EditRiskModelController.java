package gov.va.med.srcalc.web.controller.admin;

import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.EditRiskModel;
import gov.va.med.srcalc.web.view.admin.EditRiskModelValidator;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/models/{riskModelId}")
public class EditRiskModelController
{
    private static final String ATTRIBUTE_RISK_MODEL = "riskModel";
    
    private static final Logger fLogger = LoggerFactory.getLogger(EditRiskModelController.class);
    
    private final AdminService fAdminService;
    
    /**
     * Constructs an instance.
     * @param adminService the AdminService to use for operations
     */
    @Inject
    public EditRiskModelController(final AdminService adminService )
    {
        fAdminService = adminService;
    }
    
    @ModelAttribute(ATTRIBUTE_RISK_MODEL)
    public EditRiskModel createEditRiskModel(
            @PathVariable final int riskModelId )
            throws InvalidIdentifierException
    {
        RiskModel rm = fAdminService.getRiskModelForId(riskModelId);
        
        if (rm == null)
        {
            throw new InvalidIdentifierException("Unable to find RiskModel with ID " + riskModelId);
        }
        EditRiskModel editModel = EditRiskModel.fromRiskModel( rm, fAdminService );

        return editModel;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayForm( )
            throws InvalidIdentifierException
    {
        final ModelAndView mv = new ModelAndView( Views.EDIT_RISK_MODEL );        
        return mv;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView saveRiskModel(
            @ModelAttribute(ATTRIBUTE_RISK_MODEL) final EditRiskModel saveModel,
            final BindingResult bindingResult)
                    throws InvalidIdentifierException
    {
        // Spring has already bound the user input to saveModel; now validate
        //
        EditRiskModelValidator validator = new EditRiskModelValidator();
        validator.validate(saveModel, bindingResult);

        if( bindingResult.hasErrors() )
        {
            fLogger.debug( "EditRiskModel has errors: {}", bindingResult);
            return displayForm( );
        }
        
        // Apply the changes to the persistent model.
        fAdminService.saveRiskModel( saveModel.applyChanges() );

        // Using the POST-redirect-GET pattern.
        return new ModelAndView("redirect:/admin");
    }

}
