package gov.va.med.srcalc.web.controller.admin;

import java.util.ArrayList;

import javax.inject.Inject;

import gov.va.med.srcalc.db.ResultSearchParameters;
import gov.va.med.srcalc.domain.calculation.SignedResult;
import gov.va.med.srcalc.service.ModelInspectionService;
import gov.va.med.srcalc.service.ReportService;
import gov.va.med.srcalc.util.SearchResults;
import gov.va.med.srcalc.web.SrcalcUrls;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.SummaryReport;
import gov.va.med.srcalc.web.view.admin.SummaryReportRow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for generating a Summary Report.
 */
@Controller
@RequestMapping(SrcalcUrls.SUMMARY_REPORT)
public class SummaryReportController
{
    static final String ATTRIBUTE_REPORT_PARAMETERS = "reportParameters";
    static final String ATTRIBUTE_SPECIALTY_LIST = "specialtyList";
    static final String ATTRIBUTE_PROCEDURE_LIST = "procedureList";
    
    static final String ATTRIBUTE_REPORT = "report";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SummaryReportController.class);
    
    private final ReportService fReportService;
    private final ModelInspectionService fModelService;
    
    @Inject
    public SummaryReportController(
            final ReportService reportService, final ModelInspectionService modelService)
    {
        fReportService = reportService;
        fModelService = modelService;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayForm(
            @ModelAttribute(ATTRIBUTE_REPORT_PARAMETERS) final ResultSearchParameters params)
    {
        // Note: params is already in the Model via @ModelAttribute.
        return new ModelAndView(Views.SUMMARY_REPORT_FORM)
            // Even though we just need the names, we put in the whole Specialty objects
            // in order to save the transformation step.
            .addObject(ATTRIBUTE_SPECIALTY_LIST, fModelService.getAllSpecialties())
            .addObject(ATTRIBUTE_PROCEDURE_LIST, fModelService.getEligibleProcedures());
    }
    
    private SummaryReport makeReport(final ResultSearchParameters params)
    {
        final SearchResults<SignedResult> results = 
                fReportService.getSignedResults(params);
        
        final ArrayList<SummaryReportRow> rows = new ArrayList<>();
        for (final SignedResult result : results.getFoundItems())
        {
            // Note: adds multiple rows
            rows.addAll(SummaryReportRow.fromSignedResult(result));
        }

        return new SummaryReport(
                params, new SearchResults<>(rows, results.isTruncated()));
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView displayReport(
            @ModelAttribute(ATTRIBUTE_REPORT_PARAMETERS) final ResultSearchParameters params,
            final BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            LOGGER.debug("Re-showing form due to errors: {}", bindingResult);
            return displayForm(params);
        }
        else
        {
            return new ModelAndView(Views.SUMMARY_REPORT_RESULTS)
                .addObject(ATTRIBUTE_REPORT, makeReport(params));
        }
    }
    
}
