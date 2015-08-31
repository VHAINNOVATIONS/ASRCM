package gov.va.med.srcalc.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.google.common.base.Strings;

/**
 * Subclass of WebAuthenticationDetails providing extra attributes.
 */
public class SrcalcWebAuthnDetails extends WebAuthenticationDetails
{
    /**
     * The HTTP request parameter that should contain the division.
     */
    public static final String DIVISION_PARAM = "division";
    
    /**
     * Change this when changing the class.
     */
    private static final long serialVersionUID = 1L;
    
    private final String fDivision;

    /**
     * Constructs an instance, extracting the details from the given HTTP request.
     */
    public SrcalcWebAuthnDetails(final HttpServletRequest request)
    {
        super(request);
        
        fDivision = Strings.nullToEmpty(request.getParameter(DIVISION_PARAM));
    }
    
    /**
     * Returns the division value extracted from the HTTP request.
     * @return never null, possibly empty
     */
    public String getDivision()
    {
        return fDivision;
    }
}
