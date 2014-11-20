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
    }
    
    public ProcedureVariable(
            String displayName)
    {
        super(displayName);
    }
    
    @Transient // transient for now until we figure out how to do this association
    public List<Procedure> getProcedures()
    {
        return fProcedures;
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
     */
    @Transient // this is generated, not persistent
    public Map<String, Procedure> getProcedureMap()
    {
        return Collections.unmodifiableMap(fProcedureMap);
    }
    
    @Override
    public void accept(VariableVisitor visitor) throws Exception
    {
        visitor.visitProcedure(this);
    }
}
