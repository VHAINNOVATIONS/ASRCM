package gov.va.med.srcalc.web.view.admin;

import static org.junit.Assert.assertEquals;

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
import gov.va.med.srcalc.util.DisplayNameConditions;

import org.junit.Test;

public class EditRiskModelTest {

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
        RiskModel model = SampleModels.makeSampleRiskModel(
                name,
                derivedTerms,
                procedureVar, dnrVar, ageVar, wbcVar, fsVar);

        return EditRiskModel.fromRiskModel( model );        
    }
    
    @Test
    public final void testEditRiskModel()
    {
    	final EditRiskModel editRiskModel = createEditRiskModel( "Thoracic 30-day Mortality" );
        
    	editRiskModel.setModelName( "NewModelName" );
    	
        assertEquals( editRiskModel.getModelName(), "NewModelName" );
        assertEquals( editRiskModel.getMaxDisplayNameLength(), DisplayNameConditions.DISPLAY_NAME_MAX );
        
        editRiskModel.applyChanges();
        
        RiskModel rm = editRiskModel.getRiskModel();
        assertEquals("NewModelName", rm.getDisplayName() );
    }
}
