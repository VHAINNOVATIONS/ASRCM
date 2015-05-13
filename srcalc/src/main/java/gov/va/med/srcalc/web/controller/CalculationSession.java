package gov.va.med.srcalc.web.controller;

import java.io.Serializable;
import java.util.Objects;

import com.google.common.base.Optional;

import gov.va.med.srcalc.domain.calculation.Calculation;
import gov.va.med.srcalc.domain.calculation.CalculationResult;

/**
 * Encapsulates a session in which a user is running a calculation: both the
 * {@link Calculation} object and the latest {@link CalculationResult}.
 */
public class CalculationSession implements Serializable
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    private final Calculation fCalculation;
    private Optional<CalculationResult> fLastResult = Optional.absent();
    
    /**
     * Constructs an instance
     * @param calculation the associated Calculation. Must not be null.
     * @throws NullPointerException if the given calculation is null.
     */
    public CalculationSession(final Calculation calculation)
    {
        fCalculation = Objects.requireNonNull(calculation);
    }

    /**
     * Returns the associated Calculation.
     */
    public Calculation getCalculation()
    {
        return fCalculation;
    }

    /**
     * Returns the last CalculationResult if it has been set.
     * @return an Optional containing the last CalculationResult if it has been
     * set
     */
    public Optional<CalculationResult> getOptionalLastResult()
    {
        return fLastResult;
    }
    
    /**
     * Returns the last CalculationResult. Throws an exception if it has not
     * been set.
     * @return never null
     * @throws IllegalStateException if the calculation has not been set
     */
    public CalculationResult getRequiredLastResult()
    {
        try
        {
            return fLastResult.get();
        }
        catch (final IllegalStateException ex)
        {
            throw new IllegalStateException(
                    "There is no calculation result in this session.", ex);
        }
    }

    /**
     * Sets the last CalculationResult.
     */
    public void setLastResult(final CalculationResult lastResult)
    {
        fLastResult = Optional.of(lastResult);
    }
    
}
