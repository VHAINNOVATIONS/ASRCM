package gov.va.med.srcalc.domain.variable;

import java.util.Comparator;

/**
 * Compares {@link Variable}s by their display names.
 */
public final class DisplayNameComparator implements Comparator<Variable>
{
    @Override
    public int compare(final Variable v1, final Variable v2)
    {
        return v1.getDisplayName().compareToIgnoreCase(v2.getDisplayName());
    }
}