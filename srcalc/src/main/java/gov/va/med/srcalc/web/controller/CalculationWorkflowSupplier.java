package gov.va.med.srcalc.web.controller;

import gov.va.med.srcalc.domain.workflow.CalculationWorkflow;

import javax.servlet.http.HttpSession;

/**
 * A utility class that is designed to retrieve a {@link CalculationWorkflow}
 * from the currently active session.
 */
public class CalculationWorkflowSupplier 
{
	private static String SESSION_CALCULATION = "srcalc_calculation";
	
	/**
     * Returns the current CalculationWorkflow object form the HttpSession.
     * @return never null
     * @throws IllegalStateException if there is no current calculation
     */
    public static CalculationWorkflow getWorkflowFromSession(final HttpSession session)
    {
        final CalculationWorkflow workflow =
                (CalculationWorkflow)session.getAttribute(SESSION_CALCULATION);
        if (workflow == null)
        {
            throw new IllegalStateException("No current calculation.");
        }
        return workflow;
    }
}
