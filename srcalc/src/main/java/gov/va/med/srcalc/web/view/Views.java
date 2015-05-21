package gov.va.med.srcalc.web.view;

/**
 * <p>Defines available views. See /WEB-INF/views.</p>
 * 
 * <p>All references in code to View names should be through this class to
 * enhance code clarity and facilitiate refactoring.</p>
 */
public class Views
{
    /**
     * No construction.
     */
    private Views()
    {
    }
    
    public static final String LAUNCH_FROM_CPRS = "launchFromCprs.jsp";
    public static final String SESSION_TIMEOUT = "sessionTimeout.jsp";
    public static final String SELECT_SPECIALTY = "selectSpecialty.jsp";
    public static final String ENTER_VARIABLES = "enterVariables.jsp";
    public static final String DISPLAY_RESULTS = "displayResults.jsp";
    public static final String MODEL_ADMIN_HOME = "adminHome.jsp";
    public static final String EDIT_BOOLEAN_VARIABLE = "editBooleanVariable.jsp";
    public static final String SUCCESSFUL_SIGN = "successfulSign.jsp";
    public static final String CONFIRM_NEW_CALC = "confirmNewCalc.jsp";
}
