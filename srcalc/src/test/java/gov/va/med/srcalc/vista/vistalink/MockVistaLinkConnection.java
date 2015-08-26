package gov.va.med.srcalc.vista.vistalink;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.cci.*;

import gov.va.med.exception.FoundationsException;
import gov.va.med.srcalc.domain.VistaLabs;
import gov.va.med.srcalc.vista.RemoteProcedure;
import gov.va.med.srcalc.vista.RpcContext;
import gov.va.med.srcalc.vista.vistalink.VistaLinkProcedureCaller;
import gov.va.med.vistalink.adapter.cci.VistaLinkConnection;
import gov.va.med.vistalink.adapter.record.*;
import gov.va.med.vistalink.adapter.spi.VistaLinkServerInfo;
import gov.va.med.vistalink.rpc.*;
import static org.mockito.Mockito.*;

/**
 * A Mock {@link VistaLinkConnection} for simulating VistA communication.
 */
public class MockVistaLinkConnection implements VistaLinkConnection
{
    /**
     * The name of the fake radiologist user returned from {@link RemoteProcedure#GET_USER_INFO}.
     */
    public final static String RADIOLOGIST_NAME = "RADIOLOGIST,ONE";
    
    /**
     * The DFN of the fake patient returned from {@link RemoteProcedure#GET_PATIENT}.
     */
    public final static String PATIENT_DFN = "18976";
    
    /**
     * The fake patient data returned from {@link RemoteProcedure#GET_PATIENT}.
     */
    public final static String PATIENT_DATA = "PATIENT,MOCKVL^62^M";
    
    /**
     * Sample SGOT lab data returned from {@link RemoteProcedure#GET_LABS}.
     */
    public final static String SGOT_LAB_DATA = "SGOT^21.3^02/02/2015@1435^U/L";
    
    /**
     * Sample Albumin lab data returned from {@link RemoteProcedure#GET_LABS}.
     */
    public final static String ALBUMIN_LAB_DATA = "";

    private int fTimeout = 0;
    
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
    
    @Override
    public RpcResponse executeRPC(final RpcRequest request)
            throws VistaLinkFaultException, FoundationsException
    {
        if (request.getRpcName().equals(RemoteProcedure.GET_USER_INFO.getProcedureName()) &&
                request.getRpcContext().equals(RpcContext.XUS_SIGNON.getContextName()))
        {
            // RpcResponse is very hard to simulate. Use Mockito.
            final RpcResponse response = mock(RpcResponse.class);
            when(response.getResults()).thenReturn(
                    "11716\n" + RADIOLOGIST_NAME + "\nRadiologist One MD\n" +
                    "100^CAMP MASTER^512\nPHYSICIAN\nMEDICAL\n");
            when(response.getResultsType())
                .thenReturn(VistaLinkProcedureCaller.VlType.array.name());
            return response;
        }
        else if (request.getRpcName().equals(RemoteProcedure.GET_PATIENT.getProcedureName()) &&
                request.getRpcContext().equals(RpcContext.SR_ASRC.getContextName()) &&
                request.getParams().getParam(1).equals(PATIENT_DFN))
        {
            // RpcResponse is very hard to simulate. Use Mockito.
            final RpcResponse response = mock(RpcResponse.class);
            when(response.getResults()).thenReturn(PATIENT_DATA + "\n");
            when(response.getResultsType())
                .thenReturn(VistaLinkProcedureCaller.VlType.array.name());
            return response;
        }
        else if (request.getRpcName().equals(RemoteProcedure.SAVE_PROGRESS_NOTE.getProcedureName()))
        {
            return makeStringResponse(RemoteProcedure.VALID_SIGNATURE_RETURN);
        }
        else if (request.getRpcName().equals(RemoteProcedure.SAVE_RISK.getProcedureName()))
        {
            return makeStringResponse(RemoteProcedure.RISK_SAVED_RETURN);
        }
        else if (request.getRpcName().equals(RemoteProcedure.GET_LABS.getProcedureName()) &&
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
