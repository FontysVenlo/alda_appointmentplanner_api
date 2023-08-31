package appointmentplanner.api;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Objects;

/**
 * Utility class to do zoned time conversion.
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class LocalDay {

    private final ZoneId zone;
    private final LocalDate date;

    /**
     * Create a local day in the given timezone at the given date.
     *
     * @param zone the timezone to use
     * @param date the LocalDate to use
     */
    public LocalDay( ZoneId zone, LocalDate date ) {
        this.zone = zone;
        this.date = date;
    }

    /**
     * Create a LocalDay based on the system default timezone and the current date.
     */
    public LocalDay() {
        this( ZoneId.systemDefault(), LocalDate.now() );
    }

    /**
     * Get the date.
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Get the timezone identifier.
     *
     * @return timezone identifier
     */
    public ZoneId getZone() {
        return zone;
    }

    /**
     * Get the time as instant.
     *
     * @param localTime the local time
     * @return the time as an instant at this date and timezone
     */
    public Instant ofLocalTime( LocalTime localTime ) {
        return localTime.atDate( date ).atZone( zone ).toInstant();
    }

    /**
     * Get the LocalTime of an instant according to this day.
     *
     * @param instant to convert to LocalTime
     * @return the LocalTime of the given instant
     */
    public LocalTime timeOfInstant( Instant instant ) {
        return instant.atZone( zone ).toLocalTime();
    }

    /**
     * Get the LocalDate of an instant according to this timezone.
     *
     * @param instant to convert to LocalDate
     * @return the LocalDate of the given instant
     */
    public LocalDate dateOfInstant( Instant instant ) {
        return instant.atZone( zone ).toLocalDate();
    }

    /**
     * Add x numbers of days to this LocalDay.
     *
     * @param days to add
     * @return the new LocalDay shifted forward or backward in time.
     */
    public LocalDay plusDays( int days ) {
        return new LocalDay( zone, date.plusDays( days ) );
    }

    /**
     * Get the instant at the given hours and minutes.
     *
     * @param hm the hours
     * @param m the minutes
     * @return Instant at the given ours and minutes
     */
    public Instant at( int hm, int m ) {
        return ofLocalTime( LocalTime.of( hm, m, 0 ) );
    }

    /**
     * Return the LocalDay for the default time zone at the current date.
     *
     * @return the day
     */
    public static LocalDay now() {
        return new LocalDay();
    }

    @Override
    public String toString() {
        return "LocalDay{" + "zone=" + zone + ", date=" + date + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode( this.zone );
        hash = 73 * hash + Objects.hashCode( this.date );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final LocalDay other = (LocalDay) obj;
        if ( !Objects.equals( this.zone, other.zone ) ) {
            return false;
        }
        return Objects.equals( this.date, other.date );
    }
}
