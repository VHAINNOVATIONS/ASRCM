package gov.va.med.srcalc.web.controller.admin;

import java.io.IOException;
import java.io.InputStreamReader;

import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.util.csv.CsvReader;
import gov.va.med.srcalc.util.csv.TabularParseResult;
import gov.va.med.srcalc.web.SrcalcUrls;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.EditModelTerm;
import gov.va.med.srcalc.web.view.admin.EditRiskModel;
import gov.va.med.srcalc.web.view.admin.EditRiskModelValidator;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for editing existing {@link RiskModel}s, including handling a bulk term
 * upload.
 */
@Controller
@RequestMapping(EditRiskModelController.BASE_URL)
public class EditRiskModelController
{
    /**
     * The URL that this controller handles.
     */
    static final String BASE_URL = "/admin/models/{riskModelId}";

    static final String ATTRIBUTE_RISK_MODEL = "riskModel";
    
    /**
     * The attribute name of the ModelTermSummaries.
     */
    static final String ATTRIBUTE_SUMMARIES = "termSummaries";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EditRiskModelController.class);
    
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
    
    /**
     * <p>Returns a ModelAndView for editing the given EditRiskModel.</p>
     * 
     * <p>NOTE: Not a request handler method.
     */
    public ModelAndView displayForm(final EditRiskModel editModel)
    {
        final ModelAndView mv = new ModelAndView( Views.EDIT_RISK_MODEL );
        mv.addObject(ATTRIBUTE_RISK_MODEL, editModel);
        mv.addObject(ATTRIBUTE_SUMMARIES, editModel.makeTermSummaries(fAdminService));
        return mv;
    }

    /**
     * Displays a page for editing the given existing RiskModel.
     * @param riskModelId the ID (surrogate key) of the RiskModel to edit
     * @throws InvalidIdentifierException if no RiskModel exists with the ID
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayForm(
            @PathVariable final int riskModelId ) throws InvalidIdentifierException
    {
        final RiskModel rm = fAdminService.getRiskModelForId(riskModelId);
        if (rm == null)
        {
            throw new InvalidIdentifierException("Unable to find RiskModel with ID " + riskModelId);
        }
        final EditRiskModel editModel = EditRiskModel.fromRiskModel(rm, fAdminService);
        
        return displayForm(editModel);
    }
    
    /**
     * Imports a new set of EditModelTerms from the given Comma-Separated Value (CSV)
     * upload.
     * @param editModel the EditRiskModel to update
     * @param termsFile the CSV content
     * @throws IOException if an I/O error occurs reading the CSV content
     */
    @RequestMapping(method = RequestMethod.POST, params="uploadTerms")
    public ModelAndView uploadTerms(
            @ModelAttribute(ATTRIBUTE_RISK_MODEL) final EditRiskModel editModel,
            final BindingResult bindingResult,
            @RequestParam("termsFile") final MultipartFile termsFile)
            throws IOException
    {
        LOGGER.debug("Processing a {}-byte term upload.", termsFile.getSize());
        
        final InputStreamReader input = new InputStreamReader(
                termsFile.getInputStream());
        final CsvReader<EditModelTerm> csvReader = new CsvReader<>(
                new TermRowTranslator());
        final TabularParseResult<EditModelTerm> parseResult = csvReader.readObjects(input);
        
        // If there were errors, display them to the user.
        if (parseResult.hasErrors())
        {
            LOGGER.debug("There were errors: {}", parseResult.getErrors());
            // Note that we _do not_ modify the editModel object, so the form will show
            // the previous set of terms.
            return displayForm(editModel)
                    .addObject("uploadErrors", parseResult.getErrors());
        }
        // Otherwise, it's valid: update the editModel object to display the new terms
        // to the user for review.
        else
        {
            editModel.getTerms().clear();
            editModel.getTerms().addAll(parseResult.getRowObjects());
            final EditRiskModelValidator validator =
                    new EditRiskModelValidator(fAdminService);
            validator.validate(editModel, bindingResult);
            return displayForm(editModel);
        }
    }
    
    /**
     * Updates the identified existing RiskModel with the given EditRiskModel object.
     * @param riskModelId the ID of the RiskModel to update
     * @param saveModel contains the edits
     * @param bindingResult
     * @throws InvalidIdentifierException if no RiskModel exists with the ID
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView saveRiskModel(
            @PathVariable final int riskModelId,
            @ModelAttribute(ATTRIBUTE_RISK_MODEL) final EditRiskModel saveModel,
            final BindingResult bindingResult)
                    throws InvalidIdentifierException
    {
        LOGGER.debug("Handling request to save RiskModel: {}", saveModel);
        
        // Spring has already bound the user input to saveModel; now validate
        //
        EditRiskModelValidator validator = new EditRiskModelValidator(fAdminService);
        validator.validate(saveModel, bindingResult);

        if( bindingResult.hasErrors() )
        {
            LOGGER.debug( "EditRiskModel has errors: {}", bindingResult);
            return displayForm(saveModel);
        }
        
        // Apply the changes to the persistent model.
        final RiskModel targetModel = fAdminService.getRiskModelForId(riskModelId);
        saveModel.applyChanges(targetModel, fAdminService);
        fAdminService.saveRiskModel(targetModel);

        // Save successful: redirect to the admin home page.
        return new ModelAndView("redirect:" + SrcalcUrls.MODEL_ADMIN_HOME);
    }

}
