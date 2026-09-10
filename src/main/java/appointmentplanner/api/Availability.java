package appointmentplanner.api;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

/**
 * When the owner of a {@link Calendar} is willing to be booked, expressed as
 * recurring wall-clock windows per day of the week in one time zone.
 *
 * <p>
 * Availability is what bounds an otherwise continuous {@link TimeLine}. A
 * timeline on its own has free space between 17:30 and 08:30 the next morning;
 * intersecting that free space with availability is what stops an appointment
 * being offered at 03:00, and what makes appointments that cross midnight
 * impossible without a special rule.
 *
 * <p>
 * Resolving a recurring local window against a date is done through this
 * object's {@link #zoneId()}, so daylight saving transitions are handled in
 * exactly one place.
 */
public interface Availability {

    /**
     * The zone the recurring windows are expressed in. A window of 08:30-17:30
     * means 08:30-17:30 as read from a clock on the wall in this zone, on every
     * date, including the dates on which the offset changes.
     *
     * @return the zone, never null.
     */
    ZoneId zoneId();

    /**
     * The recurring windows, per day of the week. Days absent from the map, or
     * mapped to an empty list, are days on which nothing can be booked.
     *
     * @return the recurring windows.
     */
    Map<DayOfWeek, List<LocalTimeWindow>> windows();

    /**
     * Resolve the recurring windows into concrete intervals overlapping the
     * given period, clipped to that period and returned in chronological order.
     *
     * @param from start of the period of interest, inclusive.
     * @param to end of the period of interest, exclusive.
     * @return the bookable intervals in [from, to), possibly empty.
     * @throws NullPointerException if either bound is null.
     * @throws IllegalArgumentException if to is before from.
     */
    List<TimeSlot> windowsBetween(Instant from, Instant to);

    /**
     * Is the given slot entirely inside a single bookable window? A slot that
     * straddles the end of one window and the start of the next is not covered,
     * which is what keeps appointments out of the intervening night.
     *
     * @param slot the slot to test.
     * @return true if some single window contains the whole slot.
     */
    default boolean covers(TimeSlot slot) {
        return windowsBetween(slot.start(), slot.end()).stream()
                .anyMatch(window -> window.fits(slot));
    }
}
