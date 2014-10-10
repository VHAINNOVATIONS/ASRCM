package gov.va.med.srcalc.domain;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Specialty
{
    private int fId;
    
    private String fName;

    public Specialty()
    {
    }
    
    public Specialty(int id, String name)
    {
        this.fId = id;
        this.fName = name;
    }

    @Id // We use method-based property detection throughout the app.
    public int getId()
    {
        return fId;
    }

    public void setId(int id)
    {
        this.fId = id;
    }

    @Basic
    public String getName()
    {
	return fName;
    }

    public void setName(final String name)
    {
	fName = name;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Specialty)  // false if o == null
        {
            final Specialty other = (Specialty)o;
            
            return (this.getId() == other.getId()) &&
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
        return Objects.hash(getId(), getName());
    }
}

