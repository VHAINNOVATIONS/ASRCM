package gov.va.med.srcalc.web.controller.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import gov.va.med.srcalc.db.HistoricalSearchParameters;
import gov.va.med.srcalc.db.ResultsDao;
import gov.va.med.srcalc.domain.calculation.HistoricalRunInfo;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;
import gov.va.med.srcalc.domain.calculation.SignedResult;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.util.SearchResults;
import gov.va.med.srcalc.web.SrcalcUrls;
import gov.va.med.srcalc.web.view.Views;
import gov.va.med.srcalc.web.view.admin.UtilizationReport;

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

import com.google.common.collect.ImmutableList;

/**
 * Integration Tests for {@link UtilizationReportController}. Only tests some basic happy-
 * path and error-path cases: unit tests should cover the details of the classes involved.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  // need to tell Spring to instantiate a WebApplicationContext.
@ContextConfiguration({"/srcalc-context.xml", "/srcalc-controller.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class UtilizationReportControllerIT extends IntegrationTest
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
        fMockMvc.perform(get(SrcalcUrls.UTILIZATION_REPORT))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists(
                    UtilizationReportController.ATTRIBUTE_REPORT_PARAMETERS));
    }
    
    @Test
    public final void testDisplayReport() throws Exception
    {
        final SignedResult result = SampleCalculations.signedThoracic();
        final HistoricalRunInfo expectedRunInfo = HistoricalRunInfo.signed(result);
        final SearchResults<HistoricalRunInfo> expectedResults = new SearchResults<>(
                ImmutableList.of(expectedRunInfo), false);
        final HistoricalSearchParameters expectedParams = new HistoricalSearchParameters();
        
        fMockMvc.perform(post(SrcalcUrls.UTILIZATION_REPORT)
                // Populate empty parameters just like in the actual browser request.
                .param("maxDate", "")
                .param("maxDate", ""))
            .andExpect(status().isOk())
            .andExpect(model().attribute(
                    UtilizationReportController.ATTRIBUTE_REPORT,
                    new UtilizationReport(expectedParams, expectedResults)));
                
    }
    
    @Test
    public final void testDisplayReportFilters() throws Exception
    {
        final SignedResult result = SampleCalculations.signedThoracic();
        final HistoricalRunInfo expectedRunInfo = HistoricalRunInfo.signed(result);
        final SearchResults<HistoricalRunInfo> expectedResults = new SearchResults<>(
                ImmutableList.of(expectedRunInfo), false);
        final HistoricalSearchParameters expectedParams = new HistoricalSearchParameters();
        final DateTime startTimestamp = result.getHistoricalCalculation().getStartTimestamp();
        final LocalDate minDate = new LocalDate(startTimestamp).minusDays(2);
        final LocalDate maxDate = new LocalDate(startTimestamp);
        expectedParams.setMinDate(minDate);
        expectedParams.setMaxDate(maxDate);
        
        final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("MM/dd/yyyy");
        fMockMvc.perform(post(SrcalcUrls.UTILIZATION_REPORT)
                .param("minDate", dateFormat.print(minDate))
                .param("maxDate", dateFormat.print(maxDate)))
            .andExpect(status().isOk())
            .andExpect(model().attribute(
                    UtilizationReportController.ATTRIBUTE_REPORT,
                    new UtilizationReport(expectedParams, expectedResults)));
    }
    
    @Test
    public final void testDisplayReportErrors() throws Exception
    {
        fMockMvc.perform(post(SrcalcUrls.UTILIZATION_REPORT)
                .param("maxDate", "1111")
                .param("maxDate", ""))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors(
                    SummaryReportController.ATTRIBUTE_REPORT_PARAMETERS))
            .andExpect(view().name(Views.UTILIZATION_REPORT_FORM));
    }
    
}
