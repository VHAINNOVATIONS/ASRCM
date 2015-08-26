package gov.va.med.srcalc.vista.vistalink;

import gov.va.med.crypto.VistaKernelHash;
import gov.va.med.crypto.VistaKernelHashCountLimitExceededException;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpecImpl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

/**
 * <p>A {@link VistaLinkConnectionSpec} for access/verify-code re-authentication.
 * Immutable except for the division attribute.</p>
 * 
 * <p>Based on KaajeeVistaLinkConnectionSpec provided by KAAJEE. (Why doesn't VistALink
 * provide this out of the box?)</p>
 */
public final class AccessVerifyConnectionSpec extends VistaLinkConnectionSpecImpl
{
    /**
     * Some String presumably meaning that this class performs access/verify
     * authentication. Not explicitly specified by VistALink, but used by
     * KaajeeVistaLinkConnectionSpec. (See class Javadoc.)
     */
    public static final String SECURITY_TYPE = "av";
    
    /**
     * The name of the Access/Verify code XML element.
     */
    private static final String ELEMENT_AV = "AccessVerify";
    
    private final String fAccessCode;
    
    private final String fClientIp;
    
    private final String fEncryptedComponents;
    
    private final HashCode fVerifyHash;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(
            AccessVerifyConnectionSpec.class);
    
    /**
     * Constructs an instance with the given properties.
     * 
     * @param division station # (external format) of the division to log the user in
     * against
     * @param accessCode user access code. Must not be null.
     * @param verifyCode user verify code. Must not be null.
     * @param clientIp the client's IP address for signon logging. Must not be null.
     */
    public AccessVerifyConnectionSpec(
            final String division,
            final String accessCode,
            final String verifyCode,
            final String clientIp)
    {
        super(division);
        fAccessCode = Objects.requireNonNull(accessCode);
        fClientIp = Objects.requireNonNull(clientIp);
        Objects.requireNonNull(verifyCode);
        final String components = accessCode + ";" + verifyCode + ";" + clientIp;
        // Note that we do not store the plaint-text verify code to avoid careless
        // observation in memory, but we do store a hash for equals() performance. Though
        // MD5 would probably be fine here, use SHA-256 since it is superior.
        fVerifyHash = Hashing.sha256().hashString(verifyCode, StandardCharsets.US_ASCII);

        try
        {
            // Note: contrary to the class name, the returned string is not a hash or
            // encrypted, but simply obfuscated by a reversible transformation.
            fEncryptedComponents = VistaKernelHash.encrypt(components, true);
        }
        catch (VistaKernelHashCountLimitExceededException e)
        {
            // At time of writing, VistaKernelHashCountLimitExceededException should be
            // so rare that its occurrence must be a data issue. So this exception is the
            // best we can do.
            throw new IllegalArgumentException("Could not encrypt access/verify code pair.");
        }
    }
    
    @Override
    public ArrayList<String> getProprietarySecurityInfo()
    {
        // I am unsure what the significance of this returned list is. I am simply
        // emulating the behavior of KaajeeVistaLinkConnectionSpec. - David Tombs, 25 Aug
        // 2015
        final ArrayList<String> values = new ArrayList<String>(1);
        values.add(fEncryptedComponents);
        return values;
    }
    
    /**
     * {@inheritDoc}
     * 
     * Returns {@link #SECURITY_TYPE}.
     */
    @Override
    public String getSecurityType()
    {
        return SECURITY_TYPE;
    }
    
    @Override
    public void setAuthenticationNodes(
            Document requestDoc,
            Node securityNode)
    {
        LOGGER.debug("Re-authentication type is '{}'", getSecurityType());
        
        // These methods are undocumented but they seem to work.
        setSecurityDivisionAttr(securityNode);
        setSecurityTypeAttr(securityNode);
        
        /* Add Access/Verify code element to the security node. */
        Element elemAV = requestDoc.createElement(ELEMENT_AV);
        CDATASection cdata = requestDoc.createCDATASection(this.fEncryptedComponents);
        Node currentAvCdataNode = elemAV.getFirstChild();
        if (currentAvCdataNode != null)
        {
            elemAV.removeChild(currentAvCdataNode);
        }
        elemAV.appendChild(cdata);
        
        securityNode.appendChild(elemAV);
        
    }
    
    @Override
    @Deprecated
    public boolean isConnSpecEqual(Object obj)
    {
        return equals(obj);
    }
    
    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof AccessVerifyConnectionSpec)
        {
            AccessVerifyConnectionSpec other = (AccessVerifyConnectionSpec)obj;
            return (Objects.equals(this.getDivision(), other.getDivision()) &&
                    Objects.equals(this.fAccessCode, other.fAccessCode) &&
                    Objects.equals(this.fVerifyHash, other.fVerifyHash) &&
                    Objects.equals(this.fClientIp, other.fClientIp));
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getDivision(), fAccessCode, fVerifyHash, fClientIp);
    }
    
    /**
     * Returns the client ip address provided to the constructor.
     */
    public String getClientIp()
    {
        return fClientIp;
    }
    
    /**
     * Returns the access code provided to the constructor.
     */
    public String getAccessCode()
    {
        return fAccessCode;
    }
}
