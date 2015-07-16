package gov.va.med.srcalc.service;

import java.util.List;

import gov.va.med.srcalc.domain.model.*;

import com.google.common.collect.*;

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
    public ImmutableList<AbstractVariable> getAllVariables()
    {
        // sampleVariableList() is not sorted. Sort it.
        final Ordering<Variable> sortOrder = Ordering.from(new DisplayNameComparator());
        return sortOrder.immutableSortedCopy(SampleModels.sampleVariableList());
    }
    
    @Override
    public AbstractVariable getVariable(final String key)
            throws InvalidIdentifierException
    {
        // Just brute-force it: there aren't many.
        for (AbstractVariable v : getAllVariables())
        {
            if (v.getKey().equals(key))
            {
                return v;
            }
        }
        
        throw new InvalidIdentifierException(
                "MockModelService doesn't have that variable key");
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
    
    @Override
    public ImmutableList<Procedure> getAllProcedures()
    {
        return SampleModels.procedureList();
    }

    @Override
    public ImmutableCollection<Rule> getAllRules()
    {
        return ImmutableList.of(SampleModels.ageAndFsRule());
    }
    
    @Override
    public Rule getRule(final String displayName) throws InvalidIdentifierException
    {
        // As getVariable() above, just brute-force it.
        for (final Rule rule : getAllRules())
        {
            if (rule.getDisplayName().equals(displayName))
            {
                return rule;
            }
        }
        
        throw new InvalidIdentifierException("MockModelService doesn't have that rule");
    }
    
    @Override
    public RiskModel getRiskModelForId(int modelId)
    {
        
        if (fThoracicModel.getId() == modelId)
        {
            return fThoracicModel;
        }
        else
        {
            return null;
        }
    }

    @Override
    public List<Specialty> getAllSpecialties()
    {
        return SampleModels.specialtyList();
    }

    @Override
    public Specialty getSpecialtyForId(int id)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
