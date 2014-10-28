package gov.va.med.srcalc.domain;

import java.util.Objects;

/**
 * Represents a patient (for which we perform a calculation).
 */
public class Patient
{
    private int fDfn;
    
    private String fName;

    public Patient()
    {
    }
    
    public Patient(int dfn, String name)
    {
        this.fDfn = dfn;
        this.fName = name;
    }

    /**
     * The patient's VistA Data File Number.
     */
    public int getDfn()
    {
        return fDfn;
    }

    public void setDfn(int dfn)
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
    
    @Override
    public String toString()
    {
        return fName;
    }
    
    @Override
    public boolean equals(Object o)
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

