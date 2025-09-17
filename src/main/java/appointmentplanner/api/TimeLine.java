package appointmentplanner.api;

import java.time.Duration;
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
public interface TimeLine extends TimeSlot {

    /**
     * {@inheritDoc} Get the earliest time at which appointments are possible.
     */
    @Override
    LocalTime startTime();

    /**
     * {@inheritDoc} Get the latest time till which appointments are possible.
     */    
    @Override
    LocalTime endTime();

    Optional<Appointment> addAppointment(AppointmentRequest appointmentRequest);
    

    /**
     * Removes the given appointment, returning the AppointmentRequest of that
     * appointment, if found. This day is searched for a non-free time slot
     * matching the appointment. The returned data could be used to re-plan the
     * appointment.
     *
     * @param appointment to remove
     * @return the AppointmentRequest of the removed appointment or null if the
     * appointment is not found.
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
    default List<Appointment> appointments(){
        return findAppointments(app -> true);
    };

    /**
     * This method finds all time gaps that can accommodate an appointment of
     * the given duration in natural order.
     *
     * @param duration the requested duration for an appointment
     * @return a list of gaps in which the appointment can be scheduled.
     * @throws IllegalArgumentException if duration is null or negative.
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
     * Returns the number of appointments on a day.
     *
     * @return Number of appointments on this plan.
     */
    int nrOfAppointments();
}
