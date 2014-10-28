package gov.va.med.srcalc.domain;

import org.joda.time.DateTime;

/**
 * Represents a risk calculation: either in-progress, calculated, or signed.
 */
public class Calculation
{
    private DateTime fStartDateTime;
    private Patient fPatient;
    private Specialty fSpecialty;
    private Procedure fProcedure;
    
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

    public Procedure getProcedure()
    {
        return fProcedure;
    }

    public void setProcedure(Procedure procedure)
    {
        this.fProcedure = procedure;
    }
}
