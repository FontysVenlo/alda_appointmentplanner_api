package appointmentplanner.api;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A continuous, unbroken sequence of allocated and free intervals in absolute
 * time. Unlike the day plan it replaces, a timeline has no start, no end, no
 * date and no time zone: midnight is not a boundary but an ordinary point on
 * the line.
 *
 * <p>
 * Because a timeline is unbounded, every operation that has to search it takes
 * the bounded window to search inside. That window normally comes from a
 * {@link Calendar}'s {@link Availability}.
 */
public interface TimeLine {

    /**
     * Allocate the given request inside the given window, honouring the
     * request's {@link AppointmentRequest#timePreference()}.
     *
     * @param request what to plan; supplies duration and time preference.
     * @param within the bounded window to search, typically one availability
     * window. Nothing is ever allocated outside it.
     * @return the created appointment, or empty when the request does not fit
     * inside the window.
     * @throws NullPointerException if any argument is null.
     */
    Optional<Appointment> addAppointment(AppointmentRequest request, TimeSlot within);

    /**
     * All free intervals of at least the given duration inside the given
     * window, in chronological order. Gaps are clipped to the window.
     *
     * @param duration the minimum length required.
     * @param within the bounded window to search.
     * @return the matching free intervals, possibly empty.
     * @throws NullPointerException if any argument is null.
     */
    List<TimeSlot> findGapsFitting(Duration duration, TimeSlot within);

    /**
     * Free the interval held by the given appointment.
     *
     * @param appointment the appointment to remove.
     * @return the request that produced it, or null when it is not on this
     * timeline.
     */
    AppointmentRequest removeAppointment(Appointment appointment);

    /**
     * Remove every appointment matching the filter.
     *
     * @param filter selects the appointments to remove.
     * @return the requests of the removed appointments.
     */
    List<AppointmentRequest> removeAppointments(Predicate<Appointment> filter);

    /**
     * Every appointment matching the filter, in chronological order.
     *
     * @param filter selects the appointments to return.
     * @return the matching appointments.
     */
    List<Appointment> findAppointments(Predicate<Appointment> filter);

    /**
     * Every appointment on this timeline, in chronological order.
     *
     * @return all appointments.
     */
    default List<Appointment> appointments() {
        return findAppointments(appointment -> true);
    }

    /**
     * Every appointment on this timeline as a stream, in chronological order.
     *
     * @return all appointments, streamed.
     */
    Stream<Appointment> appointmentStream();

    /**
     * How many appointments this timeline holds.
     *
     * @return the number of appointments.
     */
    int nrOfAppointments();

    /**
     * Is this appointment part of this timeline?
     *
     * @param appointment the appointment to look for.
     * @return true when it is planned here.
     */
    boolean contains(Appointment appointment);

    /**
     * Human readable rendering of the allocated and free intervals.
     *
     * @return this timeline as text.
     */
    @Override
    String toString();
}
