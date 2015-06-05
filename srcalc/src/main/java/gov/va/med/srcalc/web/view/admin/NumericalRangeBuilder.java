package gov.va.med.srcalc.web.view.admin;

import com.google.common.base.MoreObjects;

import gov.va.med.srcalc.domain.model.NumericalRange;

/**
 * <p>Builds a {@link NumericalRange} from incrementally-specified component
 * parts. This class is useful because NumericalRange is immutable: you must
 * specify all properties in its constructor and you can't use it as a Java
 * bean.</p>
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
public final class NumericalRangeBuilder
{
    private float fLowerBound;
    private boolean fLowerInclusive;
    private float fUpperBound;
    private boolean fUpperInclusive;
    
    /**
     * Constructs an instance. The default bounds are [0,100.0].
     */
    public NumericalRangeBuilder()
    {
        fLowerBound = 0.0f;
        fLowerInclusive = true;
        fUpperBound = 100.0f;
        fUpperInclusive = true;
    }
    
    /**
     * Constructs an instance with the given initial values. These initial
     * values may, of course, be changed before calling {@link #build()}.
     */
    public NumericalRangeBuilder(
            final float initialLowerBound,
            final boolean initialLowerInclusive,
            final float initialUpperBound,
            final boolean initialUpperInclusive)
    {
        fLowerBound = initialLowerBound;
        fLowerInclusive = initialLowerInclusive;
        fUpperBound = initialUpperBound;
        fUpperInclusive = initialUpperInclusive;
    }
    
    /**
     * Factory method to create an instance based on a prototype NumericalRange.
     * @return a new builder that will build ranges equivalent to the given
     * prototype
     */
    public static NumericalRangeBuilder fromPrototype(
            final NumericalRange prototypeRange)
    {
        return new NumericalRangeBuilder(
                prototypeRange.getLowerBound(),
                prototypeRange.isLowerInclusive(),
                prototypeRange.getUpperBound(),
                prototypeRange.isUpperInclusive());
    }

    /**
     * Returns the lower bound to use.
     */
    public float getLowerBound()
    {
        return fLowerBound;
    }

    /**
     * Sets the lower bound to use.
     * @return this builder for convenience
     */
    public NumericalRangeBuilder setLowerBound(final float lowerBound)
    {
        fLowerBound = lowerBound;
        return this;
    }

    /**
     * Returns the inclusive flag to use for the lower bound.
     */
    public boolean getLowerInclusive()
    {
        return fLowerInclusive;
    }

    /**
     * Sets the inclusive flag to use for the lower bound.
     * @return this builder for convenience
     */
    public NumericalRangeBuilder setLowerInclusive(final boolean lowerInclusive)
    {
        fLowerInclusive = lowerInclusive;
        return this;
    }

    /**
     * Returns the upper bound to use.
     */
    public float getUpperBound()
    {
        return fUpperBound;
    }

    /**
     * Sets the upper bound to use.
     * @return this builder for convenience
     */
    public NumericalRangeBuilder setUpperBound(final float upperBound)
    {
        fUpperBound = upperBound;
        return this;
    }

    /**
     * Returns the inclusive flag to use for the upper bound.
     */
    public boolean getUpperInclusive()
    {
        return fUpperInclusive;
    }

    /**
     * Sets the inclusive flag to use for the upper bound.
     * @return this builder for convenience
     */
    public NumericalRangeBuilder setUpperInclusive(final boolean upperInclusive)
    {
        fUpperInclusive = upperInclusive;
        return this;
    }
    
    /**
     * Builds the NumericalRange with the configured bounds.
     */
    public NumericalRange build()
    {
        return new NumericalRange(
                fLowerBound, fLowerInclusive, fUpperBound, fUpperInclusive);
    }
    
    /**
     * Returns a string representing the object. The exact format is unspecified
     * but it will contain the currently-set bounds and inclusive flags.
     */
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("fLowerBound", fLowerBound)
                .add("fUpperInclusive", fUpperInclusive)
                .add("fUpperBound", fUpperBound)
                .add("fUpperInclusive", fUpperInclusive)
                .toString();
    }
}
