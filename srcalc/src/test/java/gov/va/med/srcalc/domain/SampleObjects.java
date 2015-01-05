package gov.va.med.srcalc.domain;

import gov.va.med.srcalc.domain.Specialty;
import gov.va.med.srcalc.domain.variable.*;
import gov.va.med.srcalc.domain.variable.MultiSelectVariable.DisplayType;

import java.util.Arrays;
import java.util.List;

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
     * Returns a sample Thoracic specialty, for when a single specialty is needed.
     * Includes a sample Variable set.
     */
    public static Specialty sampleThoracicSpecialty()
    {
        final Specialty s = new Specialty(58, "Thoracic");
        s.getVariables().add(sampleProcedureVariable());
        s.getVariables().add(sampleAgeVariable());
        s.getVariables().add(dnrVariable());
        s.getVariables().add(functionalStatusVariable());
        return s;
    }
    
    public static List<Variable> sampleVariableList()
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
        	    new Specialty(50, "Other Non-Cardiac Specialty"),
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
}
