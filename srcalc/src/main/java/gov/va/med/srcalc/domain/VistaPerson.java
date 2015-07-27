package gov.va.med.srcalc.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;

/**
 * A VistA person (from the NEW PERSON file).
 */
public class VistaPerson
{
    private final String fStationNumber;
    private final String fDuz;
    private final String fDisplayName;
    private final Optional<String> fProviderType;
    
    /**
     * Constructs an instance.
     * @param stationNumber See {@link #getStationNumber()}.
     * @param duz See {@link #getDuz()}.
     * @param displayName See {@link #getDisplayName()}.
     * @param providerType See {@link #getProviderType()}.
     */
    public VistaPerson(
            final String stationNumber,
            final String duz,
            final String displayName,
            final Optional<String> providerType)
    {
        fStationNumber = stationNumber;
        fDuz = duz;
        fDisplayName = displayName;
        fProviderType = providerType;
    }

    /**
     * Identifies the user's currently logged-in division. Note that a single VistA user
     * (NEW PERSON) may be associated with multiple divisions, but we only track the
     * current one.
     */
    public String getStationNumber()
    {
        return fStationNumber;
    }

    /**
     * The user's Internal Entry Number (IEN) in the NEW PERSON file.
     */
    public String getDuz()
    {
        return fDuz;
    }

    /**
     * Returns the user's name as stored in VistA. Typically "LAST,FIRST".
     */
    public String getDisplayName()
    {
        return fDisplayName;
    }

    /**
     * Returns the user's provider type, from their "Person Class". Presumably only
     * clinical staff will have these.
     */
    public Optional<String> getProviderType()
    {
        return fProviderType;
    }
    
    /**
     * A String representation of this object. The exact format is unspecified, but it
     * will at least contain the DUZ, stationNumber, and displayName.
     */
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("duz", fDuz)
                .add("stationNumber", fStationNumber)
                .add("displayName", fDisplayName)
                .add("providerType", fProviderType)
                .toString();
    }
}
