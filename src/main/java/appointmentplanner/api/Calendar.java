package appointmentplanner.api;

import java.time.Duration;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * One person's schedule: an owner, the {@link Availability} describing when
 * that owner can be booked, and a single continuous {@link TimeLine} holding
 * every appointment on any date.
 *
 * <p>
 * A calendar is not a collection of days. Asking it for free space means asking
 * for free space that is also available, over whatever period is of interest.
 */
public interface Calendar {

    /**
     * Name of the owner, used to address this calendar when planning across
     * several people.
     *
     * @return the owner name, never null or empty.
     */
    String owner();

    /**
     * When this owner can be booked.
     *
     * @return this calendar's availability, never null.
     */
    Availability availability();

    /**
     * The zone this calendar's working hours are expressed in.
     *
     * @return the zone of this calendar's availability.
     */
    default ZoneId zoneId() {
        return availability().zoneId();
    }

    /**
     * Plan the given request somewhere inside the horizon, in a period that is
     * both free and available.
     *
     * @param request what to plan; supplies duration and time preference.
     * @param horizon the period to search. Search on a continuous timeline has
     * to be bounded, and this is that bound.
     * @return the created appointment, or empty when no free and available
     * period of the requested length exists inside the horizon.
     * @throws NullPointerException if any argument is null.
     */
    Optional<Appointment> addAppointment(AppointmentRequest request, TimeSlot horizon);

    /**
     * Every period inside the horizon that is free, available, and at least the
     * requested length. This is the intersection of this calendar's free space
     * with its availability.
     *
     * @param duration the minimum length required.
     * @param horizon the period to search.
     * @return the matching periods, in chronological order, possibly empty.
     * @throws NullPointerException if any argument is null.
     */
    List<TimeSlot> findGapsFitting(Duration duration, TimeSlot horizon);

    /**
     * Free the interval held by the given appointment.
     *
     * @param appointment the appointment to remove.
     * @return the request that produced it, or null when it is not in this
     * calendar.
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
     * Every appointment in this calendar, in chronological order.
     *
     * @return all appointments.
     */
    default List<Appointment> appointments() {
        return findAppointments(appointment -> true);
    }

    /**
     * How many appointments this calendar holds.
     *
     * @return the number of appointments.
     */
    int nrOfAppointments();

    /**
     * Human readable rendering of this calendar.
     *
     * @return this calendar as text.
     */
    @Override
    String toString();
}
