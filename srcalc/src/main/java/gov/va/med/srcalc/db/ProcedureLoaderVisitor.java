package gov.va.med.srcalc.db;

import gov.va.med.srcalc.domain.variable.*;

/**
 * Loads procedures into a ProcedureVariable. A kludge until I figure out how
 * to get Hibernate to load all of the procedures.
 */
public class ProcedureLoaderVisitor implements VariableVisitor
{
    private final ProcedureDao fDao;
    
    public ProcedureLoaderVisitor(final ProcedureDao session)
    {
        fDao = session;
    }

    @Override
    public void visitNumerical(NumericalVariable variable) throws Exception
    {
        // No action needed.
    }
    
    @Override
    public void visitBoolean(BooleanVariable variable) throws Exception
    {
        // No action needed.
    }
    
    @Override
    public void visitMultiSelect(MultiSelectVariable variable) throws Exception
    {
        // No action needed.
    }
    
    @Override
    public void visitProcedure(ProcedureVariable variable) throws Exception
    {
        variable.setProcedures(fDao.getActiveProcedures());
    }
    
    @Override
    public void visitDiscreteNumerical(DiscreteNumericalVariable variable) throws Exception
    {
        // No action needed.
    }
    
}
