package gov.va.med.srcalc.vista;

import java.util.List;

public interface VistaProcedureCaller
{
    /**
     * Performs a Remote Procedure Call.
     * @param duz the calling user's DUZ
     * @param rpcName the name of the remote procedure
     * @param args the remote procedure arguments, if any
     * @return an unmodifiable list of String lines from the response
     * @throws IllegalArgumentException if the provided DUZ is invalid
     * @throws DataAccessException if some error occurred communicating with
     * VistA
     */
    public List<String> doRpc(final String duz, final String rpcName, final String... args);
    
    /**
     * Returns the division identifier (including any suffix) for the target
     * VistA.
     */
    public String getDivision();
}
