package gov.va.med.srcalc.domain.variable;

import java.util.Set;

import javax.persistence.*;

/**
 * A Variable with lab result value. This is a subtype of {@link NumericalVariable}
 * because a LabVariable is a NumericalVariable with range categorization.
 */
@Entity
public class LabVariable extends NumericalVariable
{
    private Set<NumericalRange> fRanges;
    
    /**
     * For reflection-based construction only. Business code should use
     * {@link #LabVariable(String, VariableGroup, String)}.
     */
    LabVariable()
    {
    }
    
    public LabVariable(
            final String displayName,
            final VariableGroup group,
            final Set<NumericalRange> ranges)
    {
        super(displayName, group);
        fRanges = ranges;
    }

    @ElementCollection(fetch = FetchType.EAGER)  // eager-load due to close association
    public Set<NumericalRange> getRanges()
    {
        return fRanges;
    }

    public void setRanges(final Set<NumericalRange> ranges)
    {
        fRanges = ranges;
    }

    @Override
    public void accept(final VariableVisitor visitor) throws Exception
    {
        visitor.visitLab(this);
    }
}
