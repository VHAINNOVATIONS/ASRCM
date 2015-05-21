package gov.va.med.srcalc.web.controller;

import javax.servlet.http.HttpSession;

/**
 * A utility class to retrieve objects from the currently active session.
 */
public class SrcalcSession
{
    private static String CALCULATION_SESSION_ATTR = "srcalc_calculation_session";
    
    /**
     * Returns the current CalculationSession from the session.
     * @param session the current session
     * @return never null
     * @throws IllegalStateException if there is no current calculation
     */
    public static CalculationSession getCalculationSession(final HttpSession session)
    {
        final CalculationSession cs =
                (CalculationSession)session.getAttribute(CALCULATION_SESSION_ATTR);
        if (cs == null)
        {
            throw new IllegalStateException("No current calculation.");
        }
        return cs;
    }
    
    /**
     * Stores the given CalculationSession into the session for use in other
     * requests.
     * @param session the current session
     * @param cs the CalculationSession to store
     * @throws IllegalStateException if the given Session has been invalidated
     */
    public static void setCalculationSession(
            final HttpSession session, final CalculationSession cs)
    {
        session.setAttribute(CALCULATION_SESSION_ATTR, cs);
    }
    
    public static boolean hasCalculationSession(final HttpSession session)
    {
        final CalculationSession cs =
                (CalculationSession)session.getAttribute(CALCULATION_SESSION_ATTR);
        return cs != null;
    }
}
