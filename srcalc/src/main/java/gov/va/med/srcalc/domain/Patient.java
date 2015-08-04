package gov.va.med.srcalc.domain;

import gov.va.med.srcalc.domain.calculation.RetrievedValue;
import gov.va.med.srcalc.vista.AdlNotes.AdlNote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a patient (for which we perform a calculation).
 */
public class Patient implements Serializable
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;
    
    private int fDfn;
    
    private String fName;
    private String fGender;
    private int fAge;
    private RetrievedValue fBmi;
    private RetrievedValue fWeight;
    private RetrievedValue fWeight6MonthsAgo;
    private RetrievedValue fHeight;
    private List<HealthFactor> fHealthFactors;
    private Map<String, RetrievedValue> fLabs;
    private List<String> fActiveMedications;
    private List<AdlNote> fAdlNotes;
    
    public Patient()
    {
    }
    
    public Patient(final int dfn, final String name, final String gender, final int age)
    {
        this.fDfn = dfn;
        this.fName = name;
        this.fGender = gender;
        this.fAge = age;
        this.fLabs = new HashMap<String, RetrievedValue>();
        this.fHealthFactors = new ArrayList<HealthFactor>();
        this.fActiveMedications = new ArrayList<String>();
        this.fAdlNotes = new ArrayList<AdlNote>();
    }
    
    /**
     * The patient's VistA Data File Number.
     */
    public int getDfn()
    {
        return fDfn;
    }
    
    public void setDfn(final int dfn)
    {
        this.fDfn = dfn;
    }
    
    /**
     * Return the patient's full name.
     */
    public String getName()
    {
        return fName;
    }
    
    public void setName(final String name)
    {
        fName = name;
    }
    
    /**
     * The patient's gender.
     */
    public String getGender()
    {
        return fGender;
    }
    
    public void setGender(final String gender)
    {
        fGender = gender;
    }
    
    /**
     * The patient's age.
     */
    public int getAge()
    {
        return fAge;
    }
    
    public void setAge(final int age)
    {
        fAge = age;
    }
    
    /**
     * The patient's automatically retrieved BMI.
     */
    public RetrievedValue getBmi()
    {
        return fBmi;
    }
    
    public void setBmi(final RetrievedValue bmi)
    {
        fBmi = bmi;
    }
    
    /**
     * The patient's automatically retrieved weight.
     */
    public RetrievedValue getWeight()
    {
        return fWeight;
    }
    
    public void setWeight(final RetrievedValue weight)
    {
        fWeight = weight;
    }
    
    /**
     * The patient's automatically retrieved weight 6 months ago. The weight 6 months ago is in the range of 3-12 months
     * prior to the patient's most recent weight. The most recent weight in that range is considered as weight 6 months
     * ago.
     */
    public RetrievedValue getWeight6MonthsAgo()
    {
        return fWeight6MonthsAgo;
    }
    
    public void setWeight6MonthsAgo(final RetrievedValue weight6MonthsAgo)
    {
        this.fWeight6MonthsAgo = weight6MonthsAgo;
    }
    
    /**
     * Returns the patient's height in inches.
     */
    public RetrievedValue getHeight()
    {
        return fHeight;
    }
    
    /**
     * @param height The patient's height in inches
     */
    public void setHeight(final RetrievedValue height)
    {
        this.fHeight = height;
    }
    
    /**
     * Returns all of the labs that were able to be retrieved.
     */
    public Map<String,RetrievedValue> getLabs()
    {
        return fLabs;
    }
    
    public void setLabs(final Map<String, RetrievedValue> labs)
    {
        fLabs = labs;
    }
    
    /**
     * Returns the nursing notes regarding the patient in String form.
     */
    public List<AdlNote> getAdlNotes()
    {
        return fAdlNotes;
    }
    
    /**
     * Returns the patient's health factors as a list of {@link HealthFactor}s.
     */
    public List<HealthFactor> getHealthFactors()
    {
        return fHealthFactors;
    }
    
    /**
     * Returns the patient's medications as a list of Strings. Currently the only information
     * needed is the name of each active medication. Although the date is not present, the
     * medications are listed in order of most recent to least recent.
     */
    public List<String> getActiveMedications()
    {
        return fActiveMedications;
    }
    
    @Override
    public String toString()
    {
        return String.format("Patient %s (DFN: %d)", fName, fDfn);
    }
    
    @Override
    public boolean equals(final Object o)
    {
        if (o instanceof Patient) // false if o == null
        {
            final Patient other = (Patient) o;
            
            return (this.getDfn() == other.getDfn()) && (this.getName().equals(other.getName()));
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getDfn(), getName());
    }
}
