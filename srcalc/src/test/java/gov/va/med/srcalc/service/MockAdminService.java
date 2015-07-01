package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.model.AbstractVariable;
import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.model.SampleModels;
import gov.va.med.srcalc.domain.model.Specialty;

import java.util.List;

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
}
