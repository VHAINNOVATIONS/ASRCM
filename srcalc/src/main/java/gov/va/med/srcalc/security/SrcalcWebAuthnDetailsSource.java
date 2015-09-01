package gov.va.med.srcalc.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;

import com.google.common.base.Strings;

/**
 * Provides {@link SrcalcWebAuthnDetails} objects with details from {@link
 * HttpServletRequest}s.
 */
public class SrcalcWebAuthnDetailsSource
    implements AuthenticationDetailsSource<HttpServletRequest, SrcalcWebAuthnDetails>
{
    /**
     * The servlet request parameter that should contain the division.
     */
    public static final String DIVISION_PARAM = "division";

    @Override
    public SrcalcWebAuthnDetails buildDetails(final HttpServletRequest context)
    {
        return new SrcalcWebAuthnDetails(
                context.getRemoteAddr(),
                extractDivision(context));
    }
    
    private String extractDivision(final HttpServletRequest request)
    {
        return Strings.nullToEmpty(request.getParameter(DIVISION_PARAM));
    }
}
