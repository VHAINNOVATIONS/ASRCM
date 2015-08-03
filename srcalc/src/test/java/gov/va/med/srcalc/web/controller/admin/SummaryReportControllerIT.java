package gov.va.med.srcalc.web.controller.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.ImmutableSet;

import gov.va.med.srcalc.db.ResultSearchParameters;
import gov.va.med.srcalc.db.ResultsDao;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;
import gov.va.med.srcalc.domain.calculation.SignedResult;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.util.SearchResults;
import gov.va.med.srcalc.web.SrcalcUrls;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.SummaryReport;
import gov.va.med.srcalc.web.view.admin.SummaryReportRow;

/**
 * Integration Tests for {@link SummaryReportController}. Only tests some basic happy-path
 * and error-path cases: unit tests should cover the details.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  // need to tell Spring to instantiate a WebApplicationContext.
@ContextConfiguration({"/srcalc-context.xml", "/srcalc-controller.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class SummaryReportControllerIT extends IntegrationTest
{
    @Autowired  // Field-based autowiring only in tests
    WebApplicationContext fWac;

    @Autowired
    ResultsDao fResultsDao;

    private MockMvc fMockMvc;

    @Before
    public void setup()
    {
        fMockMvc = MockMvcBuilders.webAppContextSetup(fWac).build();
        
        fResultsDao.persistSignedResult(SampleCalculations.signedThoracic());
        
        simulateNewSession();
    }
    
    @Test
    public final void testDisplayForm() throws Exception
    {
        fMockMvc.perform(get(SrcalcUrls.SUMMARY_REPORT))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists(
                    SummaryReportController.ATTRIBUTE_REPORT_PARAMETERS,
                    SummaryReportController.ATTRIBUTE_SPECIALTY_LIST,
                    SummaryReportController.ATTRIBUTE_PROCEDURE_LIST));
    }
    
    @Test
    public final void testDisplayReport() throws Exception
    {
        final SearchResults<SummaryReportRow> expectedResults = new SearchResults<>(
                SummaryReportRow.fromSignedResult(SampleCalculations.signedThoracic()),
                false);
        final ResultSearchParameters expectedParameters = new ResultSearchParameters();
        
        fMockMvc.perform(post(SrcalcUrls.SUMMARY_REPORT)
                // Populate empty parameters just like in the actual browser request.
                .param("cptCode", "")
                .param("maxDate", "")
                .param("maxDate", "")
                .param("stationNumber", "")
                .param("_specialtyNames", "on"))
            .andExpect(status().isOk())
            .andExpect(model().attribute(
                    SummaryReportController.ATTRIBUTE_REPORT,
                    new SummaryReport(expectedParameters, expectedResults)));
    }
    
    @Test
    public final void testDisplayReportFilters() throws Exception
    {
        final SignedResult result = SampleCalculations.signedThoracic();
        final String specialtyName = result.getHistoricalCalculation().getSpecialtyName();
        final String station = result.getHistoricalCalculation().getUserStation();
        final DateTime signatureTimestamp = result.getSignatureTimestamp();
        final LocalDate minDate = new LocalDate(signatureTimestamp).minusDays(1);
        final LocalDate maxDate = new LocalDate(signatureTimestamp).plusDays(10);
        final SearchResults<SummaryReportRow> expectedResults = new SearchResults<>(
                SummaryReportRow.fromSignedResult(result),
                false);
        final ResultSearchParameters expectedParameters = new ResultSearchParameters();
        expectedParameters.setCptCode(result.getCptCode().get());
        expectedParameters.setSpecialtyNames(ImmutableSet.of(specialtyName));
        expectedParameters.setMinDate(minDate);
        expectedParameters.setMaxDate(maxDate);
        expectedParameters.setStationNumber(station);
        
        final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("MM/dd/yyyy");
        fMockMvc.perform(post(SrcalcUrls.SUMMARY_REPORT)
                .param("cptCode", result.getCptCode().get())
                .param("minDate", dateFormat.print(minDate))
                .param("maxDate", dateFormat.print(maxDate))
                .param("stationNumber", station)
                .param("_specialtyNames", "on")
                .param("specialtyNames", specialtyName))
            .andExpect(status().isOk())
            .andExpect(model().attribute(
                    SummaryReportController.ATTRIBUTE_REPORT,
                    new SummaryReport(expectedParameters, expectedResults)));
    }
    
    @Test
    public final void testDisplayReportErrors() throws Exception
    {
        fMockMvc.perform(post(SrcalcUrls.SUMMARY_REPORT)
                .param("cptCode", "")
                .param("maxDate", "1111")
                .param("maxDate", "")
                .param("stationNumber", "")
                .param("_specialtyNames", "on"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors(
                    SummaryReportController.ATTRIBUTE_REPORT_PARAMETERS))
            .andExpect(view().name(Views.SUMMARY_REPORT_FORM));
    }
    
}
