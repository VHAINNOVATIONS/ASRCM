package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.model.*;

import java.util.List;
import java.util.Set;

/*
 * Created to support the EditRiskModel which needs to call getAllSpecialties()
 * Implement other methods as needed.
 * 
 */
public class MockAdminService extends MockModelService implements AdminService  {

	public MockAdminService() 
	{		
	}
	
	@Override
	public List<AbstractVariable> getAllVariables() {
		return null;
	}

	@Override
	public AbstractVariable getVariable(String key)
			throws InvalidIdentifierException {
		return null;
	}

	@Override
	public void saveVariable(AbstractVariable variable) {
		// not implemented		
	}

	@Override
	public void saveRiskModel(RiskModel model) {
		// not implemented
	}

	@Override
	public List<Specialty> getAllSpecialties() {
		return SampleModels.specialtyList();
	}

    @Override
    public Rule getRule(String displayName) throws InvalidIdentifierException
    {
        return null;
    }

    @Override
    public void saveRule(Rule rule)
    {
        // not implemented
    }

    @Override
    public void replaceAllProcedures(final Set<Procedure> newProcedures)
    {
        // TODO Auto-generated method stub
        
    }
}
