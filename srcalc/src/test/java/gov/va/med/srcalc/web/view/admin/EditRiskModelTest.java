package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gov.va.med.srcalc.domain.model.BooleanVariable;
import gov.va.med.srcalc.domain.model.DerivedTerm;
import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable;
import gov.va.med.srcalc.domain.model.MultiSelectVariable;
import gov.va.med.srcalc.domain.model.NumericalVariable;
import gov.va.med.srcalc.domain.model.ProcedureVariable;
import gov.va.med.srcalc.domain.model.RiskModel;
import gov.va.med.srcalc.domain.model.Rule;
import gov.va.med.srcalc.domain.model.SampleModels;
import gov.va.med.srcalc.domain.model.ValueMatcher;
import gov.va.med.srcalc.service.MockAdminService;
import gov.va.med.srcalc.web.view.admin.EditRiskModel.ModelTermSummary;
import gov.va.med.srcalc.util.DisplayNameConditions;

import org.junit.Test;

public class EditRiskModelTest {
    private final static MockAdminService fAdminService = new MockAdminService();
    
    /**
     * Create an EditRiskModel.
     * @param name
     * @return EditRiskModel
     */
    public static EditRiskModel createEditRiskModel( String name) 
    {
        final ProcedureVariable procedureVar = SampleModels.procedureVariable();
        final BooleanVariable dnrVar = SampleModels.dnrVariable();
        final NumericalVariable ageVar = SampleModels.ageVariable();
        final DiscreteNumericalVariable wbcVar = SampleModels.wbcVariable();
        final MultiSelectVariable fsVar = SampleModels.functionalStatusVariable();
        final Set<DerivedTerm> derivedTerms = new HashSet<DerivedTerm>();
        final List<ValueMatcher> valueMatchers = new ArrayList<ValueMatcher>();
        valueMatchers.add(new ValueMatcher(procedureVar, "#this.value.complexity == \"Standard\"", true) );
        derivedTerms.add(new DerivedTerm(6.0f, new Rule(valueMatchers, "#coefficient", true, "Test Rule")));

        // a bit contrived but this will test the sorting of the term summaries
        dnrVar.setDisplayName( wbcVar.getDisplayName()+" is Normal" );
        
        RiskModel model = SampleModels.makeSampleRiskModel(
                name,
                derivedTerms,
                procedureVar, dnrVar, ageVar, wbcVar, fsVar);
        
        return EditRiskModel.fromRiskModel( model, fAdminService );        
    }
    
    @Test
    public final void testEditRiskModel()
    {
        final EditRiskModel editRiskModel = createEditRiskModel( "Thoracic 30-day Mortality" );
        
        editRiskModel.setModelName( "NewModelName" );
        
        assertEquals( "NewModelName", editRiskModel.getModelName() );
        assertEquals(  DisplayNameConditions.DISPLAY_NAME_MAX, editRiskModel.getMaxDisplayNameLength() );
        
        editRiskModel.applyChanges();
        
        RiskModel rm = editRiskModel.getRiskModel();
        assertEquals( "NewModelName", rm.getDisplayName() );
    }

    @Test
    public final void testGetTermSummaries()
    {
        final EditRiskModel editRiskModel = createEditRiskModel( "Thoracic 30-day Mortality" );
        
        List<ModelTermSummary> termSummaries = editRiskModel.getTermSummaries();
        
        assertEquals( 10, termSummaries.size() );
        // Constant first, then the Rules and then the rest  
        assertEquals( "Constant", termSummaries.get(0).getDisplayName() );           
        assertEquals( "Rule: Test Rule", termSummaries.get(1).getDisplayName() );        
        assertEquals( "Age", termSummaries.get(2).getDisplayName() );
        assertEquals( "Functional Status = Independent", termSummaries.get(3).getDisplayName() );
        assertEquals( "Functional Status = Partially dependent", termSummaries.get(4).getDisplayName() );
        assertEquals( "Functional Status = Totally dependent", termSummaries.get(5).getDisplayName() );
        assertEquals( "Procedure (RVU Multiplier)", termSummaries.get(6).getDisplayName() );        
        assertEquals( "White Blood Count = WNL", termSummaries.get(7).getDisplayName() );
        assertEquals( "White Blood Count = >11.0", termSummaries.get(8).getDisplayName() );
        assertEquals( "White Blood Count is Normal", termSummaries.get(9).getDisplayName() );
    }

}
