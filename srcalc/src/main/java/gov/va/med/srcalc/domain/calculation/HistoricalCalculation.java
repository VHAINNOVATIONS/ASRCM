package gov.va.med.srcalc.domain.calculation;

import gov.va.med.srcalc.domain.VistaPerson;
import gov.va.med.srcalc.domain.model.Specialty;
import gov.va.med.srcalc.util.Preconditions;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;

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
     * The maximum length of a user class String. VistA's maximum appears to be 62.
     */
    public static final int PROVIDER_TYPE_MAX = 80;

    private int fId;
    private String fSpecialtyName;
    private String fUserStation;
    private DateTime fStartTimestamp;
    private int fSecondsToFirstRun;
    private Optional<String> fProviderType;
    
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
     * @param providerType see {@link #getProviderType()}.
     */
    public HistoricalCalculation(
            final String specialtyName,
            final String providerStation,
            final DateTime startTimestamp,
            final int secondsToFirstRun,
            final Optional<String> providerType)
    {
        // Use setters to verify constraints.
        setSpecialtyName(specialtyName);
        fUserStation = providerStation;
        // See getStartTimetsamp() for why we enforce 0 millis of second.
        fStartTimestamp = startTimestamp.withMillisOfSecond(0);
        fSecondsToFirstRun = secondsToFirstRun;
        setProviderType(providerType);
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
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getStartTimestamp()
    {
        return fStartTimestamp;
    }
    
    /**
     * For reflection-based construction only. Business code must provide this value to
     * the constructor.
     */
    void setStartTimestamp(final DateTime dateTime)
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
     * Returns the Provider Type of the user who performed the calculation, if there was
     * one.
     * @return never nn Optional containing the provider type, if applicable
     * @see VistaPerson#getProviderType()
     */
    @Transient
    public Optional<String> getProviderType()
    {
        return fProviderType;
    }
    
    /**
     * Sets the provider type, checking preconditions.
     * @throws IllegalArgumentException if the providerType is empty or greater than
     * {@link #PROVIDER_TYPE_MAX} characters
     */
    private void setProviderType(final Optional<String> providerType)
    {
        if (providerType.isPresent())
        {
            Preconditions.requireWithin(providerType.get(), 1, PROVIDER_TYPE_MAX);
        }
        
        fProviderType = providerType;
    }
    
    /**
     * Similar to {@link #getProviderType()}, but represents a missing Provider Type as
     * null. Purely to support Hibernate, which does not support Guava's Optional class.
     * @return the optional Provider Type as a nullable string
     */
    @Basic
    @Column(name = "provider_type", nullable = true, length = PROVIDER_TYPE_MAX)
    String getProviderTypeNullable()
    {
        return fProviderType.orNull();
    }
    
    /**
     * For reflection-based construction only. Business code must provide this value to
     * the constructor.
     * @throws IllegalArgumentException if the providerType is empty or greater than
     * {@link #PROVIDER_TYPE_MAX} characters
     */
    void setProviderTypeNullable(final String providerType)
    {
        setProviderType(Optional.fromNullable(providerType));
    }
    
    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("specialtyName", fSpecialtyName)
                .add("start", fStartTimestamp)
                .add("secondsToFirstRun", fSecondsToFirstRun)
                .add("userStation", fUserStation)
                .add("providerType", fProviderType)
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
                    Objects.equals(this.fProviderType, other.fProviderType);
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
                fProviderType);
    }

}
