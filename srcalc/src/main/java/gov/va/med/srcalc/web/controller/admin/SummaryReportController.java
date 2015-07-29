package gov.va.med.srcalc.web.controller.admin;

import java.util.ArrayList;

import javax.inject.Inject;

import gov.va.med.srcalc.db.ResultSearchParameters;
import gov.va.med.srcalc.domain.calculation.SignedResult;
import gov.va.med.srcalc.service.ReportService;
import gov.va.med.srcalc.util.SearchResults;
import gov.va.med.srcalc.web.SrcalcUrls;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.SummaryReport;
import gov.va.med.srcalc.web.view.admin.SummaryReportRow;

import org.springframework.stereotype.Controller;
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
    
    static final String ATTRIBUTE_REPORT = "report";
    
    private final ReportService fReportService;
    
    @Inject
    public SummaryReportController(final ReportService reportService)
    {
        fReportService = reportService;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayForm()
    {
        return new ModelAndView(Views.SUMMARY_REPORT_FORM)
            .addObject(ATTRIBUTE_REPORT_PARAMETERS, new ResultSearchParameters());
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
            @ModelAttribute(ATTRIBUTE_REPORT_PARAMETERS) final ResultSearchParameters params)
    {
        return new ModelAndView(Views.SUMMARY_REPORT_RESULTS)
            .addObject(ATTRIBUTE_REPORT, makeReport(params));
    }
    
}
