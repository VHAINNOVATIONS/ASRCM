package gov.va.med.srcalc.web.controller.admin;

import javax.inject.Inject;

import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.web.view.admin.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.ImmutableList;

/**
 * Web MVC controller for creating a new Multi-Select variable.
 */
@Controller
@RequestMapping(NewMultiSelectVarController.BASE_URL)
public class NewMultiSelectVarController extends NewVarController
{
    public static final String BASE_URL = "/admin/newMultiSelectVar";
    
    private static final Logger fLogger = LoggerFactory.getLogger(NewMultiSelectVarController.class);

    /**
     * Constructs an instance.
     * @param adminService the AdminService to use for operations
     */
    @Inject
    public NewMultiSelectVarController(final AdminService adminService)
    {
        super(adminService);
    }
    
    @Override
    public String getSaveUrl()
    {
        return BASE_URL;
    }
    
    @Override
    protected EditVar createEditVar()
    {
        fLogger.trace("Creating EditMultiSelectVar.");
        return new EditMultiSelectVar(getAdminService());
    }
    
    @Override
    protected Iterable<Validator> getValidators()
    {
        return ImmutableList.<Validator>of(new EditMultiSelectVarValidator());
    }
    
}
