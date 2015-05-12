package gov.va.med.srcalc.vista.vistalink;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.cci.*;

import gov.va.med.exception.FoundationsException;
import gov.va.med.srcalc.vista.RemoteProcedure;
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
     * The name of the fake radiologist user returned from {@link RemoteProcedure#GET_USER}.
     */
    public final static String RADIOLOGIST_NAME = "RADIOLOGIST,ONE";
    
    /**
     * The DFN of the fake patient returned from {@link RemoteProcedure#GET_PATIENT}
     */
    public final static String PATIENT_DFN = "18976";
    
    /**
     * The fake patient data returned from {@link RemoteProcedure#GET_PATIENT}.
     */
    public final static String PATIENT_DATA = "PATIENT,MOCKVL^62^M";

    private int fTimeout = 0;
    
    @Override
    public Interaction createInteraction() throws ResourceException
    {
        throw new NotSupportedException(
                // sic
                "VistALink does not implement Iteraction and Record part of CCI interface.  VistaRequest interface is used instead.");
    }
    
    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException
    {
        throw new NotSupportedException("VistALink does not support local transactions.");
    }
    
    @Override
    public ConnectionMetaData getMetaData() throws ResourceException
    {
        // TODO Auto-generated method stub
        return null;
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
    
    @Override
    public RpcResponse executeRPC(RpcRequest request) throws VistaLinkFaultException, FoundationsException
    {
        if (request.getRpcName().equals(RemoteProcedure.GET_USER.getProcedureName()))
        {
            // RpcResponse is very hard to simulate. Use Mockito.
            RpcResponse response = mock(RpcResponse.class);
            when(response.getResults()).thenReturn(RADIOLOGIST_NAME + "\n");
            when(response.getResultsType())
                .thenReturn(VistaLinkProcedureCaller.VlType.array.name());
            return response;
        }
        else if (request.getRpcName().equals(RemoteProcedure.GET_PATIENT.getProcedureName()) &&
                request.getParams().getParam(1).equals(PATIENT_DFN))
        {
            // RpcResponse is very hard to simulate. Use Mockito.
            RpcResponse response = mock(RpcResponse.class);
            when(response.getResults()).thenReturn(PATIENT_DATA + "\n");
            when(response.getResultsType())
                .thenReturn(VistaLinkProcedureCaller.VlType.array.name());
            return response;
        }
        else if (request.getRpcName().equals(RemoteProcedure.SAVE_PROGRESS_NOTE.getProcedureName()))
        {
            // RpcResponse is very hard to simulate. Use Mockito.
            RpcResponse response = mock(RpcResponse.class);
            when(response.getResults()).thenReturn(RemoteProcedure.VALID_SIGNATURE_RETURN);
            // This one returns a single value.
            when(response.getResultsType())
                .thenReturn(VistaLinkProcedureCaller.VlType.string.name());
            return response;
        }
        else
        {
            throw new VistaLinkFaultException("RPC not supported");
        }
    }
    
    @Override
    public VistaLinkServerInfo getConnectionInfo()
    {
        // TODO Auto-generated method stub
        return null;
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
