package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.calculation.DiscreteNumericalValue;
import gov.va.med.srcalc.domain.calculation.Value;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSortedSet;

/**
 * <p>A {@link Variable} that ultimately represents one of a finite, discrete
 * set of values. This is mainly useful for a lab results (e.g., White Blood
 * Count &lt;= 11.0x1000/mm^3 or &gt; 11.0x100/mm^3), but may be used for other
 * numerical values such as Body Mass Index.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Entity
@Table(name = "discrete_numerical_var")  // slightly abbreviate long table name
public final class DiscreteNumericalVariable extends AbstractNumericalVariable
    implements DiscreteVariable
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
    
    /**
     * Constructs an instance.
     * @param categories the initial set of categories. This constructor will
     * make a defensive copy of the set.
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if any argument is invalid
     * @see AbstractVariable#AbstractVariable(String, VariableGroup, String)
     */
    public DiscreteNumericalVariable(
            final String displayName,
            final VariableGroup group,
            final Set<Category> categories,
            final String key)
    {
        super(displayName, group, key);
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
     * <p>Returns the Categories as {@link #getCategories()}, but WNL is always
     * sorted first.</p>
     * 
     * <p>This order is mainly useful for views, but provide it here for
     * convenience.</p>
     * @return an immutable sorted set
     */
    @Transient
    public ImmutableSortedSet<Category> getCategoriesWnlFirst()
    {
        return ImmutableSortedSet.copyOf(
                new WnlFirstCategoryComparator(), fCategories);
    }
    
    /**
     * Returns the list of discrete options, in the same order as {@link
     * #getCategories()}.
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
        // Category.contains() only checks whether the given value is within the upper
        // bound. So we iterate over the categories in ascending order, returning the
        // first Category that contains the value.
        for (final Category c : getCategories())
        {
            if (c.contains(value))
            {
                return c;
            }
        }
        
        return null;
    }
    
    /**
     * Returns a {@link Value} object representing the given Category selection.
     * @param category
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
     * <p>A Category for a {@link DiscreteNumericalVariable}. Presents an immutable
     * public interface.</p>
     * 
     * <p>This class only stores an upper bound. The lower bound is inferred
     * from the preceding Category. In the case of the first category, the lower
     * bound is that of the valid range.</p>
     */
    @Embeddable
    public static final class Category implements Comparable<Category>
    {
        private float fUpperBound;
        
        private boolean fUpperInclusive;
        
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
                final MultiSelectOption option,
                final float upperBound,
                final boolean upperInclusive)
        {
            fOption = Objects.requireNonNull(option);
            fUpperBound = upperBound;
            fUpperInclusive = upperInclusive;
        }
        
        /**
         * <p>Returns the upper bound of this Category.</p>
         * 
         * <p>Note that we do not store a lower bound. See the class Javadoc.</p>
         */
        @Basic
        public final float getUpperBound()
        {
            return fUpperBound;
        }
        
        /**
         * Sets the upper bound. For reflection-based construction only: this class
         * presents an immutable public interface.
         */
        final void setUpperBound(final float upperBound)
        {
            fUpperBound = upperBound;
        }

        /**
         * <p>Returns whether the upper bound is inclusive.</p>
         */
        @Basic
        public final boolean isUpperInclusive()
        {
            return fUpperInclusive;
        }
        
        /**
         * Sets whether the upper bound is inclusive. For reflection-based construction
         * only: this class presents an immutable public interface.
         */
        final void setUpperInclusive(final boolean upperInclusive)
        {
            fUpperInclusive = upperInclusive;
        }
        
        /**
         * Returns whether this Category contains the given number. Note that this class
         * does not store a lower bound so this method only considers whether the number
         * is within the upper bound.
         * @return true if the given number is within the upper bound
         */
        public boolean contains(final float f)
        {
            return fUpperInclusive ? (f <= fUpperBound) : (f < fUpperBound);
        }
        
        /**
         * Returns the {@link MultiSelectOption} representing this category's
         * discrete value.
         */
        @Embedded
        public final MultiSelectOption getOption()
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
        
        @Transient
        public boolean isWnl()
        {
        	return this.getOption().getValue().equalsIgnoreCase("WNL");
        }
        
        @Override
        public String toString()
        {
            return String.format(
                    "%s[upperBound=%s,inclusive=%s]",
                    getOption(), fUpperBound, fUpperInclusive);
        }
        
        @Override
        public boolean equals(final Object obj)
        {
            if (obj instanceof Category)
            {
                final Category other = (Category)obj;
                return 
                        new Float(this.fUpperBound).equals(other.fUpperBound) &&
                        this.fUpperInclusive == other.fUpperInclusive &&
                        // See above: option is always non-null.
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
            return Objects.hash(fUpperBound, fUpperInclusive, getOption());
        }
        
        /**
         * <p>Orders categories in ascending upper bound order. If two categories have the
         * same upper bound, one with an inclusive upper bound will be considered greater
         * than one with an exclusive upper bound since it does contain higher numbers.
         * 
         * <p>If the upper bounds and inclusivity flags are equal, we then compare against
         * the category value in order to make this comparison consistent with equals.
         * Otherwise strange things can happen in the SortedSet.</p> 
         * 
         * <p>To summarize, the following attributes are compared, from highest precendence
         * to lowest:</p>
         * 
         * <ol>
         * <li>The upper bound number</li>
         * <li>Inclusivity: inclusive is considered greater than exclusive</li>
         * <li>The value</li>
         * </ol>
         */
        @Override
        public int compareTo(final Category other)
        {
            int boundDelta = Float.compare(this.fUpperBound, other.fUpperBound);
            if (boundDelta != 0)
            {
                return boundDelta;
            }
            
            int inclusiveDelta = Boolean.compare(this.fUpperInclusive, other.fUpperInclusive);
            if (inclusiveDelta != 0)
            {
                return inclusiveDelta;
            }
            
            return this.getOption().getValue().compareTo(other.getOption().getValue());
        }
    }

    /**
     * <p>
     * Orders Categories for {@link DiscreteNumericalVariable#getCategoriesWnlFirst()}.
     * </p>
     * <p>
     * Per Effective Java Item 17, this class is marked final because it was not
     * designed for inheritance.
     * </p>
     */
    private final class WnlFirstCategoryComparator
        implements Comparator<Category>
    {
        @Override
        public int compare(final Category a, final Category b)
        {
            if (a.isWnl())
            {
                if (!b.isWnl())
                {
                    return -1;
                }
            }
            else
            {
                if (b.isWnl())
                {
                    return 1;
                }
            }
            
            return a.compareTo(b);
        }
        
    }
}
