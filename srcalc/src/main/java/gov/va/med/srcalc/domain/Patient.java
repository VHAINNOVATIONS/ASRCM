package gov.va.med.srcalc.domain;

import java.io.Serializable;
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

    public Patient()
    {
    }
    
    public Patient(final int dfn, final String name, final String gender, final int age)
    {
        this.fDfn = dfn;
        this.fName = name;
        this.fGender = gender;
        this.fAge = age;
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

    public String getName()
    {
	return fName;
    }

    public void setName(final String name)
    {
    	fName = name;
    }
    
    public String getGender()
    {
    	return fGender;
    }

    public void setGender(final String gender)
    {
    	fGender = gender;
    }
    
    public int getAge()
    {
    	return fAge;
    }

    public void setAge(final int age)
    {
    	fAge = age;
    }
    
    @Override
    public String toString()
    {
        return String.format("Patient %s (DFN: %d)", fName, fDfn);
    }
    
    @Override
    public boolean equals(final Object o)
    {
        if (o instanceof Patient)  // false if o == null
        {
            final Patient other = (Patient)o;
            
            return (this.getDfn() == other.getDfn()) &&
                    (this.getName().equals(other.getName()));
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

