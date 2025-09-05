package appointmentplanner.api;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
 */
public interface LocalDayPlan {

    /**
     * LocalDay specifies date and time zone.
     *
     * @return the day
     */
    LocalDay day();

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
     * Get the allowed first time for this day.
     *
     * @return the start time of this plan
     */
    default LocalTime startTime() {
        return day().timeOfInstant(startOfDay());
    }

    /**
     * Get the allowed latest time for this day.
     *
     * @return the end time in this plan
     */
    default LocalTime endTime() {
        return day().timeOfInstant(endOfDay());
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
    Optional<Appointment> addAppointment(AppointmentData appointmentData,
                                                  LocalTime start,
                                                  TimePreference fallback);
    /**
     * Add and appointment with fix start time. This request can fail (Optional
     * is not present) if the start time is not available.
     *
     * @param appointmentData data
     * @param startTime       fixed time
     * @return Optional Appointment
     */
    Optional<Appointment> addAppointment(AppointmentData appointmentData, LocalTime startTime);

    /**
     * Add and appointment with time preference. This request can fail (Optional is not present)
     * if the data's duration does not fit in the (already planned day).
     *
     * This method only supports TimePreferences EARLIEST or LATEST; Other values are defaulted to EARLIEST
     *
     * @param appointmentData data
     * @param preference            time preference
     * @return Optional Appointment
     */
    Optional<Appointment> addAppointment(AppointmentData appointmentData, TimePreference preference);

    /**
     * Removes the given appointment, returning the AppointmentRequest of that appointment, if
     * found. This day is searched for a non-free time slot matching the
     * appointment. The returned data could be used to re-plan the appointment.
     *
     * @param appointment to remove
     * @return the AppointmentRequest of the removed appointment or null
     * if the appointment is not found.
     */
    AppointmentRequest removeAppointment(Appointment appointment);

    /**
     * Removes appointments with description that matches a filter.
     *
     * @param filter to determine which items to remove.
     * @return the list of AppointmentRequests of removed appointments.
     */
    List<AppointmentRequest> removeAppointments(Predicate<Appointment> filter);

    /**
     * Finds all appointments for this plan.
     *
     * @return list of all appointments.
     */
    List<Appointment> appointments();

    /**
     * Find matching free time slots in this and other TimeLines. To facilitate
     * appointment proposals.
     * 
     * @param duration Minimum duration of the slot to search for.
     * @param plans that could have common gaps
     * @return the list of gaps this and each of the other plans have in common
     * with a minimum length of duration.
     */
    List<TimeSlot> findMatchingFreeSlotsOfDuration(Duration duration, List<LocalDayPlan> plans);

    /**
     * This method finds all time gaps that can accommodate an appointment of
     * the given duration in natural order.
     *
     * @param duration the requested duration for an appointment
     * @return a list of gaps in which the appointment can be scheduled.
     */
    List<TimeSlot> findGapsFitting(Duration duration);

    /**
     * Finds all appointments matching given filter.
     *
     * @param filter to determine which items to select.
     * @return list of matching appointments.
     */
    List<Appointment> findAppointments(Predicate<Appointment> filter);

    /**
     * Return a string containing the local date, the time zone and all
     * appointments in natural order, presented with time as local time.
     *
     * @return a string representation of the local day plan.
     */
    @Override
    String toString();

    /**
     * Check if day contains the given appointment.
     *
     * @param appointment to search for.
     * @return true if the Appointment is part of the planning, false otherwise.
     */
    boolean contains(Appointment appointment);

    /**
     * What is the (start) date of this plan.
     *
     * @return the date according to this LocalDayPlan's time zone
     */
    default LocalDate date() {
        return day().date();
    }

    /**
     * Returns the number of appointments on a day.
     *
     * @return Number of appointments on this plan.
     */
    int nrOfAppointments();

    /**
     * Get the instant at the given hour and minute of this local day plan.
     *
     * @param hour sic
     * @param minute sic
     * @return the point in time as Instant
     */
    default Instant at(int hour, int minute) {
        return day().at(hour, minute);
    }
}
