package gov.va.med.srcalc.domain.model;

import java.util.Comparator;

/**
 * Compares {@link Rule}s by their display names. Comparing is case-insensitive.
 */
public final class RuleDisplayNameComparator implements Comparator<Rule>
{
    @Override
    public int compare(final Rule rule1, final Rule rule2)
    {
        return rule1.getDisplayName().compareToIgnoreCase(rule2.getDisplayName());
    }
}
