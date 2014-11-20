package gov.va.med.srcalc.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.va.med.srcalc.domain.Procedure;
import gov.va.med.srcalc.domain.variable.*;

/**
 * Loads procedures into a ProcedureVariable. A kludge until I figure out how
 * to get Hibernate to load all of the procedures.
 */
public class ProcedureLoaderVisitor implements VariableVisitor
{
    private static final Logger fLogger = LoggerFactory.getLogger(ProcedureLoaderVisitor.class);
    
    private final Session fSession;
    
    public ProcedureLoaderVisitor(final Session session)
    {
        fSession = session;
    }

    @Override
    public void visitNumerical(NumericalVariable variable) throws Exception
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
        final Query query = fSession.createQuery(
                "from Procedure p where p.active = true order by p.cptCode");
        @SuppressWarnings("unchecked") // trust hibernate
        final List<Procedure> procedures = query.list();
        variable.setProcedures(procedures);
        
        fLogger.info("Loaded all procedures.");
    }
    
}
