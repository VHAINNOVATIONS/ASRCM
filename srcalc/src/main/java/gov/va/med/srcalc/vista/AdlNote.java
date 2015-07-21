package gov.va.med.srcalc.vista;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class AdlNote
{
    private String fLocalTitle;
    private Date fSignDate;
    private String fNoteBody;

    public String getLocalTitle()
    {
        return fLocalTitle;
    }

    @XmlAttribute(name = "localTitle")
    public void setLocalTitle(String fLocalTitle)
    {
        this.fLocalTitle = fLocalTitle;
    }

    public Date getSignDate()
    {
        return this.fSignDate;
    }
    
    @XmlAttribute(name = "signDate")
    public void setSignDate(final Date signDate)
    {
        this.fSignDate = signDate;
    }
    
    @XmlElement (name = "body")
    public void setNoteBody(final String noteBody)
    {
        this.fNoteBody = noteBody;
    }
    
    public String getNoteBody()
    {
        return this.fNoteBody;
    }
}
