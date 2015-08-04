package gov.va.med.srcalc.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import gov.va.med.srcalc.db.HistoricalSearchParameters;
import gov.va.med.srcalc.db.ResultSearchParameters;
import gov.va.med.srcalc.db.ResultsDao;
import gov.va.med.srcalc.domain.calculation.HistoricalRunInfo;
import gov.va.med.srcalc.domain.calculation.SignedResult;
import gov.va.med.srcalc.util.SearchResults;

/**
 * Canonical implementation of {@link ReportService}.
 */
public class DefaultReportService implements ReportService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultReportService.class);
    
    private final ResultsDao fResultsDao;
    
    /**
     * Constructs an instance with the given dependencies.
     */
    @Inject
    public DefaultReportService(final ResultsDao resultsDao)
    {
        fResultsDao = resultsDao;
    }
    
    @Override
    @Transactional
    public SearchResults<SignedResult> getSignedResults(final ResultSearchParameters parameters)
    {
        final SearchResults<SignedResult> results =
                fResultsDao.getSignedResults(parameters);
        
        LOGGER.debug("SignedResult search returned: {}", results);
        
        return results;
    }
    
    @Override
    @Transactional
    public SearchResults<HistoricalRunInfo> getHistoricalRunInfos(
            final HistoricalSearchParameters parameters)
    {
        
        final SearchResults<HistoricalRunInfo> results =
                fResultsDao.getHistoricalRunInfos(parameters);
        
        LOGGER.debug("HistoricalRunInfo query returned: {}", results);
        
        return results;
    }
    
}
