package gov.va.med.srcalc.web.view.admin;

import gov.va.med.srcalc.domain.model.NumericalRange;

/**
 * Builds a {@link NumericalRange} from incrementally-specified component parts.
 * This class is useful because NumericalRange is immutable: you must specify
 * all properties in its constructor and you can't use it as a Java bean.
 */
public class NumericalRangeBuilder
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
     * Returns the lower bound to use.
     */
    public float getLowerBound()
    {
        return fLowerBound;
    }

    /**
     * Sets the lower bound to use.
     */
    public void setLowerBound(final float lowerBound)
    {
        fLowerBound = lowerBound;
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
     */
    public void setLowerInclusive(final boolean lowerInclusive)
    {
        fLowerInclusive = lowerInclusive;
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
     */
    public void setUpperBound(final float upperBound)
    {
        fUpperBound = upperBound;
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
     */
    public void setUpperInclusive(final boolean upperInclusive)
    {
        fUpperInclusive = upperInclusive;
    }
    
    /**
     * Builds the NumericalRange with the configured bounds.
     */
    public NumericalRange build()
    {
        return new NumericalRange(
                fLowerBound, fLowerInclusive, fUpperBound, fUpperInclusive);
    }
}
