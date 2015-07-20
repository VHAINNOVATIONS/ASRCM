package gov.va.med.srcalc.web.controller.admin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.util.DisplayNameConditions;
import gov.va.med.srcalc.util.csv.CsvReader;
import gov.va.med.srcalc.util.csv.TabularParseResult;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.EditModelTerm;
import gov.va.med.srcalc.web.view.admin.EditSpecialty;
//import gov.va.med.srcalc.web.view.admin.EditSpecialtyValidator;


import gov.va.med.srcalc.web.view.admin.EditSpecialtyValidator;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for editing existing {@link Specialty}s, including handling a bulk term
 * upload.
 */
@Controller
@RequestMapping(EditSpecialtyController.BASE_URL)
public class EditSpecialtyController
{
    /**
     * The URL that this controller handles.
     */
    static final String BASE_URL = "/admin/specialties/{specialtyId}";

    static final String ATTRIBUTE_SPECIALTY = "specialty";
    static final String ATTRIBUTE_MAX_DISPLAY_NAME = "maxDisplayName";
    
    private static final Logger fLogger = LoggerFactory.getLogger(EditSpecialtyController.class);
    
    private final AdminService fAdminService;
    
    /**
     * Constructs an instance.
     * @param adminService the AdminService to use for operations
     */
    @Inject
    public EditSpecialtyController(final AdminService adminService )
    {
        fAdminService = adminService;
    }
    
    /**
     * <p>Returns a ModelAndView for editing the given EditSpecialty.</p>
     * 
     * <p>NOTE: Not a request handler method.
     */
    public ModelAndView displayForm(final EditSpecialty editSpec)
    {
        final ModelAndView mv = new ModelAndView( Views.EDIT_SPECIALTY );
        
        mv.addObject(ATTRIBUTE_SPECIALTY, editSpec );
        
        // Note this the name max is different than for Models and Variables. (DisplayNameConditions.DISPLAY_NAME_MAX );
        mv.addObject(ATTRIBUTE_MAX_DISPLAY_NAME, Specialty.SPECIALTY_NAME_MAX ); 

        return mv;
    }

    /**
     * Displays a page for editing the given existing Specialty.
     * @param specialtyId the ID (surrogate key) of the Specialty to edit
     * @throws InvalidIdentifierException if no Specialty exists with the ID
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayForm(
            @PathVariable final int specialtyId ) throws InvalidIdentifierException
    {
        Specialty spec = fAdminService.getSpecialtyForId(specialtyId);
        
        if (spec == null)
        {
            throw new InvalidIdentifierException("Unable to find Specialty for ID " + specialtyId);
        }
        EditSpecialty editSpec = EditSpecialty.fromSpecialty(spec, fAdminService);
        
        return displayForm(editSpec);
    }
    
    
    /**
     * Updates the identified existing Specialty with the given EditSpecialty object.
     * @param specialtyId the ID of the Specialty to update
     * @param saveSpecialty contains the edits
     * @param bindingResult
     * @throws InvalidIdentifierException if no Specialty exists with the ID
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView saveSpecialty(
            @PathVariable final int specialtyId,
            @ModelAttribute(ATTRIBUTE_SPECIALTY) final EditSpecialty saveSpecialty,
            final BindingResult bindingResult)
                    throws InvalidIdentifierException
    {
        fLogger.debug("Handling request to save Specialty: {}", saveSpecialty.toString() );
        
        // Spring has already bound the user input to saveModel; now validate
        //
        EditSpecialtyValidator validator = new EditSpecialtyValidator(fAdminService);
        validator.validate(saveSpecialty, bindingResult);

        if( bindingResult.hasErrors() )
        {
            fLogger.debug( "EditSpecialty has errors: {}", bindingResult);
            return displayForm( saveSpecialty );
        }

        Specialty existingSpec = fAdminService.getSpecialtyForId( specialtyId );
        
        if( existingSpec == null ) // sanity check ; shouldn't happen
        {
            fLogger.error( "Unable to fetch specialty id {}", specialtyId );
            return displayForm( saveSpecialty );            
        }
        
        // Apply the changes to the target specialty and save it.
        //
        fAdminService.saveSpecialty( saveSpecialty.applyChanges( existingSpec ) );

        // Save successful: redirect to the admin home page.
        //
        return new ModelAndView("redirect:/admin");
    }

}
