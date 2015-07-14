package gov.va.med.srcalc.web.controller.admin;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;

import gov.va.med.srcalc.domain.model.Procedure;
import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.util.csv.CsvReader;
import gov.va.med.srcalc.util.csv.TabularParseResult;
import gov.va.med.srcalc.web.view.Views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.ImmutableSet;

/**
 * Web MVC controller for displaying and updating the set of clinical procedures.
 */
@Controller
@RequestMapping(EditProceduresController.BASE_URL)
public class EditProceduresController
{
    /**
     * Upon success, this controller will add a flash attribute with this name.
     */
    public static final String FLASH_ATTR_SUCCESS = "uploadSuccess";
    
    /**
     * The model attribute containing the current procedure list.
     */
    public static final String ATTRIBUTE_PROCEDURES = "procedures";

    static final String BASE_URL = "/admin/procedures";
    
    private static final Logger fLogger =
            LoggerFactory.getLogger(EditProceduresController.class);
    
    private final AdminService fAdminService;
    
    @Inject
    public EditProceduresController(final AdminService adminService)
    {
        fAdminService = adminService;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayCurrentProcedures()
    {
        return new ModelAndView(
                Views.EDIT_PROCEDURES,
                ATTRIBUTE_PROCEDURES,
                fAdminService.getAllProcedures());
                
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView uploadNewProcedures(
            @RequestParam("newProceduresFile") final MultipartFile newProceduresFile,
            final RedirectAttributes redirectAttributes)
                    throws IOException
    {
        fLogger.debug(
                "Processing a {}-byte procedure set upload.",
                newProceduresFile.getSize());

        final InputStreamReader input = new InputStreamReader(
                newProceduresFile.getInputStream());
        final CsvReader<Procedure> csvReader = new CsvReader<>(
                new ProcedureRowTranslator());
        final TabularParseResult<Procedure> result = csvReader.readObjects(input);
        
        // If there were errors, display them to the user.
        if (result.hasErrors())
        {
            fLogger.debug("There were errors: {}", result.getErrors());
            final ModelAndView mav = displayCurrentProcedures();
            mav.addObject("validationErrors", result.getErrors());
            return mav;
        }
        // Otherwise, it's valid: update the persistent store.
        else
        {
            fAdminService.replaceAllProcedures(
                    ImmutableSet.copyOf(result.getRowObjects()));

            // Return to the procedures page so that the user can inspect and verify the
            // new procedure set.
            redirectAttributes.addFlashAttribute(FLASH_ATTR_SUCCESS, true);
            return new ModelAndView("redirect:" + BASE_URL);
        }
    }
    
}
