package gov.va.med.srcalc.web.view.admin;

import java.util.HashMap;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSortedMap;

import gov.va.med.srcalc.db.HistoricalSearchParameters;
import gov.va.med.srcalc.domain.calculation.HistoricalRunInfo;
import gov.va.med.srcalc.util.SearchResults;

/**
 * <p>Encapsulates Utilization Report data. Even though we only display the summary
 * statistics on the UI, we still store the full results since they are available
 * anyway.</p>
 * 
 * <p>The references in this class cannot be changed, but it is not truly immutable
 * because {@link HistoricalSearchParameters} is mutable.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class UtilizationReport
    extends BaseReport<HistoricalSearchParameters, HistoricalRunInfo>
{
    // Note that equals() does not depend on this property, but that's OK because this
    // property is derived form the others.
    private final ImmutableSortedMap<String, UtilizationSummary> fSpecialtySummaries;
    
    /**
     * Constructs an instance with the given properties and a generationDate of now.
     * @param parameters See {@link #getParameters()}.
     * @param results See {@link #getResults()}.
     * @throws NullPointerException if any argument is null
     */
    public UtilizationReport(
            final HistoricalSearchParameters params,
            final SearchResults<HistoricalRunInfo> results)
    {
        super(params, results);
        
        // Group the run infos by specialty and calculate statistics.
        final ArrayListMultimap<String, HistoricalRunInfo> specialtyGroups = ArrayListMultimap.create();
        for (final HistoricalRunInfo runInfo : results.getFoundItems())
        {
            specialtyGroups.put(
                    runInfo.getHistoricalCalculation().getSpecialtyName(), runInfo);
        }
        final HashMap<String, UtilizationSummary> summaries = new HashMap<>();
        for (final String specialtyName : specialtyGroups.keySet())
        {
            summaries.put(
                    specialtyName,
                    UtilizationSummary.fromRunInfos(specialtyGroups.get(specialtyName)));
        }
        fSpecialtySummaries =
                ImmutableSortedMap.copyOf(summaries, String.CASE_INSENSITIVE_ORDER);
    }
    
    /**
     * Returns {@link UtilizationSummary}s for each specialty.
     * @return a map sorted by Specialty name (case-insensitive)
     */
    public ImmutableSortedMap<String, UtilizationSummary> getSpecialtySummaries()
    {
        return fSpecialtySummaries;
    }
}
