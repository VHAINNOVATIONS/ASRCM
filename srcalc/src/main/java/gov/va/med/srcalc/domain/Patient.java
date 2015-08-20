package gov.va.med.srcalc.domain;

import gov.va.med.srcalc.domain.calculation.RetrievedValue;

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
     * The possible patient genders.
     */
    public enum Gender
    {
        Male,
        Female,
        Unknown
    }

    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 2L;
    
    private final int fDfn;
    
    private final String fName;
    private final Gender fGender;
    private final int fAge;
    private RetrievedValue fBmi;
    private RetrievedValue fWeight;
    private RetrievedValue fWeight6MonthsAgo;
    private RetrievedValue fHeight;
    private List<HealthFactor> fHealthFactors;
    private Map<VistaLabs, RetrievedValue> fLabs;
    private List<String> fActiveMedications;
    private List<ReferenceNote> fAdlNotes;
    private List<ReferenceNote> fDnrNotes;
    
    /**
     * Constructs an instance with the given properties and empty collections for all
     * other properties.
     */
    public Patient(final int dfn, final String name, final Gender gender, final int age)
    {
        this.fDfn = dfn;
        this.fName = name;
        this.fGender = gender;
        this.fAge = age;
        this.fLabs = new HashMap<VistaLabs, RetrievedValue>();
        this.fHealthFactors = new ArrayList<HealthFactor>();
        this.fActiveMedications = new ArrayList<String>();
        this.fAdlNotes = new ArrayList<ReferenceNote>();
        this.fDnrNotes = new ArrayList<ReferenceNote>(); 
    }
    
    /**
     * Returns the patient's VistA Data File Number. Immutable because the DFN for a
     * patient should not change.
     */
    public int getDfn()
    {
        return fDfn;
    }
    
    /**
     * Returns the patient's full name. Immutable because we never need to update a
     * patient's name for this application.
     */
    public String getName()
    {
        return fName;
    }
    
    /**
     * Returns the patient's gender. Immutable because we never need to update it for this
     * application.
     */
    public Gender getGender()
    {
        return fGender;
    }
    
    /**
     * Returns the patient's age. Immutable because we never need to update it for this
     * application.
     */
    public int getAge()
    {
        return fAge;
    }
    
    /**
     * Returns the patient's retrieved BMI.
     */
    public RetrievedValue getBmi()
    {
        return fBmi;
    }
    
    /**
     * Sets the patient's retrieved BMI.
     * @see #getBmi()
     */
    public void setBmi(final RetrievedValue bmi)
    {
        fBmi = bmi;
    }
    
    /**
     * Returns the patient's retrieved weight.
     */
    public RetrievedValue getWeight()
    {
        return fWeight;
    }
    
    /**
     * Sets the patient's retrieved weight.
     * @see #getWeight()
     */
    public void setWeight(final RetrievedValue weight)
    {
        fWeight = weight;
    }
    
    /**
     * Returns the patient's retrieved weight 6 months ago. The weight 6 months ago is in
     * the range of 3-12 months prior to the patient's most recent weight. The most recent
     * weight in that range is considered as weight 6 months ago.
     */
    public RetrievedValue getWeight6MonthsAgo()
    {
        return fWeight6MonthsAgo;
    }
    
    /**
     * Sets the patient's retrieved weight 6 months ago.
     * @see #getWeight6MonthsAgo()
     */
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
     * @return a mutable map
     */
    public Map<VistaLabs,RetrievedValue> getLabs()
    {
        return fLabs;
    }
    
    /**
     * Returns the patient's health factors as a list of {@link HealthFactor}s.
     * @return a mutable list
     */
    public List<HealthFactor> getHealthFactors()
    {
        return fHealthFactors;
    }
    
    /**
     * Returns the patient's medications as a list of Strings. Currently the only information
     * needed is the name of each active medication. Although the date is not present, the
     * medications are listed in order of most recent to least recent.
     * @return a mutable list
     */
    public List<String> getActiveMedications()
    {
        return fActiveMedications;
    }
    
    /**
     * Returns the nursing notes regarding the patient in String form.
     * @return a mutable list
     */
    public List<ReferenceNote> getAdlNotes()
    {
        return fAdlNotes;
    }
    
    /**
     * Returns the notes regarding the patient's DNR status.
     * @return a mutable list
     */
    public List<ReferenceNote> getDnrNotes()
    {
        return fDnrNotes;
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
