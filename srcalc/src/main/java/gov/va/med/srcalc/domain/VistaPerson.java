package gov.va.med.srcalc.domain;

/**
 * A VistA person (from the NEW PERSON file).
 */
public class VistaPerson
{
    private final String fDivision;
    private final String fDuz;
    private final String fDisplayName;
    private final String fUserClass;
    
    /**
     * Constructs an instance.
     * @param division See {@link #getDivision()}.
     * @param duz See {@link #getDuz()}.
     * @param displayName
     * @param userClass
     */
    public VistaPerson(
            final String division,
            final String duz,
            final String displayName,
            final String userClass)
    {
        fDivision = division;
        fDuz = duz;
        fDisplayName = displayName;
        fUserClass = userClass;
    }

    /**
     * The user's currently logged-in division. Note that a single VistA user
     * (NEW PERSON) may be associated with multiple divisions, but we only track
     * the current one.
     */
    public String getDivision()
    {
        return fDivision;
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
    
    @Override
    public String toString()
    {
        return String.format("%s (%s@%s)", fDisplayName, fDuz, fDivision);
    }
}
