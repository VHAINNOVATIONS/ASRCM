package gov.va.med.srcalc.domain.variable;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link Variable} that ultimately represents one of a finite, discrete
 * set of values. This is mainly useful for a lab results (e.g., White Blood
 * Count &lt;= 11.0x1000/mm^3 or &gt; 11.0x100/mm^3), but may be used for other
 * numerical values such as Body Mass Index.
 */
@Entity
@Table(name = "discrete_numerical_var")  // slightly abbreviate long table name
public class DiscreteNumericalVariable extends AbstractNumericalVariable implements DiscreteVariable
{
    private static final Logger fLogger = LoggerFactory.getLogger(DiscreteNumericalVariable.class);
    
    private SortedSet<Category> fCategories = new TreeSet<>();
    
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
        fCategories = new TreeSet<>(categories);
    }

    @ElementCollection(fetch = FetchType.EAGER)  // eager-load due to close association
    @Sort(type = SortType.NATURAL)
    // Override strange defaults.
    @CollectionTable(
            name = "discrete_numerical_var_category",
            joinColumns = @JoinColumn(name = "variable_id"))
    public SortedSet<Category> getCategories()
    {
        return fCategories;
    }

    protected void setCategories(final SortedSet<Category> categories)
    {
        fCategories = categories;
    }
    
    /**
     * Returns the list of discrete options.
     * @return an unmodifiable list
     */
    @Override
    @Transient
    public List<MultiSelectOption> getOptions()
    {
        // Construct a new list every time for now. If we see this method being
        // called often, it may be worth caching the list.
        fLogger.debug("Constructing MultiSelectOption list from categories");
        final ArrayList<MultiSelectOption> options = new ArrayList<>(fCategories.size());
        for (final Category c : fCategories)
        {
            options.add(c.getOption());
        }
        return Collections.unmodifiableList(options);
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
        // There should be less than 10 categories in the real world, so just
        // search via iteration.
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
     * Returns a {@link Value} object representing the given Category selection.
     * @param category
     * @return
     */
    public DiscreteNumericalValue makeValue(final Category category)
    {
        return DiscreteNumericalValue.fromCategory(this, category);
    }
    
    /**
     * Returns a {@link Value} object representing the given numerical value.
     * @param floatValue
     * @throws ValueTooHighException 
     * @throws ValueTooLowException 
     * @throws ConfigurationException if the value is not in any of the
     * categories but is within the valid range
     */
    public DiscreteNumericalValue makeValue(final float floatValue)
            throws ValueTooLowException, ValueTooHighException
    {
        return DiscreteNumericalValue.fromNumerical(this, floatValue);
    }

    /**
     * A Category for a {@link DiscreteNumericalVariable}. Presents an immutable
     * public interface.
     */
    @Embeddable
    public static final class Category implements Comparable<Category>
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
                // See above: range and option are always non-null.
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
        
        /**
         * Orders categories by their {@link NumericalRange}s.
         */
        @Override
        public int compareTo(Category other)
        {
            // See above: the range is always non-null.
            return this.getRange().compareTo(other.getRange());
        }
    }
}
