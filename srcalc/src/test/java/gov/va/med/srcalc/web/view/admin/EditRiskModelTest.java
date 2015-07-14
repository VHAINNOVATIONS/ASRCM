package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import gov.va.med.srcalc.service.MockModelService;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.service.InvalidIdentifierException;
import gov.va.med.srcalc.util.DisplayNameConditions;

import com.google.common.collect.ImmutableList;

import org.junit.Test;

public class EditRiskModelTest
{
    private final MockModelService fModelService = new MockModelService();
    
    private final Rule testRule = SampleModels.ageAndFsRule();
    
    /**
     * Constructs a sample RiskModel for our testing.
     * @param name
     * @return
     */
    private RiskModel createRiskModel(String name)
    {
        final ProcedureVariable procedureVar = SampleModels.procedureVariable();
        final NumericalVariable ageVar = SampleModels.ageVariable();
        final DiscreteNumericalVariable wbcVar = SampleModels.wbcVariable();
        final MultiSelectVariable fsVar = SampleModels.functionalStatusVariable();
        // Note: intentionally including this edge-case variable to test ordering.
        final BooleanVariable wbcIsNormalVar = SampleModels.wbcIsNormalVariable();
        final Set<DerivedTerm> derivedTerms = new HashSet<DerivedTerm>();
        derivedTerms.add(new DerivedTerm(6.0f, testRule));
        
        return SampleModels.makeSampleRiskModel(
                name,
                derivedTerms,
                procedureVar, wbcIsNormalVar, ageVar, wbcVar, fsVar);
    }
    
    @Test
    public final void testApplyChanges() throws InvalidIdentifierException
    {
        final RiskModel riskModel = createRiskModel("Thoracic 30-day Mortality");
        final EditRiskModel editRiskModel = EditRiskModel.fromRiskModel(riskModel, fModelService);
        final ImmutableList<EditModelTerm> newTerms = ImmutableList.of(
                EditModelTerm.forConstant(0.23f),
                EditModelTerm.forVariable("dnr", 12.3f),
                EditModelTerm.forRule(SampleModels.ageAndFsRule().getDisplayName(), 0.5f));
        final HashSet<ModelTerm> expectedTerms = new HashSet<>();
        for (final EditModelTerm newTerm : newTerms)
        {
            expectedTerms.add(newTerm.build(fModelService));
        }
        
        editRiskModel.setModelName("NewModelName");
        editRiskModel.getTerms().clear();
        editRiskModel.getTerms().addAll(newTerms);
        
        assertEquals("NewModelName", editRiskModel.getModelName());
        assertEquals(  DisplayNameConditions.DISPLAY_NAME_MAX, editRiskModel.getMaxDisplayNameLength() );
        
        editRiskModel.applyChanges(riskModel, fModelService);
        
        assertEquals("NewModelName", riskModel.getDisplayName());
        assertEquals(expectedTerms, riskModel.getTerms());
    }

    @Test
    public final void testGetTermSummaries()
    {
        /* Setup */
        final RiskModel riskModel = createRiskModel("Thoracic 30-day Mortality");
        final float constant = 0.34f;
        riskModel.setConstantTerm(new ConstantTerm(constant));
        final EditRiskModel editRiskModel = EditRiskModel.fromRiskModel(riskModel, fModelService);
        // Constant first, then the Rules and then the rest
        final ImmutableList<ModelTermSummary> expectedSummaries = ImmutableList.of(
                new ModelTermSummary("Constant", "", constant),
                new ModelTermSummary(testRule.getDisplayName(), "Rule", 6.0f),
                new ModelTermSummary("Age", "Numerical", 2.0f),
                new ModelTermSummary("Functional Status = Independent", "Multi-Select", 3.0f),
                new ModelTermSummary("Functional Status = Partially dependent", "Multi-Select", 3.0f ),
                new ModelTermSummary("Functional Status = Totally dependent", "Multi-Select", 3.0f ),
                new ModelTermSummary("Procedure", "Procedure (RVU Multiplier)", 1.0f),
                new ModelTermSummary("White Blood Count = WNL", "Discrete Numerical", 4.0f ),
                new ModelTermSummary("White Blood Count = >11.0", "Discrete Numerical", 4.0f ),
                new ModelTermSummary("White Blood Count is Normal", "Boolean", 5.0f ));
        
        /* Behavior */
        ImmutableList<ModelTermSummary> actualSummaries =
                editRiskModel.makeTermSummaries(fModelService);
        
        /* Verification */
        assertEquals(expectedSummaries, actualSummaries);
    }

}
