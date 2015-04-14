package gov.va.med.srcalc.domain;

import java.io.Serializable;
import java.util.Date;
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
    private double fBmi;
    private Date fBmiDate;
    private double fWeight;
    private Date fWeightDate;
    private double fWeight6MonthsAgo;
    private Date fWeight6MonthsAgoDate;
    private int fHeight;
    private Date fHeightDate;

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
    
    public double getBmi()
    {
    	return fBmi;
    }

    public void setBmi(final double bmi)
    {
    	fBmi = bmi;
    }
    
    /**
     * The date on which the BMI was retrieved
     */
    public Date getBmiDate()
    {
		return fBmiDate;
	}
    
    /**
     * Set the date on which the patient's height was measured.
     * @param bmiDate
     */
	public void setBmiDate(final Date bmiDate)
	{
		this.fBmiDate = bmiDate;
	}

	public double getWeight()
    {
    	return fWeight;
    }

    public void setWeight(final double weight)
    {
    	fWeight = weight;
    }
    
    public Date getWeightDate()
    {
		return fWeightDate;
	}

    /**
     * Set the date on which the patient's height was measured.
     * @param weightDate
     */
	public void setWeightDate(final Date weightDate)
	{
		this.fWeightDate = weightDate;
	}
	
    public double getWeight6MonthsAgo()
    {
		return fWeight6MonthsAgo;
	}

	public void setWeight6MonthsAgo(double weight6MonthsAgo)
	{
		this.fWeight6MonthsAgo = weight6MonthsAgo;
	}

	public Date getWeight6MonthsAgoDate() {
		return fWeight6MonthsAgoDate;
	}

	public void setWeight6MonthsAgoDate(Date weight6MonthsAgoDate)
	{
		this.fWeight6MonthsAgoDate = weight6MonthsAgoDate;
	}

	/**
     * @return The patient's height in inches
     */
    public int getHeight()
    {
		return fHeight;
	}

    /**
     * 
     * @param height The patient's height in inches
     */
	public void setHeight(int height)
	{
		this.fHeight = height;
	}

	public Date getHeightDate()
	{
		return fHeightDate;
	}

	/**
	 * Set the date on which the patient's height was measured.
	 * @param fHeightDate
	 */
	public void setHeightDate(Date fHeightDate)
	{
		this.fHeightDate = fHeightDate;
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

