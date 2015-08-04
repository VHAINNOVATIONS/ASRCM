package gov.va.med.srcalc.service;

import org.springframework.dao.DataAccessException;

import gov.va.med.srcalc.db.HistoricalSearchParameters;
import gov.va.med.srcalc.db.ResultSearchParameters;
import gov.va.med.srcalc.domain.calculation.HistoricalRunInfo;
import gov.va.med.srcalc.domain.calculation.SignedResult;
import gov.va.med.srcalc.util.SearchResults;

/**
 * Service Layer facade for generating reports.
 * @see gov.va.med.srcalc.service
 */
public interface ReportService
{
    /**
     * Loads SignedResults matching the given search parameters from the persistent store.
     * @throws DataAccessException if any error occurs interacting with the persistent
     * store
     */
    public SearchResults<SignedResult> getSignedResults(
            final ResultSearchParameters parameters);
    
    /**
     * Generates {@link HistoricalRunInfo}s from historical calculation data.
     * @param parameters specifies which calculations to consider
     * @return results ordered by start timestamp, descending
     * @throws DataAccessException if any error occurs interacting with the persistent
     * store
     */
    public SearchResults<HistoricalRunInfo> getHistoricalRunInfos(
            final HistoricalSearchParameters parameters);
}
