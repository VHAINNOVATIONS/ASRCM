package gov.va.med.srcalc.domain.model;

import java.util.Comparator;

import com.google.common.collect.ComparisonChain;

/**
 * <p>Compares {@link Variable}s (case-insensitively) by their display names. In order
 * to be <i>consistent with equals</i>, if the display names compare as equal, then this
 * order compares the variable keys.</p>
 * 
 * <p>This comparison is consistent with equals: {@link #compare(Variable, Variable)}
 * returns 0 if and only if the variables are considered equal.</p>
 */
public final class DisplayNameComparator implements Comparator<Variable>
{
    @Override
    public int compare(final Variable v1, final Variable v2)
    {
        return ComparisonChain.start()
                .compare(v1.getDisplayName(), v2.getDisplayName(), String.CASE_INSENSITIVE_ORDER)
                // If the display names are equal, compare the keys to be consistent with
                // equals().
                .compare(v1.getKey(), v2.getKey())
                .result();
    }
}