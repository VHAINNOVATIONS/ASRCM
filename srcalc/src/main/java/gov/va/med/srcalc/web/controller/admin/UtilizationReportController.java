package gov.va.med.srcalc.web.controller.admin;

import javax.inject.Inject;

import gov.va.med.srcalc.db.HistoricalSearchParameters;
import gov.va.med.srcalc.service.ReportService;
import gov.va.med.srcalc.web.SrcalcUrls;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.UtilizationReport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for generating a Utilization Report.
 */
@Controller
@RequestMapping(SrcalcUrls.UTILIZATION_REPORT)
public class UtilizationReportController
{
    /**
     * The attribute name of the {@link HistoricalSearchParameters} object.
     */
    public static final String ATTRIBUTE_REPORT_PARAMETERS = "reportParameters";
    
    /**
     * The attribute name of the {@link UtilizationReport} object when showing the report.
     */
    public static final String ATTRIBUTE_REPORT = "report";
    
    private static final Logger LOGGER =
            LoggerFactory.getLogger(UtilizationReportController.class);

    private final ReportService fReportService;
    
    /**
     * Constructs an instance with the given dependencies.
     */
    @Inject
    public UtilizationReportController(final ReportService reportService)
    {
        fReportService = reportService;
    }
    
    /**
     * Presents the report parameters form.
     * @param params contains the report parameters. (This is a method parameter to
     * support re-presenting the form with validation errors.)
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView displayForm(
            @ModelAttribute(ATTRIBUTE_REPORT_PARAMETERS) final HistoricalSearchParameters params)
    {
        // Note: params is already in the Model via @ModelAttribute.
        return new ModelAndView(Views.UTILIZATION_REPORT_FORM);
    }
    
    private UtilizationReport makeReport(final HistoricalSearchParameters params)
    {
        return new UtilizationReport(params, fReportService.getHistoricalRunInfos(params));
    }
    
    /**
     * If the parameters are valid, generates and presents the report. If invalid,
     * presents the validation errors.
     * @param params the report parameters
     * @param bindingResult the BindingResult for the report parameters
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView displayReport(
            @ModelAttribute(ATTRIBUTE_REPORT_PARAMETERS) final HistoricalSearchParameters params,
            final BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            LOGGER.debug("Re-showing form due to errors: {}", bindingResult);
            return displayForm(params);
        }
        else
        {
            return new ModelAndView(Views.UTILIZATION_REPORT_RESULTS)
                .addObject(ATTRIBUTE_REPORT, makeReport(params));
        }
    }
}
