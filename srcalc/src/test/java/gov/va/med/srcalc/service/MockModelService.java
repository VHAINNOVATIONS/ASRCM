package gov.va.med.srcalc.service;

import gov.va.med.srcalc.domain.model.*;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

/**
 * A ModelInspectionService that returns sample objects.
 */
public class MockModelService implements ModelInspectionService
{
    private final RiskModel fThoracicModel;
    
    public MockModelService()
    {
        fThoracicModel = SampleModels.thoracicRiskModel();
    }
    
    @Override
    public ImmutableCollection<VariableGroup> getAllVariableGroups()
    {
        return SampleModels.variableGroups();
    }
    
    @Override
    public ImmutableCollection<RiskModel> getAllRiskModels()
    {
        return ImmutableList.of(
                fThoracicModel,
                // include an empty model to ensure the code can handle this
                new RiskModel("empty model"));
    }
    
    /**
     * Returns the Thoracic model included in {@link #getAllRiskModels()} for
     * reference.
     */
    public RiskModel getThoracicModel()
    {
        return fThoracicModel;
    }
}
