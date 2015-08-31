package gov.va.med.srcalc.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;

/**
 * Provides {@link SrcalcWebAuthnDetails} objects with details from {@link
 * HttpServletRequest}s.
 */
public class SrcalcWebAuthnDetailsSource
    implements AuthenticationDetailsSource<HttpServletRequest, SrcalcWebAuthnDetails>
{
    @Override
    public SrcalcWebAuthnDetails buildDetails(final HttpServletRequest context)
    {
        return new SrcalcWebAuthnDetails(context);
    }
}
