package gov.va.med.srcalc.web.controller;

import gov.va.med.srcalc.domain.model.Procedure;
import gov.va.med.srcalc.service.ModelInspectionService;

import java.util.*;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.google.common.collect.ImmutableList;

/**
 * A Web MVC Controller to simply return reference data to users, typically
 * as JSON.
 */
@Controller
// All requests are mapped under /refdata for caching. See web.xml
@RequestMapping("/refdata")
public class ReferenceDataController
{
    private final ModelInspectionService fService;
    
    @Inject
    public ReferenceDataController(final ModelInspectionService modelService)
    {
        fService = modelService;
    }

    /**
     * Returns a list of active procedures, sorted by CPT code.
     * @return the list of active procedures
     */
    @RequestMapping(
            value = "/procedures",
            method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody  // don't use a view, use the return value as the content
    public List<Object> getProcedures()
    {
        final ImmutableList<Procedure> procedures = fService.getAllProcedures();
        
        // Transform each procedure into just what we want in the JSON.
        final List<Object> returnList = new ArrayList<>(procedures.size());
        for (final Procedure p : procedures)
        {
            final HashMap<String, String> jsonProcedure = new HashMap<>();
            jsonProcedure.put("cptCode", p.getCptCode());
            jsonProcedure.put("longDescription", p.getLongDescription());
            jsonProcedure.put("shortDescription", p.getShortDescription());
            // Use Float.toString() to send RVU as a string.
            jsonProcedure.put("rvu", Float.toString(p.getRvu()));
            returnList.add(jsonProcedure);
        }
        
        return returnList;
    }
    
}
