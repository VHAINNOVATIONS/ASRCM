package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.util.Preconditions;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Immutable;
import org.joda.time.DateTime;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;

/**
 * <p>Encapsulates data from a past calculation. Stores calculation metadata (division,
 * specialty name, etc.), but does not store the actual risk results. Unlike {@link
 * CalculationResult}, this class stores its data as primitive values and is suitable for
 * storing in a database.</p>
 * 
 * <p>This class presents an immutable public interface.</p>
 *
 * <p>Per Effective Java Item 17, this class is marked final because it was not
 * designed for inheritance.</p>
 */
@Entity
@Table(name = "historical_calc") // shorten for the sake of the schema
@Immutable
public final class HistoricalCalculation implements Serializable
{
    /**
     * Change this when changing the class!
     */
    private static final long serialVersionUID = 1L;

    /**
     * The maximum length of a station number String, e.g. {@link #getUserStation()}.
     */
    public static final int STATION_NUMBER_MAX = 10;
    
    /**
     * The maximum length of a user class String. VistA's maximum appears to be 30.
     */
    public static final int USER_CLASS_MAX = 80;

    private int fId;
    private String fSpecialtyName;
    private String fUserStation;
    private Date fStartTimestamp;
    private int fSecondsToFirstRun;
    private ImmutableSet<String> fProviderTypes;
    
    /**
     * Intended for reflection-based construction only. Business code should use the other
     * constructor.
     */
    HistoricalCalculation()
    {
    }
    
    /**
     * Constructs an instance with the given properties.
     * @param specialtyName see {@link #getSpecialtyName()}
     * @param providerStation see {@link #getUserStation()}
     * @param secondsToFirstRun see {@link #getSecondsToFirstRun()}
     * @param providerTypes see {@link #getProviderTypes()}. Defensively-copied.
     */
    public HistoricalCalculation(
            final String specialtyName,
            final String providerStation,
            final DateTime startTimestamp,
            final int secondsToFirstRun,
            final Set<String> providerTypes)
    {
        // Use setters to verify constraints.
        setSpecialtyName(specialtyName);
        fUserStation = providerStation;
        // See getStartTimetsamp() for why we enforce 0 millis of second.
        fStartTimestamp = startTimestamp.withMillisOfSecond(0).toDate();
        fSecondsToFirstRun = secondsToFirstRun;
        fProviderTypes = ImmutableSet.copyOf(providerTypes);
    }
    
    /**
     * The object's surrogate primary key. Don't show this to the user.
     */
    @Id
    @GeneratedValue
    public int getId()
    {
        return fId;
    }

    /**
     * For reflection-based construction only. Business code should never modify
     * the surrogate key as it is generated from the database.
     */
    void setId(final int id)
    {
        fId = id;
    }

    /**
     * Returns the name of the associated surgical specialty.
     */
    @Basic
    @Column(nullable = false, length = Specialty.SPECIALTY_NAME_MAX)
    public String getSpecialtyName()
    {
        return fSpecialtyName;
    }
    
    /**
     * For reflection-based construction only. Business code must provide this value to
     * the constructor.
     */
    void setSpecialtyName(final String specialtyName)
    {
        fSpecialtyName = Preconditions.requireWithin(
                specialtyName, 1, Specialty.SPECIALTY_NAME_MAX);
    }

    /**
     * Returns the station number of the user who performed the calculation. At time
     * of writing, this is always the patient's station number as well.
     * @return never null
     */
    @Basic
    @Column(nullable = false, length = STATION_NUMBER_MAX)
    public String getUserStation()
    {
        return fUserStation;
    }
    
    /**
     * For reflection-based construction only. Business code must provide this value to
     * the constructor.
     */
    void setUserStation(final String station)
    {
        fUserStation = Preconditions.requireWithin(station, 1, STATION_NUMBER_MAX);
    }
    
    /**
     * Returns when the user started the calculation. We do not track milliseconds, so
     * this Date object will always represent a particular second on-the-dot.
     */
    @Basic
    @Column(nullable = false)
    public Date getStartTimestamp()
    {
        return fStartTimestamp;
    }
    
    /**
     * For reflection-based construction only. Business code must provide this value to
     * the constructor.
     */
    void setStartTimestamp(final Date dateTime)
    {
        fStartTimestamp = Objects.requireNonNull(dateTime);
    }

    /**
     * Returns the number of seconds from when the user first started the calculation to
     * when he first ran the calculation (i.e., when he first saw the results).
     */
    @Basic
    public int getSecondsToFirstRun()
    {
        return fSecondsToFirstRun;
    }
    
    /**
     * For reflection-based construction only. Business code must provide this value to
     * the constructor.
     */
    void setSecondsToFirstRun(int seconds)
    {
        fSecondsToFirstRun = seconds;
    }

    /**
     * Returns the Provider Types of the user who performed the calculation.
     * @return an ImmutableSet
     * @see VistaPerson#getProviderTypes()
     */
    @ElementCollection(fetch = FetchType.EAGER) // eager due to close association
    // Override strange default column names.
    @CollectionTable(
            // Call the table "person_class" in case we ever add other person class fields
            name = "historical_calc_person_class",
            joinColumns = @JoinColumn(name = "calc_id"))
    @Column(name = "provider_type", nullable = false, length = USER_CLASS_MAX)
    // Hibernate requires us to return a Set instead of an ImmutableSet.
    public Set<String> getProviderTypes()
    {
        return fProviderTypes;
    }
    
    /**
     * For reflection-based construction only. Business code must provide this value to
     * the constructor.
     */
    void setProviderTypes(final Set<String> providerTypes)
    {
        // Note: normally with Hibernate we would preserve the passed collection since it
        // will be Hibernate's special implementation of Set. Since this object is
        // immutable, however, we don't care to keep Hibernate's mutable implementation
        // and just discard it.
        fProviderTypes = ImmutableSet.copyOf(providerTypes);
    }
    
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("specialtyName", fSpecialtyName)
                .add("start", fStartTimestamp)
                .add("secondsToFirstRun", fSecondsToFirstRun)
                .add("userStation", fUserStation)
                .add("providerTypes", fProviderTypes)
                .toString();
    }
    
    /**
     * Implements value equality for all properties.
     */
    @Override
    public boolean equals(final Object o)
    {
        if (o instanceof HistoricalCalculation) // returns false for null
        {
            final HistoricalCalculation other = (HistoricalCalculation)o;
            
            return Objects.equals(this.fSpecialtyName, other.fSpecialtyName) &&
                    Objects.equals(this.fUserStation, other.fUserStation) &&
                    Objects.equals(this.fStartTimestamp, other.fStartTimestamp) &&
                    (this.fSecondsToFirstRun == other.fSecondsToFirstRun) &&
                    Objects.equals(this.fProviderTypes, other.fProviderTypes);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(
                fSpecialtyName,
                fUserStation,
                fStartTimestamp,
                fSecondsToFirstRun,
                fProviderTypes);
    }

}
