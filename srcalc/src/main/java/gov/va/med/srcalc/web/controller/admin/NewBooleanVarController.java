package gov.va.med.srcalc.web.controller.admin;

import javax.inject.Inject;

import gov.va.med.srcalc.service.AdminService;
import gov.va.med.srcalc.web.view.admin.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import com.google.common.collect.ImmutableList;

/**
 * Web MVC controller for creating a new Boolean variable.
 */
@Controller
@RequestMapping(NewBooleanVarController.BASE_URL)
public class NewBooleanVarController extends NewVarController
{
    /**
     * The base URL that this controller serves.
     */
    public static final String BASE_URL = "/admin/newBooleanVar";
    
    private static final Logger fLogger = LoggerFactory.getLogger(NewBooleanVarController.class);
    
    /**
     * Constructs an instance.
     * @param adminService the AdminService to use for operations
     * @throws NullPointerException if adminService is null
     */
    @Inject
    public NewBooleanVarController(final AdminService adminService)
    {
        super(adminService);
    }

    @Override
    public String getSaveUrl()
    {
        return BASE_URL;
    }
    
    @Override
    protected EditBooleanVariable createEditVariable()
    {
        fLogger.trace("Creating EditBooleanVariable.");
        return new EditBooleanVariable(getAdminService());
    }
    
    @Override
    protected Iterable<Validator> getValidators()
    {
        return ImmutableList.<Validator>of(new EditVariableValidator());
    }
}
