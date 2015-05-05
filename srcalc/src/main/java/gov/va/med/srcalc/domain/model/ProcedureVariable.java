package gov.va.med.srcalc.domain.model;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class ProcedureVariable extends AbstractVariable
{
    private List<Procedure> fProcedures;
    private HashMap<String, Procedure> fProcedureMap;
    
    /**
     * For reflections-based construction only. Business code should use
     * {@link #ProcedureVariable(String, VariableGroup)}.
     */
    ProcedureVariable()
    {
        // Sentinel values to detect if procedures have not been loaded. See
        // getProcedures().
        fProcedures = null;
        fProcedureMap = null;
    }
    
    /**
     * Constructs an instance.
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if any argument is invalid
     * @see AbstractVariable#AbstractVariable(String, VariableGroup, String)
     */
    public ProcedureVariable(
            final String displayName, final VariableGroup group, final String key)
    {
        super(displayName, group, key);
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
    
    public void setProcedures(final List<Procedure> procedures)
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
    
    /**
     * Returns a {@link Value} object representing the given procedure
     * selection.
     * @param selectedProcedure
     */
    public ProcedureValue makeValue(final Procedure selectedProcedure)
    {
        return new ProcedureValue(this, selectedProcedure);
    }
    
    @Override
    public void accept(VariableVisitor visitor) throws Exception
    {
        visitor.visitProcedure(this);
    }
}
