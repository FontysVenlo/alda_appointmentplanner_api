package appointmentplanner.api;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the interface that the user (the teachers) of your implementation use
 * to obtain instances of the classes implementing the given other interfaces.
 */
public interface AbstractAPFactory {

    /**
     * Create availability from recurring wall-clock windows per weekday.
     *
     * @param zone the zone the windows are read in.
     * @param windows the recurring windows; absent or empty days are days on
     * which nothing can be booked.
     * @return the availability.
     * @throws NullPointerException if any argument is null.
     */
    Availability createAvailability(ZoneId zone, Map<DayOfWeek, List<LocalTimeWindow>> windows);

    /**
     * Convenience for the common case: the same single window on each of the
     * given days.
     *
     * @param zone the zone the window is read in.
     * @param days the days on which the owner is available.
     * @param open start of the daily window.
     * @param close end of the daily window; must be after open.
     * @return the availability.
     * @throws NullPointerException if any argument is null.
     * @throws IllegalArgumentException if close is not after open.
     */
    default Availability createAvailability(ZoneId zone, Set<DayOfWeek> days,
            LocalTime open, LocalTime close) {
        LocalTimeWindow window = new LocalTimeWindow(open, close);
        Map<DayOfWeek, List<LocalTimeWindow>> windows = new EnumMap<>(DayOfWeek.class);
        days.forEach(day -> windows.put(day, List.of(window)));
        return createAvailability(zone, windows);
    }

    /**
     * Create an empty timeline spanning the given period. The period only has
     * to be wide enough to hold everything that will ever be planned on it; a
     * timeline has no notion of a day.
     *
     * @param from start of the span.
     * @param to end of the span; must be after from.
     * @return an empty timeline.
     * @throws NullPointerException if any argument is null.
     * @throws IllegalArgumentException if to is not after from.
     */
    TimeLine createTimeLine(Instant from, Instant to);

    /**
     * Create a calendar for one owner.
     *
     * @param owner name of the owner, used to address the calendar.
     * @param availability when this owner can be booked.
     * @return the calendar, holding no appointments.
     * @throws NullPointerException if any argument is null.
     * @throws IllegalArgumentException if owner is blank.
     */
    Calendar createCalendar(String owner, Availability availability);

    /**
     * Create the planner that plans across calendars.
     *
     * @return a planner holding no calendars.
     */
    AppointmentPlanner createAppointmentPlanner();

    /**
     * Create a request for the continuous-calendar API.
     *
     * @param description what the appointment is for.
     * @param duration how long it takes.
     * @param preference which of the possible periods to choose.
     * @param horizon the period to search within.
     * @param calendars owners of every calendar that must attend.
     * @return the request.
     * @throws NullPointerException if any argument is null.
     * @throws IllegalArgumentException if calendars is empty.
     */
    AppointmentRequest createAppointmentRequest(String description, Duration duration,
            TimePreference preference, TimeSlot horizon, List<String> calendars);

    /**
     * Create a period in absolute time, used above all to express the horizon
     * a request is searched in.
     *
     * @param from start of the period, inclusive.
     * @param to end of the period, exclusive; must be after from.
     * @return the period.
     * @throws NullPointerException if any argument is null.
     * @throws IllegalArgumentException if to is not after from.
     */
    TimeSlot createTimeSlot(Instant from, Instant to);
}
