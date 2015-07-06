package gov.va.med.srcalc.web.controller.admin;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;

import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.web.controller.admin.ProcedureCsvReader.ParseResult;
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

        final InputStreamReader reader = new InputStreamReader(
                newProceduresFile.getInputStream());
        final ParseResult result = new ProcedureCsvReader().readProcedures(reader);
        
        // If all lines were valid, update the persistent store.
        if (result.getErrors().isEmpty())
        {
            fAdminService.replaceAllProcedures(
                    ImmutableSet.copyOf(result.getProcedures()));

            // Return to the procedures page so that the user can inspect and verify the
            // new procedure set.
            redirectAttributes.addFlashAttribute(FLASH_ATTR_SUCCESS, true);
            return new ModelAndView("redirect:" + BASE_URL);
        }
        // Otherwise, display the errors to the user.
        else
        {
            fLogger.debug("There were errors: {}", result.getErrors());
            final ModelAndView mav = displayCurrentProcedures();
            mav.addObject("validationErrors", result.getErrors());
            return mav;
        }

    }
    
}