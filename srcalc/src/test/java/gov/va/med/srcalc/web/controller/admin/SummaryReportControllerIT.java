package gov.va.med.srcalc.web.controller.admin;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import gov.va.med.srcalc.db.ResultsDao;
import gov.va.med.srcalc.domain.calculation.SampleCalculations;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.util.SearchResults;
import gov.va.med.srcalc.web.SrcalcUrls;
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
                    SummaryReportController.ATTRIBUTE_REPORT_PARAMETERS));
    }
    
    @Test
    public final void testDisplayReport() throws Exception
    {
        final SearchResults<SummaryReportRow> expectedResults = new SearchResults<>(
                SummaryReportRow.fromSignedResult(SampleCalculations.signedThoracic()),
                false);
        
        fMockMvc.perform(post(SrcalcUrls.SUMMARY_REPORT))
            .andExpect(status().isOk())
            .andExpect(model().attribute(
                    SummaryReportController.ATTRIBUTE_REPORT,
                    hasProperty("results", equalTo(expectedResults))));
    }
    
}
