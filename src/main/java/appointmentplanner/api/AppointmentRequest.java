package appointmentplanner.api;

import java.time.Duration;
import java.util.List;

/**
 * A request to plan an appointment: what it is, how long it takes, who must
 * attend, when it may happen, and which of the possible periods to prefer.
 *
 * <p>
 * A request describes an appointment that has not been placed yet. It carries
 * no start time: where it lands is decided by the calendars it names and the
 * periods they have free.
 */
public interface AppointmentRequest {

    /**
     * What the appointment is for.
     *
     * @return the description, never null or empty.
     */
    String description();

    /**
     * How long the appointment takes.
     *
     * @return the duration, never null.
     */
    Duration duration();

    /**
     * Which of the possible periods to choose. Defaults to
     * {@link TimePreference#UNSPECIFIED}, which behaves as EARLIEST.
     *
     * @return the preference, never null.
     */
    default TimePreference timePreference() {
        return TimePreference.UNSPECIFIED;
    }

    /**
     * The period this request may be planned in.
     *
     * <p>
     * A timeline is continuous and has no end, so a search has to be bounded.
     * Narrowing the horizon is also how "not before" and "not after" are
     * expressed, which is why there are no time preferences for them.
     *
     * @return the horizon, never null.
     */
    TimeSlot horizon();

    /**
     * Names of the calendars that must all attend. A request naming several
     * owners is satisfiable only in a period that is free and available in
     * every one of them.
     *
     * @return the calendar owners, never empty.
     */
    List<String> calendars();

    /**
     * Human readable rendering of this request.
     *
     * @return this request as text.
     */
    @Override
    String toString();
}
