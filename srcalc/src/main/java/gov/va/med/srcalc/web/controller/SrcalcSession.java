package gov.va.med.srcalc.web.controller;

import gov.va.med.srcalc.domain.calculation.Calculation;
import gov.va.med.srcalc.domain.calculation.CalculationResult;

import javax.servlet.http.HttpSession;

import com.google.common.base.Optional;

/**
 * A utility class that is designed to retrieve a {@link Calculation}
 * from the currently active session.
 */
public class SrcalcSession
{
    private static String SESSION_CALCULATION = "srcalc_calculation";
    
    private static String SESSION_LAST_RESULT = "srcalc_last_result";
    
    /**
     * Returns the current Calculation from the session.
     * @param session the current session
     * @return never null
     * @throws IllegalStateException if there is no current calculation
     */
    public static Calculation getCalculation(final HttpSession session)
    {
        final Calculation calculation = (Calculation)session.getAttribute(SESSION_CALCULATION);
        if (calculation == null)
        {
            throw new IllegalStateException("No current calculation.");
        }
        return calculation;
    }
    
    /**
     * Stores the given Calculation into the session for use in other requests.
     * @param session the current session
     * @param calculation the calculation to store
     * @throws IllegalStateException if the given Session has been invalidated
     */
    public static void setCalculation(
            final HttpSession session, final Calculation calculation)
    {
        session.setAttribute(SESSION_CALCULATION, calculation);
    }
    
    /**
     * Returns the last {@link CalculationResult} stored in the Session, if one
     * exists.
     * @param session the current session
     * @return an Optional
     */
    public static Optional<CalculationResult> getOptionalLastResult(
            final HttpSession session)
    {
        return Optional.fromNullable(
                (CalculationResult)session.getAttribute(SESSION_LAST_RESULT));
    }
    
    /**
     * Returns the last {@link CalculationResult} stored in the Session, assuming
     * it exists.
     * @param session the current session
     * @return the CalculationResult, never null
     * @throws IllegalStateException if no result exists in the session
     */
    public static CalculationResult getRequiredLastResult(final HttpSession session)
    {
        try
        {
            return getOptionalLastResult(session).get();
        }
        catch (final IllegalStateException ex)
        {
            throw new IllegalStateException(
                    "There is no calculation result in this session.", ex);
        }
    }
    
    /**
     * Stores the result of the last-run calculation in the session for use on
     * other pages.
     * @param session the current session
     * @param result the result of the last-run calculation
     */
    public static void setLastResult(
            final HttpSession session, final CalculationResult result)
    {
        session.setAttribute(SESSION_LAST_RESULT, result);
    }
}
