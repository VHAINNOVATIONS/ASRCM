package gov.va.med.srcalc.db;

import static org.junit.Assert.*;
import gov.va.med.srcalc.domain.calculation.HistoricalCalculation;
import gov.va.med.srcalc.domain.calculation.SignedResult;
import gov.va.med.srcalc.test.util.IntegrationTest;
import gov.va.med.srcalc.util.SearchResults;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Tests {@link ResultsDao}. Integration Testing (with an actual database) is really the
 * only way to test this class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/srcalc-context.xml", "/test-context.xml"})
@Transactional // run each test in its own (rolled-back) transaction
public class ResultsDaoIT extends IntegrationTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultsDaoIT.class);

    /*
     * Sample Results & Component Parts
     * 
     * populateSampleResults() puts these in the database and various tests use them.
     */
    private static final String SPECIALTY_THORACIC = "Thoracic";
    private static final String SPECIALTY_NEURO = "Neurosurgery";
    private static final String SPECIALTY_CARDIAC = "Cardiac";
    private static final String PROVIDER_TYPE_1 = "Physicians (M.D. and D.O.)";
    private static final String CPT_CODE_1 = "47010";
    private static final String STATION_NUMBER_1 = "500";
    private static final String STATION_NUMBER_2 = "442";
    private static final String STATION_NUMBER_3 = "512";
    private static final ImmutableMap<String, String> VALUES_PROCEDURE_1 =
            ImmutableMap.of("procedure", "47010 - Open drainage liver lesion (19.4)");
    private static final String CPT_CODE_2 = "44141";
    private static final ImmutableMap<String, String> VALUES_PROCEDURE_2 =
            ImmutableMap.of("procedure", "44141 - Partial removal of colon - (29.91)");
    private static final ImmutableMap<String, String> VALUES_NON_PROCEDURE =
            ImmutableMap.of("gender", "Female");
    
    /*
     * Note: the below items must be fields, not constants, because the ID attribute is
     * actually changed (by Hibernate). Otherwise, we would have test cross-contamination.
     */

    private final HistoricalCalculation fHistoricalCalc1 = new HistoricalCalculation(
            SPECIALTY_THORACIC,
            STATION_NUMBER_1,
            new DateTime(2015, 3, 4, 10, 5).withSecondOfMinute(51),
            50,
            Optional.of(PROVIDER_TYPE_1));

    private final SignedResult fSampleResult1 = new SignedResult(
            fHistoricalCalc1,
            1001,
            Optional.of(CPT_CODE_1),
            new DateTime(2015, 3, 4, 10, 10).withSecondOfMinute(52),
            VALUES_PROCEDURE_1,
            ImmutableMap.of("Thoracic 90-Day", 24.4f, "Thoracic 30-Day", 20.1f));
    
    private final HistoricalCalculation fHistoricalCalc2 = new HistoricalCalculation(
            SPECIALTY_THORACIC,
            STATION_NUMBER_2,
            new DateTime(2014, 1, 1, 12, 55),
            60,
            Optional.of(PROVIDER_TYPE_1));
    
    private final SignedResult fSampleResult2 = new SignedResult(
            fHistoricalCalc2,
            1002,
            Optional.of(CPT_CODE_2),
            new DateTime(2014, 1, 1, 13, 1),
            VALUES_PROCEDURE_2,
            ImmutableMap.of("Thoracic 30-Day", 13.9f));
    
    private final HistoricalCalculation fHistoricalCalc3 = new HistoricalCalculation(
            SPECIALTY_NEURO,
            STATION_NUMBER_3,
            new DateTime(2015, 6, 7, 10, 5),
            70,
            Optional.<String>absent());
    
    private final SignedResult fSampleResult3 = new SignedResult(
            fHistoricalCalc3,
            1003,
            Optional.of(CPT_CODE_1),
            new DateTime(2015, 6, 7, 11, 1),
            VALUES_PROCEDURE_1,
            ImmutableMap.of("Neurosurgery 30-Day", 24.5f));
    
    private final HistoricalCalculation fHistoricalCalc4 = new HistoricalCalculation(
            SPECIALTY_CARDIAC,
            STATION_NUMBER_2,
            new DateTime(2015, 6, 8, 13, 34, 23),
            80,
            Optional.<String>absent());
    
    private final SignedResult fSampleResult4 = new SignedResult(
            fHistoricalCalc4,
            1004,
            Optional.<String>absent(),
            new DateTime(2015, 6, 8, 13, 34, 59),
            VALUES_NON_PROCEDURE,
            ImmutableMap.of("Cardiac 30-Day", 73.5f));

    @Autowired
    ResultsDao fResultsDao;
    
    @Before
    public final void setup()
    {
        // Note: @Before methods _are_ executed within the test transaction, so this is
        // safe.
        populateSampleResults();
    }
    
    /**
     * Populates the test database with the above sample results.
     */
    private void populateSampleResults()
    {
        LOGGER.info("Populating sample results in DB.");
        fResultsDao.persistSignedResult(fSampleResult1);
        fResultsDao.persistSignedResult(fSampleResult2);
        fResultsDao.persistSignedResult(fSampleResult3);
        fResultsDao.persistSignedResult(fSampleResult4);
        simulateNewSession();
    }

    @Test
    public final void testGetSignedResultsBySpecialty()
    {
        /* Setup */
        final SearchResults<SignedResult> expectedResults = new SearchResults<>(
                ImmutableList.of(fSampleResult3, fSampleResult1, fSampleResult2),
                false);
        
        /* Behavior */

        final ResultSearchParameters params = new ResultSearchParameters();
        params.setSpecialtyNames(ImmutableSet.of("Neurosurgery", "Thoracic"));
        final SearchResults<SignedResult> actualResults =
                params.doSearch(getHibernateSession());
        
        /* Verification */
        assertEquals(expectedResults, actualResults);
    }

    @Test
    public final void testGetSignedResultsByCpt()
    {
        /* Setup */
        final SearchResults<SignedResult> expectedResults = new SearchResults<>(
                ImmutableList.of(fSampleResult3, fSampleResult1),
                false);
        
        /* Behavior */

        final ResultSearchParameters params = new ResultSearchParameters();
        params.setCptCode(CPT_CODE_1);
        final SearchResults<SignedResult> actualResults =
                params.doSearch(getHibernateSession());
        
        /* Verification */
        assertEquals(expectedResults, actualResults);
    }
    
    @Test
    public final void testGetSignedResultsByDate()
    {
        /* Setup */
        final SearchResults<SignedResult> expectedResults = new SearchResults<>(
                ImmutableList.of(fSampleResult1), false);
        
        /* Behavior */
        final ResultSearchParameters params = new ResultSearchParameters();
        params.setMinDate(new LocalDate(2015, 3, 4));
        params.setMaxDate(new LocalDate(2015, 3, 4));
        final SearchResults<SignedResult> actualResults =
                params.doSearch(getHibernateSession());
        
        /* Verification */
        assertEquals(expectedResults, actualResults);
    }
    
    @Test
    public final void testGetSignedResultsByStation()
    {
        /* Setup */
        final SearchResults<SignedResult> expectedResults = new SearchResults<>(
                ImmutableList.of(fSampleResult4, fSampleResult2), false);
        
        /* Behavior */
        final ResultSearchParameters params = new ResultSearchParameters();
        params.setStationNumber(STATION_NUMBER_2);
        final SearchResults<SignedResult> actualResults =
                params.doSearch(getHibernateSession());
        
        /* Verification */
        assertEquals(expectedResults, actualResults);
        
    }
}
