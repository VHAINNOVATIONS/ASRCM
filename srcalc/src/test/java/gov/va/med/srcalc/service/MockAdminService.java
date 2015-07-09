package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.model.*;

import java.util.List;
import java.util.Set;

/*
 * Created to support the EditRiskModel which needs to call getAllSpecialties()
 * Implement other methods as needed.
 * 
 */
public class MockAdminService extends MockModelService implements AdminService
{
    public MockAdminService() 
    {        
    }
    
    /**
     * Not Implemented.
     */
    @Override
    public List<AbstractVariable> getAllVariables()
    {
        return null;
    }

    /**
     * Not Implemented.
     */
    @Override
    public AbstractVariable getVariable(String key)
            throws InvalidIdentifierException
    {
        return null;
    }

    /**
     * Not Implemented.
     */
    @Override
    public void saveVariable(AbstractVariable variable)
    {
        // not implemented        
    }

    /**
     * Not Implemented.
     */
    @Override
    public void saveRiskModel(RiskModel model)
    {
        // not implemented
    }

    @Override
    public List<Specialty> getAllSpecialties()
    {
        return SampleModels.specialtyList();
    }

    /**
     * Not Implemented.
     */
    @Override
    public Rule getRule(String displayName) throws InvalidIdentifierException
    {
        return null;
    }

    /**
     * Not Implemented.
     */
    @Override
    public void saveRule(Rule rule)
    {
        // not implemented
    }

    /**
     * Not Implemented.
     */
    @Override
    public void replaceAllProcedures(final Set<Procedure> newProcedures)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public Rule getRuleById(int id) throws InvalidIdentifierException
    {
        return null;
    }
}
