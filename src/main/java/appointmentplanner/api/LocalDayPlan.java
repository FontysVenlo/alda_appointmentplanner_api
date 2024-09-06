package appointmentplanner.api;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import static java.util.stream.Collectors.toList;

/**
 * Timezone and day based view on timeline. This is the classic view on a day
 * plan or calendar in which entries are added and viewed in local time.
 *
 * A LocalDayPlan has two local time boundaries for allowed times, start time
 * and end time, which default to 0:00 (inclusive) and 24:00 exclusive.
 *
 * The implementer should implement a toString that shows all appointments based
 * on local time. It is left to the discretion of the implementer to also show
 * the gaps.
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public interface LocalDayPlan {

    /**
     * LocalDay specifies date and time zone.
     *
     * @return the day
     */
    LocalDay getDay();

    /**
     * Start time of the day, inclusive.
     *
     * @return instant representing the start of the day
     */
    Instant startOfDay();

    /**
     * End time of the day, exclusive. E.g. when end time is 17.30,
     * in that minute no new appointment (of 1 minute) can be added.
     *
     * @return instant representing the end of the day
     */
    Instant endOfDay();

    /**
     * Get the timeline used by this LocalDayPlan.
     *
     * @return the timeline used by this LocalDayPlan
     */
    Timeline getTimeline();

    /**
     * Get the allowed first time for this day.
     *
     * @return the start time of this plan
     */
    default LocalTime getStartTime() {
        return getDay().timeOfInstant(startOfDay());
    }

    /**
     * Get the allowed latest time for this day.
     *
     * @return the end time in this plan
     */
    default LocalTime getEndTime() {
        return getDay().timeOfInstant(endOfDay());
    }

    /**
     * Add an appointment with start time and fallback time preference.
     *
     * @param appointmentData the appointment data
     * @param start           if not null, preferred start time
     * @param fallback        if start cannot be met, use as preference for fallback
     * @return an optional of Appointment, which is present when the appointment
     * was created successfully, or empty when not successful.
     */
    default Optional<Appointment> addAppointment(AppointmentData appointmentData,
                                                  LocalTime start,
                                                  TimePreference fallback) {
        return getTimeline().addAppointment(getDay(), appointmentData, start, fallback);
    }

    /**
     * Add and appointment with fix start time. This request can fail (Optional
     * is not present) if the start time is not available.
     *
     * @param appointmentData data
     * @param startTime       fixed time
     * @return Optional Appointment
     */
    default Optional<Appointment> addAppointment(AppointmentData appointmentData, LocalTime startTime) {
        return getTimeline().addAppointment(getDay(), appointmentData, startTime);
    }

    /**
     * Add and appointment with time preference. This request can fail (Optional
     * is not present) if the data's duration does not fit in the (already
     * planned day).
     *
     * @param appointmentData data
     * @param preference            time preference
     * @return Optional Appointment
     */
    default Optional<Appointment> addAppointment(AppointmentData appointmentData, TimePreference preference) {
        return getTimeline().addAppointment(getDay(), appointmentData, preference);
    }

    /**
     * See {@link Timeline#removeAppointment(Appointment)}.
     *
     * @param appointment the appointment to remove
     * @return AppointmentRequest, the original appointment request
     */
    default AppointmentRequest removeAppointment(Appointment appointment) {
        return getTimeline().removeAppointment(appointment);
    }

    /**
     * {@link Timeline#removeAppointments(Predicate)}.
     *
     * @param filter to remove all appointments that match
     * @return all appointment requests of removed appointments
     */
    default List<AppointmentRequest> removeAppointments(Predicate<Appointment> filter) {
        return getTimeline().removeAppointments(filter);
    }

    /**
     * {@link Timeline#getAppointments()}.
     *
     * @return all appointments
     */
    default List<Appointment> getAppointments() {
        return getTimeline().getAppointments();
    }

    /**
     * See {@link Timeline#getMatchingFreeSlotsOfDuration(Duration, List)}.
     *
     * @param duration Minimum duration of the slots
     * @param plans that could have common gaps
     * @return the list of gaps this and each of the other plans have in common
     * with a minimum length of duration.
     */
    default List<TimeSlot> getMatchingFreeSlotsOfDuration(Duration duration, List<LocalDayPlan> plans) {
        return getTimeline().getMatchingFreeSlotsOfDuration(duration, plans.stream().map(LocalDayPlan::getTimeline).collect(toList()));
    }

    /**
     * See {@link Timeline#getGapsFitting(Duration)}.
     *
     * @param duration the minimum duration that should fit
     * @return list of timeslots that fit the duration
     */
    default List<TimeSlot> getGapsFitting(Duration duration) {
        return getTimeline().getGapsFitting(duration);
    }

    /**
     * {@link Timeline#getGapsFittingReversed(Duration)}.
     *
     * @param duration the minimum duration that should fit
     * @return list of timeslots that fit the duration
     */
    default List<TimeSlot> getGapsFittingReversed(Duration duration) {
        return getTimeline().getGapsFittingReversed(duration);
    }

    /**
     * See{@link Timeline#getGapsFittingLargestFirst(Duration)}.
     *
     * @param duration the minimum duration
     * @return list of gaps fitting the duration
     */
    default List<TimeSlot> getGapsFittingLargestFirst(Duration duration) {
        return getTimeline().getGapsFittingLargestFirst(duration);
    }

    /**
     * {@link Timeline#getGapsFittingSmallestFirst(Duration)}.
     *
     * @param duration the minimum duration
     * @return list of timeslots fitting the duration
     */
    default List<TimeSlot> getGapsFittingSmallestFirst(Duration duration) {
        return getTimeline().getGapsFittingSmallestFirst(duration);
    }

    /**
     * {@link Timeline#canAddAppointmentOfDuration(Duration)}.
     *
     * @param duration the minimum duration
     * @return true of a gap is available, false otherwise
     */
    default boolean canAddAppointmentOfDuration(Duration duration) {
        return getTimeline().canAddAppointmentOfDuration(duration);
    }

    /**
     * {@link Timeline#findAppointments(Predicate)} ()}.
     *
     * @param filter to find appointments
     * @return list of appointments that fit the filter
     */
    default List<Appointment> findAppointments(Predicate<Appointment> filter) {
        return getTimeline().findAppointments(filter);
    }

    /**
     * Return a string containing the local date, the time zone and all
     * appointments in natural order, presented with time as local time.
     *
     * @return a string representation of the local day plan.
     */
    @Override
    String toString();

    /**
     * {@link Timeline#contains(Appointment)}.
     *
     * @param appointment the appointment to check
     * @return true if present, false otherwise
     */
    default boolean contains(Appointment appointment) {
        return getTimeline().contains(appointment);
    }

    /**
     * What is the (start) date of this plan.
     *
     * @return the date according to this LocalDayPlan's time zone
     */
    default LocalDate getDate() {
        return getDay().getDate();
    }

    /**
     * {@link Timeline#getNrOfAppointments()}.
     *
     * @return number of appointments
     */
    default int getNrOfAppointments() {
        return getTimeline().getNrOfAppointments();
    }

    /**
     * Get the instant at the given hour and minute of this local day plan.
     *
     * @param hour sic
     * @param minute sic
     * @return the point in time as Instant
     */
    default Instant at(int hour, int minute) {
        return getDay().at(hour, minute);
    }

}
