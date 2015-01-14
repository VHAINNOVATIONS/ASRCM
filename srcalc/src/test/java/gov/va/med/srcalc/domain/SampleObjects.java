package gov.va.med.srcalc.domain;

import gov.va.med.srcalc.domain.Specialty;
import gov.va.med.srcalc.domain.model.*;
import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.domain.variable.DiscreteNumericalVariable.Category;
import gov.va.med.srcalc.domain.variable.MultiSelectVariable.DisplayType;

import java.util.*;

public class SampleObjects
{
    public static Procedure sampleRepairLeftProcedure()
    {
        return new Procedure(
                "26546",
                10.06f,
                "Repair left hand",
                "Repair left hand - you know, the thing with fingers");  
    }

    public static Procedure sampleRepairRightProcedure()
    {
        return new Procedure(
                "26545",
                5.05f,
                "Repair right hand",
                "Repair right hand - you know, the thing with fingers");  
    }
    
    public static List<Procedure> sampleProcedureList()
    {
        return Arrays.asList(sampleRepairRightProcedure(), sampleRepairLeftProcedure());
    }
    
    /**
     * Convenience function to construct a RiskModel from the given list of
     * Variables. The terms will have arbitrary coefficients.
     * @param name the intended name of the model
     * @param variables
     * @return a RiskModel with a term for each given variable
     */
    public static RiskModel makeSampleRiskModel(final String name, final Variable... variables)
    {
        final RiskModel m = new RiskModel(name);
        
        final ExceptionlessVariableVisitor visitor = new ExceptionlessVariableVisitor()
        {
            @Override
            public void visitProcedure(ProcedureVariable variable)
            {
                m.getTerms().add(new ProcedureTerm(variable, 1.0f));
            }
            
            @Override
            public void visitNumerical(NumericalVariable variable)
            {
                m.getTerms().add(new NumericalTerm(variable, 2.0f));
            }
            
            @Override
            public void visitMultiSelect(MultiSelectVariable variable)
            {
                m.getTerms().add(new DiscreteTerm(variable, 1, 3.0f));
            }
            
            @Override
            public void visitDiscreteNumerical(DiscreteNumericalVariable variable)
            {
                m.getTerms().add(new DiscreteTerm(variable, 0, 4.0f));
            }
            
            @Override
            public void visitBoolean(BooleanVariable variable)
            {
                m.getTerms().add(new BooleanTerm(variable, 5.0f));
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
        m.getTerms().add(new ProcedureTerm(sampleProcedureVariable(), 1.0f));
        m.getTerms().add(new NumericalTerm(sampleAgeVariable(), 2.0f));
        m.getTerms().add(new BooleanTerm(dnrVariable(), 0.5f));
        m.getTerms().add(new DiscreteTerm(functionalStatusVariable(), 1, 5.0f));
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
                functionalStatusVariable());
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
        return new MultiSelectVariable(
                "Gender",
                demographicsVariableGroup(),
                DisplayType.Radio,
                options);
    }
    
    public static NumericalVariable sampleAgeVariable()
    {
        final NumericalVariable var = new NumericalVariable(
                "Age", demographicsVariableGroup());
        var.setMinValue(0);
        var.setMaxValue(999);
        var.setUnits("years");
        return var;
    }

    public static ProcedureVariable sampleProcedureVariable()
    {
        final ProcedureVariable var = new ProcedureVariable(
                "Procedure", procedureVariableGroup());
        var.setProcedures(sampleProcedureList());
        return var;
    }
    
    public static BooleanVariable dnrVariable()
    {
        return new BooleanVariable("DNR", demographicsVariableGroup());
    }
    
    public static MultiSelectVariable functionalStatusVariable()
    {
        final List<MultiSelectOption> fsOptions = Arrays.asList(
                new MultiSelectOption("Independent"),
                new MultiSelectOption("Partially dependent"),
                new MultiSelectOption("Totally dependent"));
        return new MultiSelectVariable(
                "Functional Status",
                recentClinicalVariableGroup(),
                MultiSelectVariable.DisplayType.Radio,
                fsOptions);
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
                "White Blood Count", labVariableGroup(), new HashSet<>(categories));
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
                "White Blood Count", labVariableGroup(), new HashSet<>(categories));
        var.setMinValue(2.0f);
        var.setMaxValue(50.0f);
        var.setUnits("x1000/mm^3");
        return var;
    }
}
