package gov.va.med.srcalc.domain.model;

import gov.va.med.srcalc.domain.model.DiscreteNumericalVariable.Category;
import gov.va.med.srcalc.domain.model.MultiSelectVariable.DisplayType;

import java.util.*;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Constructs sample instances of various risk model objects.
 */
public class SampleModels
{
    public static Procedure repairLeftProcedure()
    {
        return new Procedure(
                "26546",
                10.06f,
                "Repair left hand",
                "Repair left hand - you know, the thing with fingers",
                "Standard");  
    }

    public static Procedure repairRightProcedure()
    {
        return new Procedure(
                "26545",
                5.05f,
                "Repair right hand",
                "Repair right hand - you know, the thing with fingers",
                "Standard");  
    }
    
    public static List<Procedure> procedureList()
    {
        return Arrays.asList(repairRightProcedure(), repairLeftProcedure());
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
    
    public static RiskModel thoracicRiskModel()
    {
        final RiskModel m = new RiskModel("Thoracic 30-day mortality estimate");
        m.getProcedureTerms().add(new ProcedureTerm(procedureVariable(), 1.0f));
        m.getNumericalTerms().add(new NumericalTerm(ageVariable(), 2.0f));
        m.getBooleanTerms().add(new BooleanTerm(dnrVariable(), 0.5f));
        m.getDiscreteTerms().add(new DiscreteTerm(functionalStatusVariable(), 1, 5.0f));
        return m;
    }

    /**
     * Returns a sample Thoracic specialty, for when a single specialty is needed.
     * Includes a sample Variable set.
     */
    public static Specialty thoracicSpecialty()
    {
        final Specialty s = new Specialty(58, "Thoracic");
        s.getRiskModels().add(thoracicRiskModel());
        return s;
    }
    
    /**
     * Returns a sample {@link Rule} that multiplies age by the coefficient
     * if Functional Status == Totally dependent.
     */
    public static Rule ageAndFsRule()
    {
        final NumericalVariable ageVar = SampleModels.ageVariable();
        final MultiSelectVariable fsVar = SampleModels.functionalStatusVariable();
        final ValueMatcher totallyDependentMatcher = new ValueMatcher(
                fsVar, "value == 'Totally dependent'");
        final ValueMatcher ageMatcher = new ValueMatcher(ageVar, "true");
        return new Rule(
                Arrays.asList(totallyDependentMatcher, ageMatcher),
                "#Age.value * #coefficient", true);
    }
    
    public static List<AbstractVariable> sampleVariableList()
    {
        return Arrays.asList(
                procedureVariable(),
                ageVariable(),
                genderVariable(),
                dnrVariable(),
                functionalStatusVariable(),
                wbcVariable());
    }

    /**
     * Returns a basic set of Specialty objects.
     */
    public static List<Specialty> specialtyList()
    {
        return Arrays.asList(
        	    new Specialty(48, "Cardiac"),
        	    new Specialty(50, "General Surgery"),
        	    new Specialty(52, "Neurosurgery"),
        	    new Specialty(54, "Orthopedic"),
        	    SampleModels.thoracicSpecialty(),
        	    new Specialty(59, "Urology"),
        	    new Specialty(62, "Vascular")
                );
    }
    
    /**
     * Constructs a VariableGroup object with the given properties.
     * @param name the group name
     * @param displayOrder the group display order
     * @return a new VariableGroup object with a mock database ID of
     * displayOrder + 1
     */
    private static VariableGroup makeVariableGroup(
            final String name, final int displayOrder)
    {
        final VariableGroup vg = new VariableGroup(name, displayOrder);
        // Normally only Hibernate calls the package-private setId() but fake it
        // here.
        vg.setId(displayOrder + 1);
        return vg;
    }

    public static VariableGroup procedureVariableGroup()
    {
        return makeVariableGroup("Planned Procedure", 0);
    }
    
    public static VariableGroup demographicsVariableGroup()
    {
        return makeVariableGroup("Demographics", 1);
    }
    
    public static VariableGroup labVariableGroup()
    {
        return makeVariableGroup("Laboratory Values", 4);
    }
    
    public static VariableGroup recentClinicalVariableGroup()
    {
        return makeVariableGroup("Clinical Conditions or Diseases - Recent", 5);
    }
    
    /**
     * Returns a set of 4 Variable Groups, sorted in natural order. The objects
     * will have mock database IDs.
     */
    public static SortedSet<VariableGroup> variableGroups()
    {
        return new TreeSet<>(Arrays.asList(
                procedureVariableGroup(),
                demographicsVariableGroup(),
                labVariableGroup(),
                recentClinicalVariableGroup()));
    }

    public static MultiSelectVariable genderVariable()
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
    
    public static NumericalVariable ageVariable()
    {
        final NumericalVariable var = new NumericalVariable(
                "Age", demographicsVariableGroup(), "age");
        var.setMinValue(0);
        var.setMaxValue(999);
        var.setUnits("years");
        var.setRetrievalKey(2);
        return var;
    }

    public static ProcedureVariable procedureVariable()
    {
        final ProcedureVariable var = new ProcedureVariable(
                "Procedure", procedureVariableGroup(), "procedure");
        var.setProcedures(procedureList());
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
}
