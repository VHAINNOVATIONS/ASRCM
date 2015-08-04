package gov.va.med.srcalc.vista;

import gov.va.med.srcalc.util.XmlDateAdapter;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;

/**
 * This class exists to be the top level element of an XML source containing a patient's
 * nursing notes. As the top level element, it should only contain a collection of
 * {@link AdlNote}.
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@XmlRootElement(name = "notes")
public final class AdlNotes
{
    private List<AdlNote> fAllNotes;
    
    /**
     * Returns all of the {@link AdlNote}s contained in this instance.
     */
    public List<AdlNote> getAllNotes()
    {
        return fAllNotes;
    }
    
    @XmlElement (name = "note")
    public void setAllNotes(final List<AdlNote> allNotes)
    {
        this.fAllNotes = allNotes;
    }
    
    /**
     * This class is created to store information that is taken from an XML source. Each
     * instance will store a single note's information.
     */
    @XmlRootElement(name = "note")
    public static final class AdlNote
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

        @XmlAttribute(name = "localTitle")
        public void setLocalTitle(String fLocalTitle)
        {
            this.fLocalTitle = fLocalTitle;
        }

        /**
         * Returns the date on which this note was signed.
         */
        public DateTime getSignDate()
        {
            return this.fSignDate;
        }
        
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
        
        @XmlElement (name = "body")
        public void setNoteBody(final String noteBody)
        {
            this.fNoteBody = noteBody;
        }
    }
}
