package gov.va.med.srcalc.web.view;

/**
 * <p>Defines available views. See /WEB-INF/views.</p>
 * 
 * <p>All references in code to View names should be through this class to
 * enhance code clarity and facilitate refactoring.</p>
 */
public class Views
{
    /**
     * No construction.
     */
    private Views()
    {
    }
    
    /* Shared and Calculation Views */
    public static final String LAUNCH_FROM_CPRS = "launchFromCprs.jsp";
    public static final String SESSION_TIMEOUT = "sessionTimeout.jsp";
    public static final String SELECT_SPECIALTY = "selectSpecialty.jsp";
    public static final String ENTER_VARIABLES = "enterVariables.jsp";
    public static final String DISPLAY_RESULTS = "displayResults.jsp";
    public static final String SUCCESSFUL_SIGN = "successfulSign.jsp";
    public static final String CONFIRM_NEW_CALC = "confirmNewCalc.jsp";
    
    /* Administrative Views */
    public static final String ADMIN_HOME = "admin/home.jsp";
    public static final String MODEL_ADMIN_HOME = "admin/modelHome.jsp";
    public static final String EDIT_RISK_MODEL  = "admin/editRiskModel.jsp";
    public static final String EDIT_BOOLEAN_VARIABLE = "admin/editBooleanVariable.jsp";
    public static final String NEW_BOOLEAN_VARIABLE = "admin/newBooleanVariable.jsp";
    public static final String NEW_MULTI_SELECT_VARIABLE = "admin/newMultiSelectVariable.jsp";
    public static final String EDIT_MULTI_SELECT_VARIABLE = "admin/editMultiSelectVariable.jsp";
    public static final String NEW_DISCRETE_NUMERICAL_VARIABLE = "admin/newDiscreteNumericalVariable.jsp";
    public static final String EDIT_DISCRETE_NUMERICAL_VARIABLE = "admin/editDiscreteNumericalVariable.jsp";
    public static final String EDIT_RULE = "admin/editRule.jsp";
    public static final String EDIT_PROCEDURES = "admin/editProcedures.jsp";
    public static final String SUMMARY_REPORT_FORM = "admin/summaryReportForm.jsp";
    public static final String SUMMARY_REPORT_RESULTS = "admin/summaryReportResults.jsp";
    public static final String UTILIZATION_REPORT_FORM = "admin/utilizationReportForm.jsp";
    public static final String UTILIZATION_REPORT_RESULTS = "admin/utilizationReportResults.jsp";
}
