package gov.va.med.srcalc.domain;

import gov.va.med.srcalc.domain.variable.Variable;

import java.util.List;

import org.joda.time.DateTime;

/**
 * Represents a risk calculation: either in-progress, calculated, or signed.
 */
public class Calculation
{
    private DateTime fStartDateTime;
    private Patient fPatient;
    private Specialty fSpecialty;
    
    /**
     * This class presents a pure JavaBean interface, with a default constructor
     * and mutators for all fields. It also offers convenience factory methods
     * below.
     */
    public Calculation()
    {
        fStartDateTime = new DateTime();
    }
    
    public static Calculation forPatient(final Patient patient)
    {
        final Calculation c = new Calculation();
        c.setPatient(patient);
        return c;
    }
    
    /**
     * The DateTime at which this Calculation first began (for example, when the
     * user first opened the tool).
     */
    public DateTime getStartDateTime()
    {
        return fStartDateTime;
    }

    public void setStartDateTime(DateTime startDateTime)
    {
        this.fStartDateTime = startDateTime;
    }

    public Patient getPatient()
    {
        return fPatient;
    }

    public void setPatient(Patient patient)
    {
        this.fPatient = patient;
    }

    public Specialty getSpecialty()
    {
        return fSpecialty;
    }

    public void setSpecialty(Specialty specialty)
    {
        this.fSpecialty = specialty;
    }
    
    /**
     * Returns the List of {@link Variable}s for the selected specialty.
     * @throws IllegalStateException if no specialty has been set.
     */
    public List<Variable> getVariables()
    {
        // Ensure we are in the proper state.
        if (fSpecialty == null)
        {
            throw new IllegalStateException(
                    "Cannot return list of variables because no specialty has been set.");
        }
        
        return fSpecialty.getVariables();
    }
}
