package gov.va.med.srcalc.vista.vistalink;

import java.util.List;
import java.util.Objects;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.cci.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import gov.va.med.crypto.VistaKernelHash;
import gov.va.med.exception.FoundationsException;
import gov.va.med.srcalc.domain.VistaLabs;
import gov.va.med.srcalc.vista.RemoteProcedure;
import gov.va.med.srcalc.vista.RpcContext;
import gov.va.med.srcalc.vista.vistalink.VistaLinkProcedureCaller;
import gov.va.med.vistalink.adapter.cci.VistaLinkAppProxyConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnectionSpec;
import gov.va.med.vistalink.adapter.cci.VistaLinkDuzConnectionSpec;
import gov.va.med.vistalink.adapter.record.*;
import gov.va.med.vistalink.adapter.spi.VistaLinkServerInfo;
import gov.va.med.vistalink.rpc.*;
import gov.va.med.vistalink.security.m.SecurityAccessVerifyCodePairInvalidException;
import gov.va.med.vistalink.security.m.SecurityIdentityDeterminationFaultException;
import gov.va.med.vistalink.security.m.SecurityUserAuthorizationException;
import static org.mockito.Mockito.*;

/**
 * A Mock {@link VistaLinkConnection} for simulating VistA communication.
 */
public class MockVistaLinkConnection implements VistaLinkConnection
{
    /**
     * The DUZ of the fake radiologist user. Only this DUZ is supported by this mock
     * connection.
     */
    public final static String RADIOLOGIST_DUZ = "11716";
    
    /**
     * The access code of the fake radiologist user.
     */
    public final static String RADIOLOGIST_ACCESS_CODE = "radio";
    
    /**
     * The verify code of the fake radiologist user.
     */
    public final static String RADIOLOGIST_VERIFY_CODE = "logist";
    
    /**
     * The name of the fake radiologist user returned from {@link RemoteProcedure#XUS_GET_USER_INFO}.
     */
    public final static String RADIOLOGIST_NAME = "RADIOLOGIST,ONE";
    
    /**
     * The full name (e.g., from the VistA NAME COMPONENTS file) for the fake radiologist
     * user.
     */
    public final static String RADIOLOGIST_FULL_NAME = "Radiologist One MD";
    
    /**
     * The person class of the fake radiologist user.
     */
    public final static String RADIOLOGIST_PROVIDER_TYPE =
            "Podiatric Medicine and Surgery Service Providers";
    
    /**
     * The DUZ for a fake disabled user.
     */
    public final static String DISABLED_DUZ = "12345";
    
    /**
     * The DUZ for a fake application proxy user.
     */
    public final static String PROXY_DUZ = "10000000218";
    
    /**
     * A fake CCOW token for mock CCOW authentication. Supplying this token to
     * {@link RemoteProcedure#XUS_KAAJEE_GET_USER_VIA_PROXY} will return the fake radiologist
     * user.
     */
    public final static String CCOW_TOKEN = "~2xwb123-3423";
    
    /**
     * The DFN of the fake patient returned from {@link RemoteProcedure#SR_ASRC_GET_PATIENT}.
     */
    public final static String PATIENT_DFN = "18976";
    
    /**
     * The fake patient data returned from {@link RemoteProcedure#SR_ASRC_GET_PATIENT}.
     */
    public final static String PATIENT_DATA = "PATIENT,MOCKVL^62^M";
    
    /**
     * Sample SGOT lab data returned from {@link RemoteProcedure#SR_ASRC_LAB_RESULTS}.
     */
    public final static String SGOT_LAB_DATA = "SGOT^21.3^02/02/2015@1435^U/L";
    
    /**
     * Sample Albumin lab data returned from {@link RemoteProcedure#SR_ASRC_LAB_RESULTS}.
     */
    public final static String ALBUMIN_LAB_DATA = "";
    
    private static final Logger LOGGER =
            LoggerFactory.getLogger(MockVistaLinkConnection.class);

    private final VistaLinkConnectionSpec fConnectionSpec;

    private int fTimeout = 0;
    
    public MockVistaLinkConnection(final VistaLinkConnectionSpec connectionSpec)
    {
        fConnectionSpec = connectionSpec;
    }
    
    @Override
    public Interaction createInteraction() throws ResourceException
    {
        throw new NotSupportedException(
                // sic
                "VistALink does not implement Iteraction and Record part of CCI " +
                "interface.  VistaRequest interface is used instead.");
    }
    
    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException
    {
        throw new NotSupportedException("VistALink does not support local transactions.");
    }
    
    @Override
    public ConnectionMetaData getMetaData() throws ResourceException
    {
        throw new ResourceException(
                "Unsure what VistALink returns for this - it is undocumented.");
    }
    
    @Override
    public ResultSetInfo getResultSetInfo() throws ResourceException
    {
        throw new NotSupportedException("VistALink does not support ResultSet functionality.");
    }
    
    @Override
    public void close() throws ResourceException
    {
        // No-op
    }
    
    @Override
    public VistaLinkResponseVO executeInteraction(VistaLinkRequestVO arg0, VistaLinkResponseFactory arg1)
            throws VistaLinkFaultException, FoundationsException
    {
        // Unused. This is not a public API.
        return null;
    }
    
    /**
     * Returns an RpcResponse with the given string result. (The class does not
     * provide a constructor to do this.)
     * @return a mock object
     */
    private RpcResponse makeStringResponse(final String result)
    {
        // RpcResponse is very hard to simulate. Use Mockito.
        final RpcResponse response = mock(RpcResponse.class);
        when(response.getResults()).thenReturn(result);
        // This one returns a single value.
        when(response.getResultsType())
            .thenReturn(VistaLinkProcedureCaller.VlType.string.name());
        return response;
    }
    
    /**
     * Returns an RpcResponse with the given array result. (The class does not provide a
     * constructor to do this.)
     * @return a mock object
     */
    private RpcResponse makeArrayResponse(final List<String> resultArray)
    {
        final RpcResponse response = mock(RpcResponse.class);
        // VistaLink (or VistA?) always has a trailing newline.
        final String resultString = Joiner.on('\n').join(resultArray)+ "\n";
        when(response.getResults()).thenReturn(resultString);
        when(response.getResultsType())
            .thenReturn(VistaLinkProcedureCaller.VlType.array.name());
        return response;
    }
    
    /**
     * Extracts the DUZ from the configured {@link VistaLinkConnectionSpec}. Throws an
     * appropriate Exception if the connection spec is invalid.
     */
    private String authenticate() throws
            SecurityIdentityDeterminationFaultException,
            SecurityAccessVerifyCodePairInvalidException,
            SecurityUserAuthorizationException
    {
        LOGGER.debug("Performing mock authentication with {}", fConnectionSpec);

        if (fConnectionSpec instanceof VistaLinkDuzConnectionSpec)
        {
            final VistaLinkDuzConnectionSpec duzConnectionSpec =
                    (VistaLinkDuzConnectionSpec)fConnectionSpec;

            if (Objects.equals(duzConnectionSpec.getDuz(), RADIOLOGIST_DUZ))
            {
                return duzConnectionSpec.getDuz();
            }
            else if (Objects.equals(duzConnectionSpec.getDuz(), DISABLED_DUZ))
            {
                throw new SecurityUserAuthorizationException(
                        new VistaLinkFaultException("disabled user"));
            }
            else
            {
                throw new SecurityIdentityDeterminationFaultException(
                        // Strange constructor.
                        new VistaLinkFaultException("Not a valid DUZ"));
            }
            
        }
        // Use getSecurityType() instead of the class type because
        // AccessVerifyConnectionSpec is just our own implementation: there could be other
        // ConnectionSpec implementations that use A/V codes (e.g., KAAJEE's
        // KaajeeVistaLinkConnectionSpec).
        else if (fConnectionSpec.getSecurityType().equals(
                AccessVerifyConnectionSpec.SECURITY_TYPE))
        {
            final String plaintextComponents = VistaKernelHash.decrypt(
                    // The VistaLink docs say this is a String.
                    (String)fConnectionSpec.getProprietarySecurityInfo().get(0));
            if (plaintextComponents.startsWith(
                    RADIOLOGIST_ACCESS_CODE + ";" + RADIOLOGIST_VERIFY_CODE + ";"))
            {
                return RADIOLOGIST_DUZ;
            }
            else
            {
                throw new SecurityAccessVerifyCodePairInvalidException(
                        new VistaLinkFaultException("invalid access/verify code"));
            }
        }
        else if (fConnectionSpec instanceof VistaLinkAppProxyConnectionSpec)
        {
            final VistaLinkAppProxyConnectionSpec appProxyCs =
                    (VistaLinkAppProxyConnectionSpec)fConnectionSpec;
            if (Objects.equals(
                    appProxyCs.getAppProxyName(),
                    VistaLinkAuthenticator.APPLICATON_PROXY_USER))
            {
                LOGGER.info("Detected application proxy user.");
                return PROXY_DUZ;
            }
            else
            {
                throw new SecurityIdentityDeterminationFaultException(
                        new VistaLinkFaultException("Bad application proxy user"));
            }
        }
        else
        {
            throw new SecurityIdentityDeterminationFaultException(
                    new VistaLinkFaultException("Unsupported reauthentication method."));
        }
    }
    
    @Override
    public RpcResponse executeRPC(final RpcRequest request)
            throws VistaLinkFaultException, FoundationsException
    {
        final String duz = authenticate();
        
        if (request.getRpcName().equals(RemoteProcedure.XUS_GET_USER_INFO.getProcedureName()) &&
                request.getRpcContext().equals(RpcContext.XUS_SIGNON.getContextName()))
        {
            return makeArrayResponse(ImmutableList.of(
                    duz,
                    RADIOLOGIST_NAME,
                    RADIOLOGIST_FULL_NAME,
                    "100^CAMP MASTER^512",
                    "PHYSICIAN",
                    "MEDICAL"));
        }
        else if (duz.equals(PROXY_DUZ) &&
                request.getRpcName().equals(RemoteProcedure.XUS_KAAJEE_GET_USER_VIA_PROXY.getProcedureName()))
        {
            final String decryptedToken = VistaKernelHash.decrypt(
                    (String)request.getParams().getParam(3));
            if (decryptedToken.equals("~~TOK~~" + CCOW_TOKEN))
            {
                return makeArrayResponse(ImmutableList.of(
                        RADIOLOGIST_DUZ,
                        RADIOLOGIST_NAME,
                        RADIOLOGIST_FULL_NAME));
            }
            else
            {
                return makeArrayResponse(ImmutableList.of(
                        RemoteProcedure.BAD_TOKEN_DUZ,
                        "unk",
                        "unk"));
            }
        }
        else if (request.getRpcName().equals(RemoteProcedure.SR_ASRC_PERSON_CLASSES.getProcedureName()) &&
                duz.equals(RADIOLOGIST_DUZ))
        {
            return makeArrayResponse(ImmutableList.of(RADIOLOGIST_PROVIDER_TYPE));
        }
        else if (request.getRpcName().equals(RemoteProcedure.SR_ASRC_GET_PATIENT.getProcedureName()) &&
                request.getRpcContext().equals(RpcContext.SR_ASRC.getContextName()) &&
                request.getParams().getParam(1).equals(PATIENT_DFN))
        {
            return makeArrayResponse(ImmutableList.of(PATIENT_DATA));
        }
        else if (request.getRpcName().equals(RemoteProcedure.SR_ASRC_SAVE_PROGRESS_NOTE.getProcedureName()))
        {
            return makeStringResponse(RemoteProcedure.VALID_SIGNATURE_RETURN);
        }
        else if (request.getRpcName().equals(RemoteProcedure.SR_ASRC_RISK_SAVE.getProcedureName()))
        {
            return makeStringResponse(RemoteProcedure.RISK_SAVED_RETURN);
        }
        else if (request.getRpcName().equals(RemoteProcedure.SR_ASRC_LAB_RESULTS.getProcedureName()) &&
                request.getParams().getParam(1).equals(PATIENT_DFN))
        {
            if (request.getParams().getParam(2).equals(VistaLabs.SGOT.getPossibleLabNames()))
            {
                return makeStringResponse(SGOT_LAB_DATA);
            }
            else
            {
                return makeStringResponse("");
            }
        }
        else
        {
            throw new VistaLinkFaultException("RPC not supported");
        }
    }
    
    @Override
    public VistaLinkServerInfo getConnectionInfo()
    {
        // This method is hard to implement because VistaLinkServerInfo doesn't have a
        // public constructor.
        throw new UnsupportedOperationException("Unimplemented method.");
    }
    
    @Override
    public int getTimeOut()
    {
        return fTimeout;
    }
    
    @Override
    public void setTimeOut(int timeout)
    {
        fTimeout = timeout;
    }
    
}
