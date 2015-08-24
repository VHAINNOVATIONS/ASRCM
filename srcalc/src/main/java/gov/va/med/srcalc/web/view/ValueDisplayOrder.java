package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.calculation.Value;
import gov.va.med.srcalc.domain.model.DisplayNameComparator;
import gov.va.med.srcalc.domain.model.VariableGroup;

import java.util.Comparator;

import com.google.common.collect.ComparisonChain;

/**
 * Defines the display order of values. Procedures first, then everything else
 * sorted by variable name.
 */
public class ValueDisplayOrder implements Comparator<Value>
{
    private boolean isProcedureValue(final Value v)
    {
        return v.getVariable().getGroup().getName().equals(VariableGroup.PROCEDURE_GROUP);
    }

    @Override
    public int compare(final Value a, final Value b)
    {
        return ComparisonChain.start()
                // A variable from the procedure group precedes a variable not from it.
                .compareTrueFirst(isProcedureValue(a), isProcedureValue(b))
                .compare(a.getVariable(), b.getVariable(), new DisplayNameComparator())
                .result();
    }
    
}
