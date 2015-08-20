package gov.va.med.srcalc.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class exists to be the top level element of an XML source containing a patient's
 * nursing notes. As the top level element, it should only contain a collection of
 * {@link ReferenceNote}.
 * 
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@XmlRootElement(name = "notes")
public final class ReferenceNotes
{
    private List<ReferenceNote> fAllNotes = new ArrayList<ReferenceNote>();
    
    /**
     * Returns all of the {@link ReferenceNote}s contained in this instance.
     */
    public List<ReferenceNote> getAllNotes()
    {
        return fAllNotes;
    }
    
    /**
     * Sets the collection of {@link ReferenceNote}s contained in this instance. If a null
     * value is passed, the collection is set to an empty list rather than null. This is
     * done to work around null being passed during XML binding.
     * @param allNotes the notes to contain in this instance (may be null)
     */
    @XmlElement (name = "note")
    public void setAllNotes(final List<ReferenceNote> allNotes)
    {
        // If there are no notes present, JAXB passes a null here.
        if(allNotes == null)
        {
            fAllNotes = new ArrayList<ReferenceNote>();
        }
        else
        {
            this.fAllNotes = allNotes;
        }
    }
}
