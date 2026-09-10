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
     * Factory method to create a day plan instance.
     *
     * @param day local day of this plan
     * @param startTime earliest time in plan
     * @param endTime no appointments end after this time
     * @return LocalDayPlan object.
     */
    default LocalDayPlan createLocalDayPlan(LocalDay day, LocalTime startTime, LocalTime endTime) {
        return createLocalDayPlan(day, day.ofLocalTime(startTime), day.ofLocalTime(endTime));
    }

    /**
     * Factory method to create a day plan instance.
     *
     * @param day local day of this plan
     * @param start earliest time in plan
     * @param end no appointments end after this time
     * @return LocalDayPlan object.
     */
    LocalDayPlan createLocalDayPlan(LocalDay day, Instant start, Instant end);


    /**
     * Factory method to create an AppointmentData object without specified
     * startTime. The start time will be set (in an Appointment) as soon as the
     * appointment is added to the Day schedule.
     *
     * @param description of the appointment
     * @param duration of the appointment
     * @return AppointmentData object.
     * @throws NullPointerException if any of the parameters are null
     */
    AppointmentData createAppointmentData(String description,
            Duration duration);

    /**
     * Create an appointmentRequest with a required start time.
     *
     * @param data of the request
     * @param startTime time of the request
     * @return the request.
     */
    default AppointmentRequest createAppointmentRequest(AppointmentData data, LocalTime startTime) {
        return createAppointmentRequest(data, startTime, TimePreference.UNSPECIFIED);
    }

    /**
     * Create a request with time preference.
     *
     * @param appData for this request
     * @param preference for time, early or late
     * @return the request
     */
    default AppointmentRequest createAppointmentRequest(AppointmentData appData, TimePreference preference) {
        return createAppointmentRequest(appData, null, preference);
    }

    /**
     * Create a request with a preferred start time and a fallback time
     * preference.
     *
     * @param appointmentData data of the request
     * @param preferredStart preferred start time
     * @param fallBack when time not available, use fallback TimePreference
     * @return the request
     */
    AppointmentRequest createAppointmentRequest(AppointmentData appointmentData,
                                                LocalTime preferredStart,
                                                TimePreference fallBack);

    // ---------------------------------------------------------------------
    // Continuous-calendar API (3.0.0). The LocalDay/LocalDayPlan factory
    // methods above are retained only until the teacher tests are ported,
    // and are removed before 3.0.0 is published.
    // ---------------------------------------------------------------------

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
}
