package gov.va.med.srcalc.service;

import static org.mockito.Mockito.*;
import gov.va.med.srcalc.db.ResultSearchParameters;
import gov.va.med.srcalc.db.ResultsDao;

import org.junit.Test;

import com.google.common.base.Optional;

/**
 * Unit tests for {@link DefaultReportService}.
 */
public class DefaultReportServiceTest
{
    private final ResultsDao fMockResultsDao;
    
    public DefaultReportServiceTest()
    {
        fMockResultsDao = mock(ResultsDao.class);
    }
    
    @Test
    public final void testGetSignedResults()
    {
        /* Setup */
        final ResultSearchParameters searchParams = new ResultSearchParameters();
        searchParams.setCptCode(Optional.of("1111F"));
        final DefaultReportService service = new DefaultReportService(fMockResultsDao);

        /* Behavior */
        service.getSignedResults(searchParams);
        
        /*
         * Verification
         * 
         * Normally we use when/thenReturn mocking, but here verify() is so much easier.
         */
        verify(fMockResultsDao).getSignedResults(searchParams);
    }
    
}
