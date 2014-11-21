package gov.va.med.srcalc.domain;

import gov.va.med.srcalc.domain.Specialty;
import gov.va.med.srcalc.domain.variable.NumericalVariable;
import gov.va.med.srcalc.domain.variable.ProcedureVariable;

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
        final Specialty s = new Specialty(4, 58, "Thoracic");
        s.getVariables().add(new ProcedureVariable("Procedure"));
        s.getVariables().add(new NumericalVariable("Age"));
        return s;
    }

    /**
     * Returns a basic set of Specialty objects.
     */
    public static List<Specialty> sampleSpecialtyList()
    {
        return Arrays.asList(
        	    new Specialty(7, 48, "Cardiac"),
        	    new Specialty(1, 50, "General Surgery"),
        	    new Specialty(2, 52, "Neurosurgery"),
        	    new Specialty(3, 54, "Orthopedic"),
        	    new Specialty(8, 50, "Other Non-Cardiac Specialty"),
        	    SampleObjects.sampleThoracicSpecialty(),
        	    new Specialty(5, 59, "Urology"),
        	    new Specialty(6, 62, "Vascular")
                );
    }
}
