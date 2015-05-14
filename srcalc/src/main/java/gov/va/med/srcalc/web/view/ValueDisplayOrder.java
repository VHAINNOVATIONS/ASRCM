package gov.va.med.srcalc.web.view;

import gov.va.med.srcalc.domain.calculation.Value;
import gov.va.med.srcalc.domain.model.DisplayNameComparator;

import java.util.Comparator;

/**
 * Defines the display order of values. Procedures first, then everything else
 * sorted by variable name.
 */
public class ValueDisplayOrder implements Comparator<Value>
{
    private static final String PROCEDURE_GROUP = "Planned Procedure";
    
    @Override
    public int compare(final Value a, final Value b)
    {
        // Sort ProcedureValues first.
        final int aIsProcedure = (a.getVariable().getGroup().getName().equals(PROCEDURE_GROUP)) ? 1 : 0;
        final int bIsProcedure = (b.getVariable().getGroup().getName().equals(PROCEDURE_GROUP)) ? 1 : 0;
        // Subtract b from a to because a ProcedureValue is _less than_ a non-
        // ProcedureValue.
        final int procedureDelta = bIsProcedure - aIsProcedure;
        if (procedureDelta != 0)
        {
            return procedureDelta;
        }
        
        // Both were either ProcedureValues or not. Compare by variable name.
        return new DisplayNameComparator().compare(a.getVariable(), b.getVariable());
    }
    
}
