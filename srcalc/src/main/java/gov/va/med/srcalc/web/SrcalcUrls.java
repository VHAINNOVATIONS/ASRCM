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
     * The base URL for all Administrative resources.
     */
    public static final String ADMIN_BASE = "/admin";
    
    /**
     * The URL to the Administration home page.
     */
    public static final String ADMIN_HOME = ADMIN_BASE;
    
    // Note: even though it's against Java conventions, we intersperse constants and
    // methods in this class to keep the constants next to their associated methods.
    
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
    public static final String MODEL_ADMIN_HOME_SUFFIX = "/modelHome";
    
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
    public static final String SUMMARY_REPORT = "/admin/reports/summary";
    
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
    public static final String UTILIZATION_REPORT = "/admin/reports/utilization";
    
    /**
     * Returns {@link #UTILIZATION_REPORT}.
     */
    public String getUtilizationReport()
    {
        return UTILIZATION_REPORT;
    }
}
