package gov.va.med.srcalc.domain;

/**
 * A VistA person (from the NEW PERSON file).
 */
public class VistaPerson
{
    private final String fDuz;
    private final String fDisplayName;
    private final String fUserClass;
    
    public VistaPerson(final String duz, final String displayName, final String userClass)
    {
        fDuz = duz;
        fDisplayName = displayName;
        fUserClass = userClass;
    }

    /**
     * The user's Internal Entry Number (IEN) in the NEW PERSON file.
     */
    public String getDuz()
    {
        return fDuz;
    }

    public String getDisplayName()
    {
        return fDisplayName;
    }

    public String getUserClass()
    {
        return fUserClass;
    }
    
}
