package gov.va.med.srcalc.domain.variable;

import java.util.*;

import javax.persistence.*;

/**
 * A NumericalVariable that ultimately represents one of a finite, discrete
 * set of values. This is mainly useful for a lab results (e.g., White Blood
 * Count &lt;= 11.0x1000/mm^3 or &gt; 11.0x100/mm^3), but may be used for other
 * numerical values such as Body Mass Index.
 */
@Entity
@Table(name = "discrete_numerical_var")  // slightly abbreviate long table name
public class DiscreteNumericalVariable extends NumericalVariable
{
    private Set<Category> fCategories = new HashSet<>();
    
    /**
     * For reflection-based construction only. Business code should use
     * {@link #DiscreteNumericalVariable(String, VariableGroup, String)}.
     */
    DiscreteNumericalVariable()
    {
    }
    
    public DiscreteNumericalVariable(
            final String displayName,
            final VariableGroup group,
            final Set<Category> categories)
    {
        super(displayName, group);
        fCategories = categories;
    }

    @ElementCollection(fetch = FetchType.EAGER)  // eager-load due to close association
    @CollectionTable(
            name = "discrete_numerical_var_category",
            joinColumns = @JoinColumn(name = "variable_id"))
    public Set<Category> getCategories()
    {
        return fCategories;
    }

    protected void setCategories(final Set<Category> ranges)
    {
        fCategories = ranges;
    }

    @Override
    public void accept(final VariableVisitor visitor) throws Exception
    {
        visitor.visitDiscreteNumerical(this);
    }
    
    /**
     * Returns the Category containing the given value, or null if none contains
     * it.
     * @return the containing Category or null
     */
    public Category getContainingCategory(final float value)
    {
        for (final Category c : getCategories())
        {
            if (c.getRange().isValueInRange(value))
            {
                return c;
            }
        }
        
        return null;
    }

    /**
     * A Category for a {@link DiscreteNumericalVariable}. Presents an immutable
     * public interface.
     */
    @Embeddable
    public static final class Category
    {
        private NumericalRange fRange;
        
        private MultiSelectOption fOption;
        
        /**
         * For reflection-based construction only. Business code should use
         * {@link #Category(NumericalRange, MultiSelectOption)}.
         */
        Category()
        {
        }
        
        /**
         * Constructs an instance.
         */
        public Category(
                final NumericalRange range, final MultiSelectOption option)
        {
            fRange = Objects.requireNonNull(range);
            fOption = Objects.requireNonNull(option);
        }
        
        @Embedded
        public NumericalRange getRange()
        {
            return fRange;
        }
        
        /**
         * For reflection-based construction only.
         * @param range must not be null
         */
        void setRange(NumericalRange range)
        {
            fRange = Objects.requireNonNull(range);
        }
        
        @OneToOne
        @JoinColumn(name = "option_id", nullable = false)
        public MultiSelectOption getOption()
        {
            return fOption;
        }
        
        /**
         * For reflection-based construction only.
         * @param option must not be null
         */
        void setOption(MultiSelectOption option)
        {
            fOption = Objects.requireNonNull(option);
        }
        
        @Override
        public String toString()
        {
            return String.format("%s[range=%s]", getOption(), getRange());
        }
        
        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof Category)
            {
                final Category other = (Category)obj;
                return 
                        this.getRange().equals(other.getRange()) &&
                        this.getOption().equals(other.getOption());
            }
            else
            {
                return false;
            }
        }
        
        @Override
        public int hashCode()
        {
            return Objects.hash(getRange(), getOption());
        }
    }
}
