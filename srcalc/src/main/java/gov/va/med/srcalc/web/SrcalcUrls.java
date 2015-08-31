package gov.va.med.srcalc.web;

/**
 * <p>Provides a single point of definition for URLs to various resources. URLs used in
 * multiple places in the application should be defined here to stay DRY (Don't Repeat
 * Yourself), but URLs only used once need not be.</p>
 * 
 * <p>URLs are provided as constants for Java code and via accessor methods for easy use
 * in views.</p>
 * 
 * <p>Unless otherwise specified, all URLs should be absolute URLs within the application,
 * e.g. "/admin/editVariable"</p>
 */
public class SrcalcUrls
{
    /**
     * The URL to the default page (i.e., if not launched from CPRS).
     */
    public static final String DEFAULT_PAGE = "/";
    
    // Note: even though it's against Java conventions, we intersperse constants and
    // methods in this class to keep the constants next to their associated methods.
    
    /**
     * Returns {@link #DEFAULT_PAGE}.
     */
    public String getDefaultPage()
    {
        return DEFAULT_PAGE;
    }
    
    /**
     * The URL to the VistA user login form.
     */
    public static final String VISTA_LOGIN_FORM = "/login";
    
    /**
     * Returns {@link #VISTA_LOGIN_FORM}.
     */
    public String getVistaLoginForm()
    {
        return VISTA_LOGIN_FORM;
    }
    
    /**
     * The URL to the Spring Security-provided login verification for non-administrator
     * users.
     */
    public static final String LOGIN_TARGET = "/checkLogin";
    
    /**
     * Returns {@link #LOGIN_TARGET}.
     */
    public String getLoginTarget()
    {
        return LOGIN_TARGET;
    }
    
    /**
     * The URL to the page informing the user of a session timeout.
     */
    public static final String SESSION_TIMEOUT_PAGE = "/sessionTimeout";
    
    /**
     * Returns {@link #SESSION_TIMEOUT_PAGE}.
     */
    public String getSessionTimeoutPage()
    {
        return SESSION_TIMEOUT_PAGE;
    }
    
    /**
     * The base URL for all Administrative resources.
     */
    public static final String ADMIN_BASE = "/admin/";
    
    /**
     * The suffix to append to {@link #ADMIN_BASE} to make {@link #ADMIN_LOGIN_FORM}.
     */
    public static final String ADMIN_LOGIN_FORM_SUFFIX = "login";
    
    /**
     * The URL to the Administrator login page.
     */
    public static final String ADMIN_LOGIN_FORM = ADMIN_BASE + ADMIN_LOGIN_FORM_SUFFIX;
    
    /**
     * Returns {@link #ADMIN_LOGIN_FORM}.
     */
    public String getAdminLoginForm()
    {
        return ADMIN_LOGIN_FORM;
    }
    
    /**
     * The URL to the Spring Security-provided login verification for administrators.
     */
    public static final String ADMIN_LOGIN_TARGET = ADMIN_BASE + "checkLogin";
    
    /**
     * Returns {@link #ADMIN_LOGIN_TARGET}.
     */
    public String getAdminLoginTarget()
    {
        return ADMIN_LOGIN_TARGET;
    }
    
    /**
     * The URL to the Administration home page.
     */
    public static final String ADMIN_HOME = ADMIN_BASE;
    
    /**
     * Returns {@link #ADMIN_HOME}.
     */
    public String getAdminHome()
    {
        return ADMIN_HOME;
    }
    
    /**
     * The suffix to append to {@link #ADMIN_BASE} to make {@link #MODEL_ADMIN_HOME}.
     */
    public static final String MODEL_ADMIN_HOME_SUFFIX = "modelHome";
    
    /**
     * The URL of the Model Administration home page.
     */
    public static final String MODEL_ADMIN_HOME = ADMIN_BASE + MODEL_ADMIN_HOME_SUFFIX;
    
    /**
     * Returns {@link #MODEL_ADMIN_HOME}.
     */
    public String getModelAdminHome()
    {
        return MODEL_ADMIN_HOME;
    }
    
    /**
     * The URL of the Summary Report.
     */
    public static final String SUMMARY_REPORT = ADMIN_BASE + "reports/summary";
    
    /**
     * Returns {@link #SUMMARY_REPORT}.
     */
    public String getSummaryReport()
    {
        return SUMMARY_REPORT;
    }
    
    /**
     * The URL of the Utilization Report.
     */
    public static final String UTILIZATION_REPORT = ADMIN_BASE + "reports/utilization";
    
    /**
     * Returns {@link #UTILIZATION_REPORT}.
     */
    public String getUtilizationReport()
    {
        return UTILIZATION_REPORT;
    }
}
