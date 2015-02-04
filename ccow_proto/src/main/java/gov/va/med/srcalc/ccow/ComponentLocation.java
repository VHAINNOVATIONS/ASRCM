package gov.va.med.srcalc.ccow;

/**
 * Specifies a component location (i.e., the output of of the
 * ContextManagementRegistry.Locate method).
 */
public final class ComponentLocation
{
    private final String fUrl;
    private final String fParameters;
    private final String fSite;
    
    public ComponentLocation(final String url, final String parameters, final String site)
    {
        fUrl = url;
        fParameters = parameters;
        fSite = site;
    }

    public String getUrl()
    {
        return fUrl;
    }

    public String getParameters()
    {
        return fParameters;
    }

    public String getSite()
    {
        return fSite;
    }
    
    @Override
    public String toString()
    {
        return String.format(
                "ComponentLocation[url=%s,params=%s,site=%s]",
                getUrl(), getParameters(), getSite());
    }
}
