package gov.va.med.srcalc.domain;

import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.domain.variable.DiscreteNumericalVariable.Category;
import gov.va.med.srcalc.domain.variable.MultiSelectVariable.DisplayType;

import java.util.*;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Constructs sample instances of various domain objects.
 */
public class SampleObjects
{
	public static Patient dummyPatient(final int dfn)
    {
		final Patient patient = new Patient(dfn, "Zach Smith", "M", 40);
		patient.setBmi(20.0);
        return patient;
    }

    public static Procedure sampleRepairLeftProcedure()
    {
        return new Procedure(
                "26546",
                10.06f,
                "Repair left hand",
                "Repair left hand - you know, the thing with fingers",
                "Standard");  
    }

    public static Procedure sampleRepairRightProcedure()
    {
        return new Procedure(
                "26545",
                5.05f,
                "Repair right hand",
                "Repair right hand - you know, the thing with fingers",
                "Standard");  
    }
    
    public static List<Procedure> sampleProcedureList()
    {
        return Arrays.asList(sampleRepairRightProcedure(), sampleRepairLeftProcedure());
    }
    
    /**
     * <p>Convenience function to construct a RiskModel from the given list of
     * Variables. The terms will have coefficients according to the type:</p>
     * <ul>
     * <li>ProcedureVariable: 1.0</li>
     * <li>NumericalVariable: 2.0</li>
     * <li>MultiSelectVariable: 3.0</li>
     * <li>DiscreteNumericalVariable: 4.0</li>
     * <li>BooleanVariable: 5.0</li>
     * </ul>
     * @param name the intended name of the model
     * @param variables
     * @return a RiskModel with a term for each given variable
     */
    public static RiskModel makeSampleRiskModel(final String name, final Set<DerivedTerm> derivedTerms, 
    		final Variable... variables)
    {
        final RiskModel m = new RiskModel(name);
        m.getDerivedTerms().addAll(derivedTerms);
        
        final ExceptionlessVariableVisitor visitor = new ExceptionlessVariableVisitor()
        {
            @Override
            public void visitProcedure(ProcedureVariable variable)
            {
                m.getProcedureTerms().add(new ProcedureTerm(variable, 1.0f));
            }
            
            @Override
            public void visitNumerical(NumericalVariable variable)
            {
                m.getNumericalTerms().add(new NumericalTerm(variable, 2.0f));
            }
            
            @Override
            public void visitMultiSelect(MultiSelectVariable variable)
            {
                m.getDiscreteTerms().add(new DiscreteTerm(variable, 1, 3.0f));
            }
            
            @Override
            public void visitDiscreteNumerical(DiscreteNumericalVariable variable)
            {
                m.getDiscreteTerms().add(new DiscreteTerm(variable, 0, 4.0f));
            }
            
            @Override
            public void visitBoolean(BooleanVariable variable)
            {
                m.getBooleanTerms().add(new BooleanTerm(variable, 5.0f));
            }
        };
        
        for (final Variable var : variables)
        {
            visitor.visit(var);
        }

        return m;
    }
    
    public static RiskModel sampleThoracicRiskModel()
    {
        final RiskModel m = new RiskModel("Thoracic 30-day mortality estimate");
        m.getProcedureTerms().add(new ProcedureTerm(sampleProcedureVariable(), 1.0f));
        m.getNumericalTerms().add(new NumericalTerm(sampleAgeVariable(), 2.0f));
        m.getBooleanTerms().add(new BooleanTerm(dnrVariable(), 0.5f));
        m.getDiscreteTerms().add(new DiscreteTerm(functionalStatusVariable(), 1, 5.0f));
        return m;
    }

    /**
     * Returns a sample Thoracic specialty, for when a single specialty is needed.
     * Includes a sample Variable set.
     */
    public static Specialty sampleThoracicSpecialty()
    {
        final Specialty s = new Specialty(58, "Thoracic");
        s.getRiskModels().add(sampleThoracicRiskModel());
        return s;
    }
    
    public static List<AbstractVariable> sampleVariableList()
    {
        return Arrays.asList(
                sampleProcedureVariable(),
                sampleAgeVariable(),
                sampleGenderVariable(),
                dnrVariable(),
                functionalStatusVariable(),
                wbcVariable());
    }

    /**
     * Returns a basic set of Specialty objects.
     */
    public static List<Specialty> sampleSpecialtyList()
    {
        return Arrays.asList(
        	    new Specialty(48, "Cardiac"),
        	    new Specialty(50, "General Surgery"),
        	    new Specialty(52, "Neurosurgery"),
        	    new Specialty(54, "Orthopedic"),
        	    SampleObjects.sampleThoracicSpecialty(),
        	    new Specialty(59, "Urology"),
        	    new Specialty(62, "Vascular")
                );
    }

    public static VariableGroup procedureVariableGroup()
    {
        return new VariableGroup("Planned Procedure", 0);
    }
    
    public static VariableGroup demographicsVariableGroup()
    {
        return new VariableGroup("Demographics", 1);
    }
    
    public static VariableGroup labVariableGroup()
    {
        return new VariableGroup("Laboratory Values", 4);
    }
    
    public static VariableGroup recentClinicalVariableGroup()
    {
        return new VariableGroup("Clinical Conditions or Diseases - Recent", 5);
    }

    public static MultiSelectVariable sampleGenderVariable()
    {
        final List<MultiSelectOption> options = Arrays.asList(
                new MultiSelectOption("Male"),
                new MultiSelectOption("Female"));
        final MultiSelectVariable var = new MultiSelectVariable(
                "Gender",
                demographicsVariableGroup(),
                DisplayType.Radio,
                options,
                "gender");
        var.setRetrievalKey(1);
        return var;
    }
    
    public static NumericalVariable sampleAgeVariable()
    {
        final NumericalVariable var = new NumericalVariable(
                "Age", demographicsVariableGroup(), "age");
        var.setMinValue(0);
        var.setMaxValue(999);
        var.setUnits("years");
        var.setRetrievalKey(2);
        return var;
    }

    public static ProcedureVariable sampleProcedureVariable()
    {
        final ProcedureVariable var = new ProcedureVariable(
                "Procedure", procedureVariableGroup(), "procedure");
        var.setProcedures(sampleProcedureList());
        return var;
    }
    
    public static BooleanVariable dnrVariable()
    {
    	final BooleanVariable var= new BooleanVariable("DNR", demographicsVariableGroup(), "dnr");
        return var;
    }
    
    public static MultiSelectVariable functionalStatusVariable()
    {
        final List<MultiSelectOption> fsOptions = Arrays.asList(
                new MultiSelectOption("Independent"),
                new MultiSelectOption("Partially dependent"),
                new MultiSelectOption("Totally dependent"));
        final MultiSelectVariable fsVariable = new MultiSelectVariable(
                "Functional Status",
                recentClinicalVariableGroup(),
                MultiSelectVariable.DisplayType.Radio,
                fsOptions,
                "functionalStatus");
        return fsVariable;
    }
    
    public static DiscreteNumericalVariable wbcVariable()
    {
        final Category wbcWnl = new Category(
                new NumericalRange(Float.NEGATIVE_INFINITY, false, 11.0f, true),
                new MultiSelectOption("WNL"));
        final Category wbcHigh = new Category(
                new NumericalRange(11.0f, false, Float.POSITIVE_INFINITY, false),
                new MultiSelectOption(">11.0"));
        final List<Category> categories = Arrays.asList(wbcWnl, wbcHigh);
        final DiscreteNumericalVariable var = new DiscreteNumericalVariable(
                "White Blood Count", labVariableGroup(), new HashSet<>(categories), "wbc");
        var.setMinValue(2.0f);
        var.setMaxValue(50.0f);
        var.setUnits("x1000/mm^3");
        return var;
    }
    
    /**
     * Like {@link #wbcVariable()} but has a gap in the categories. Users may
     * do this, you know.
     */
    public static DiscreteNumericalVariable misconfiguredWbcVariable()
    {
        final Category wbcWnl = new Category(
                new NumericalRange(Float.NEGATIVE_INFINITY, false, 10.0f, true),
                new MultiSelectOption("WNL"));
        final Category wbcHigh = new Category(
                new NumericalRange(11.0f, false, Float.POSITIVE_INFINITY, false),
                new MultiSelectOption(">11.0"));
        final List<Category> categories = Arrays.asList(wbcWnl, wbcHigh);
        final DiscreteNumericalVariable var = new DiscreteNumericalVariable(
                "White Blood Count", labVariableGroup(), new HashSet<>(categories), "wbc");
        var.setMinValue(2.0f);
        var.setMaxValue(50.0f);
        var.setUnits("x1000/mm^3");
        return var;
    }
    
    /**
     * Constructs a sample {@link Expression} object.
     */
    public static Expression expression1()
    {
        final SpelExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression("true");
    }
    
    /**
     * Constructs a different sample {@link Expression} object.
     */
    public static Expression expression2()
    {
        final SpelExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression("false");
    }
    
    public static Calculation calculatedCalculation() throws Exception
    {
    	final Calculation calc = Calculation.forPatient(SampleObjects.dummyPatient(1));
    	calc.setSpecialty(sampleThoracicSpecialty());
    	final List<Value> values = new ArrayList<Value>();
    	values.add(new BooleanValue(SampleObjects.dnrVariable(), false));
    	values.add(new NumericalValue(SampleObjects.sampleAgeVariable(), 45.0f));
    	values.add(new MultiSelectValue(SampleObjects.functionalStatusVariable(), new MultiSelectOption("Independent")));
    	values.add(new ProcedureValue(SampleObjects.sampleProcedureVariable(), SampleObjects.sampleRepairLeftProcedure()));
    	calc.calculate(values);
    	return calc;
    }
}
