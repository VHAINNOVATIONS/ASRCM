package gov.va.med.srcalc.web.controller;

import gov.va.med.srcalc.domain.calculation.Calculation;

import javax.servlet.http.HttpSession;

/**
 * A utility class that is designed to retrieve a {@link Calculation}
 * from the currently active session.
 */
public class SrcalcSession
{
    private static String SESSION_CALCULATION = "srcalc_calculation";
    
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
}
