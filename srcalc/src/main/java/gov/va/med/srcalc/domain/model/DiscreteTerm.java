package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.calculation.DiscreteValue;
import gov.va.med.srcalc.domain.calculation.Value;

import java.util.Objects;

import javax.persistence.*;

/**
 * <p>A ModelTerm related to a particular option of a {@link DiscreteVariable}. Presents
 * an immutable public interface.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Embeddable
public final class DiscreteTerm extends SingleVariableTerm
{
    private static final int UNSET_INDEX = -1;

    private DiscreteVariable fVariable = null;
    private int fOptionIndex = UNSET_INDEX;
    
    DiscreteTerm()
    {
    }

    /**
     * Constructs an instance.
     * @param variable
     * @param optionIndex
     * @param coefficient
     * @throws IndexOutOfBoundsException if the index does not exist in the
     * variable's options
     */
    public DiscreteTerm(
            final DiscreteVariable variable, final int optionIndex, final float coefficient)
    {
        super(coefficient);
        fVariable = Objects.requireNonNull(variable);
        fOptionIndex = optionIndex;
    }
    
    @Override
    @ManyToOne(optional = false, targetEntity = AbstractVariable.class)
    public DiscreteVariable getVariable()
    {
        return fVariable;
    }

    /**
     * For reflection-based construction only. The associated Variable should
     * not be changed.
     * @deprecated because code should not explicitly call this method
     */
    @Deprecated
    void setVariable(final DiscreteVariable variable)
    {
        fVariable = Objects.requireNonNull(variable);
    }

    @Basic
    public int getOptionIndex()
    {
        return fOptionIndex;
    }

    /**
     * For reflection-based construction only. The associated option should not
     * be changed.
     * @deprecated because code should not explicitly call this method
     */
    @Deprecated
    void setOptionIndex(final int optionIndex)
    {
        fOptionIndex = optionIndex;
    }
    
    /**
     * Returns the associated {@link MultiSelectOption}.
     * @throws IndexOutOfBoundsException if the configured index is out of bounds
     */
    @Transient
    public MultiSelectOption getOption()
    {
        // TODO: it would be much better to cache this term it's hard due to
        // Hibernate's reflection-based construction. We need something like
        // @PostLoad.
        return fVariable.getOptions().get(getOptionIndex());
    }
    
    @Override
    public double getSummand(final Value inputValue)
    {
        try
        {
            final DiscreteValue discreteValue = (DiscreteValue)inputValue;

            final boolean isSelected =
                    getOption().equals(discreteValue.getSelectedOption());
            
            return isSelected ? getCoefficient() : 0.0;
        }
        catch (ClassCastException ex)
        {
            throw new IllegalArgumentException("Value was not a DiscreteValue", ex);
        }
    }
    
    @Override
    public boolean equals(final Object o)
    {
        // Performance optimization.
        if (o == this)
        {
            return true;
        }
        
        if (o instanceof DiscreteTerm)
        {
            final DiscreteTerm other = (DiscreteTerm)o;
            return
                    baseEquals(other) &&
                    this.getOptionIndex() == other.getOptionIndex();
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getCoefficient(), getVariable(), getOptionIndex());
    }
    
    @Override
    public String toString()
    {
        return String.format(
                "%s=%s?->%s", getVariable(), getOption(), getCoefficient());
    }
    
    @Override
    public void accept(final ModelTermVisitor visitor)
    {
        visitor.visitDiscreteTerm(this);
    }
}
