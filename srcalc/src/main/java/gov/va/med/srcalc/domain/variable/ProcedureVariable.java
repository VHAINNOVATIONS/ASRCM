package gov.va.med.srcalc.domain.variable;

import gov.va.med.srcalc.domain.Procedure;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class ProcedureVariable extends Variable
{
    private List<Procedure> fProcedures;
    private HashMap<String, Procedure> fProcedureMap;
    
    public ProcedureVariable()
    {
        // Sentinel values to detect if procedures have not been loaded. See
        // getProcedures().
        fProcedures = null;
        fProcedureMap = null;
    }
    
    public ProcedureVariable(
            String displayName)
    {
        super(displayName);
    }
    
    /**
     * <p>Returns the List of all active Procedures. Note that this is not a member
     * collection of the ProcedureVariable, it is just for navigability.</p>
     * 
     * <p><strong>Warning:</strong> this collection is not automatically loaded
     * when the ProcedureVariable is loaded from the Database. It must be set
     * via {@link #setProcedures(List)} after loading.</p>
     * @return an unmodifiable list
     * @throws IllegalStateException if the procedure list has not been set
     */
    @Transient // see method Javadocs
    public List<Procedure> getProcedures()
    {
        if (fProcedures == null)
        {
            throw new IllegalStateException("Procedure list not set!");
        }
        return Collections.unmodifiableList(fProcedures);
    }
    
    public void setProcedures(List<Procedure> procedures)
    {
        fProcedures = procedures;
        
        // Create the procedure map.
        fProcedureMap = new HashMap<>();
        for (Procedure p : fProcedures)
        {
            fProcedureMap.put(p.getCptCode(), p);
        }
    }
    
    /**
     * Returns a Map of CPT Code to Procedure for convenience. Unmodifiable.
     * @throws IllegalStateException if the procedure list has not been set
     */
    @Transient // this is generated, not persistent
    public Map<String, Procedure> getProcedureMap()
    {
        if (fProcedureMap == null)
        {
            throw new IllegalStateException("Procedure list not set!");
        }
        return Collections.unmodifiableMap(fProcedureMap);
    }
    
    @Override
    public void accept(VariableVisitor visitor) throws Exception
    {
        visitor.visitProcedure(this);
    }
}
