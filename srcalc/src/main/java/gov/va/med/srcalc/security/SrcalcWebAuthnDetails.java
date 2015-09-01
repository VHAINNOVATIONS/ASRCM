package gov.va.med.srcalc.security;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * Similar to {@link
 * org.springframework.security.web.authentication.WebAuthenticationDetails} but stores
 * srcalc-specific attributes.
 */
public class SrcalcWebAuthnDetails
{
    private final String fRemoteAddress;
    private final String fDivision;

    /**
     * Constructs an instance with the given attributes.
     * @param remoteAddress See {@link #getRemoteAddress()}. Must be non-empty.
     * @param division See {@link #getDivision()}. Must not be null.
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if remoteAddress is empty
     */
    public SrcalcWebAuthnDetails(final String remoteAddress, final String division)
    {
        if (remoteAddress.isEmpty())
        {
            throw new IllegalArgumentException("remoteAddress must be non-empty.");
        }
        fRemoteAddress = remoteAddress;
        fDivision = Preconditions.checkNotNull(division);
    }
    
    /**
     * Returns the Internet Protocol (IP) address of the client requesting authenticaiton.
     * @return a non-empty string
     */
    public String getRemoteAddress()
    {
        return fRemoteAddress;
    }
    
    /**
     * Returns the division value extracted from the HTTP request. Immutable.
     * @return never null, possibly empty
     */
    public String getDivision()
    {
        return fDivision;
    }
    
    /**
     * Returns a programmer-friendly String representation of this object. The format is
     * unspecified but will contain the remote address and division.
     */
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("remoteAddress", fRemoteAddress)
                .add("division", fDivision)
                .toString();
    }
}
