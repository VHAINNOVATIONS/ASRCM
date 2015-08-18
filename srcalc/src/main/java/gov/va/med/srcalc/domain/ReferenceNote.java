package gov.va.med.srcalc.domain;

import gov.va.med.srcalc.util.XmlDateAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

/**
 * This class is created to store information that is taken from an XML source. Each
 * instance will store a single note's information.
 */
@XmlRootElement(name = "note")
public final class ReferenceNote
{
    private String fLocalTitle;
    private DateTime fSignDate;
    private String fNoteBody;

    /**
     * Return the specific localized title for this note.
     */
    public String getLocalTitle()
    {
        return fLocalTitle;
    }

    /**
     * Set the local title for this reference note. Necessary for XML binding.
     * @param localTitle the name of the local title
     */
    @XmlAttribute(name = "localTitle")
    public void setLocalTitle(final String localTitle)
    {
        this.fLocalTitle = localTitle;
    }

    /**
     * Returns the date on which this note was signed.
     */
    public DateTime getSignDate()
    {
        return this.fSignDate;
    }
    
    /**
     * Set the date and time on which this note was signed. Necessary for XML binding.
     * @param signDate the DateTime on which this note was signed
     */
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    @XmlAttribute(name = "signDate")
    public void setSignDate(final DateTime signDate)
    {
        this.fSignDate = signDate;
    }

    /**
     * Return the body of the note in String form.
     */
    public String getNoteBody()
    {
        return this.fNoteBody;
    }
    
    /**
     * Set the body of this reference note. Necessary for XML binding.
     * @param noteBody the body of the note
     */
    @XmlElement (name = "body")
    public void setNoteBody(final String noteBody)
    {
        this.fNoteBody = noteBody;
    }
}