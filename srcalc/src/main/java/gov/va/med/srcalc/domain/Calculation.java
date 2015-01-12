package gov.va.med.srcalc.domain;

import gov.va.med.srcalc.domain.variable.*;

import java.io.Serializable;
import java.util.*;

import org.joda.time.DateTime;

/**
 * Represents a risk calculation: either in-progress, calculated, or signed.
 */
public class Calculation implements Serializable
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    private DateTime fStartDateTime;
    private Patient fPatient;
    private Specialty fSpecialty;
    private List<Value> fValues;
    
    /**
     * This class presents a pure JavaBean interface, with a default constructor
     * and mutators for all fields. It also offers convenience factory methods
     * below.
     */
    public Calculation()
    {
        fStartDateTime = new DateTime();
        fValues = new ArrayList<>();
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

    public void setStartDateTime(final DateTime startDateTime)
    {
        this.fStartDateTime = startDateTime;
    }

    public Patient getPatient()
    {
        return fPatient;
    }

    public void setPatient(final Patient patient)
    {
        this.fPatient = patient;
    }

    public Specialty getSpecialty()
    {
        return fSpecialty;
    }

    public void setSpecialty(final Specialty specialty)
    {
        this.fSpecialty = specialty;
    }
    
    /**
     * Returns the List of {@link Variable}s for the selected specialty.
     * @throws IllegalStateException if no specialty has been set.
     * @return an unmodifiable list of Variables.
     */
    public List<Variable> getVariables()
    {
        // Ensure we are in the proper state.
        if (fSpecialty == null)
        {
            throw new IllegalStateException(
                    "Cannot return list of variables because no specialty has been set.");
        }
        
        // We want to return aa List<Variable> instead of a
        // List<AbstractVariable> so explicitly specificy the type argument.
        return Collections.<Variable>unmodifiableList(fSpecialty.getVariables());
    }
    
    /**
     * Returns the selected specialty's variables bucketed into groups. See
     * {@link Specialty#getVariableGroups()} for details.
     * @throws IllegalStateException if no specialty has been set.
     */
    public List<PopulatedVariableGroup> getVariableGroups()
    {
        // Ensure we are in the proper state.
        if (fSpecialty == null)
        {
            throw new IllegalStateException(
                    "Cannot return list of variables because no specialty has been set.");
        }
        
        return fSpecialty.getVariableGroups();
    }

    public List<Value> getValues()
    {
        return fValues;
    }
    
    /**
     * For bean construction only. Replaces the internal List of Values with the
     * given one.
     */
    void setValues(final List<Value> values)
    {
        fValues = values;
    }
    
    /**
     * Runs the calculation for each outcome with the given Values.
     */
    public void calculate(final List<Value> values)
    {
        // Placeholder validity check - may change as we implement outcomes.
        if (values.size() != getVariables().size())
        {
            throw new IllegalArgumentException("One value for each variable must be given");
        }

        fValues.clear();
        fValues.addAll(values);

        // Outcome(s) will be calculated here when we get to that.
    }
}
