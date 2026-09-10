package appointmentplanner.api;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Plans appointments across the calendars of several people.
 *
 * <p>
 * A request names the calendars that must all attend. The planner satisfies it
 * only in a period that is simultaneously free and available in every one of
 * them, which is the interesting problem: each calendar carries its own
 * availability in its own time zone, so the common periods have to be found in
 * absolute time.
 *
 * <p>
 * An appointment booked into several calendars is one appointment, not one per
 * attendee. It is reported once and removed everywhere.
 */
public interface AppointmentPlanner {

    /**
     * Register a calendar, addressable afterwards by its owner's name.
     *
     * @param calendar the calendar to add.
     * @throws NullPointerException if calendar is null.
     * @throws IllegalArgumentException if a calendar for that owner is already
     * registered.
     */
    void addCalendar(Calendar calendar);

    /**
     * The calendar belonging to the given owner.
     *
     * @param owner name of the owner.
     * @return that owner's calendar.
     * @throws IllegalArgumentException if no calendar is registered for that
     * owner.
     */
    Calendar getCalendar(String owner);

    /**
     * Every period inside the request's horizon that is long enough and is both
     * free and available in all of the calendars the request names.
     *
     * <p>
     * With one calendar this is that calendar's own free and available periods.
     * With several it is their intersection, which may well be empty: two
     * working days far enough apart in time zone simply do not overlap, and an
     * empty result is the correct answer rather than an error.
     *
     * @param request what to plan; supplies duration, horizon and calendars.
     * @return the possible periods, in chronological order, possibly empty.
     * @throws NullPointerException if request is null.
     * @throws IllegalArgumentException if the request names no calendars, or
     * names one that is not registered.
     */
    List<TimeSlot> findAppointmentPossibilities(AppointmentRequest request);

    /**
     * Plan the request in all of the calendars it names, choosing among the
     * possibilities according to its time preference.
     *
     * @param request what to plan.
     * @return the appointment, or empty when no common period exists.
     * @throws NullPointerException if request is null.
     * @throws IllegalArgumentException if the request names no calendars, or
     * names one that is not registered.
     */
    Optional<Appointment> addAppointment(AppointmentRequest request);

    /**
     * Remove the appointment from every calendar that holds it.
     *
     * @param appointment the appointment to remove.
     * @return the request that produced it, or null when no calendar held it.
     */
    AppointmentRequest removeAppointment(Appointment appointment);

    /**
     * Remove every appointment matching the filter, from every calendar.
     *
     * @param filter selects the appointments to remove.
     * @return the requests of the removed appointments, each once.
     */
    List<AppointmentRequest> removeAppointments(Predicate<Appointment> filter);

    /**
     * Every appointment matching the filter, in chronological order. An
     * appointment shared by several calendars is reported once.
     *
     * @param filter selects the appointments to return.
     * @return the matching appointments.
     */
    List<Appointment> findAppointments(Predicate<Appointment> filter);

    /**
     * Every appointment known to this planner, each once.
     *
     * @return all appointments, in chronological order.
     */
    default List<Appointment> appointments() {
        return findAppointments(appointment -> true);
    }

    /**
     * How many distinct appointments this planner holds. A meeting shared by
     * three people counts once.
     *
     * @return the number of appointments.
     */
    int nrOfAppointments();
}
