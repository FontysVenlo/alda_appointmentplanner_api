package appointmentplanner.api;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface AppointmentPlanner {

    void addCalendar(Calendar calendar);
    
    Calendar getCalendar(String owner);

    Optional<Appointment> addAppointment(AppointmentRequest request);
    

    AppointmentRequest removeAppointment(Appointment appointment);

    /**
     * Removes appointments with description that matches a filter.
     *
     * @param filter to determine which items to remove.
     * @return the list of AppointmentRequests of removed appointments.
     */
    List<AppointmentRequest> removeAppointments(Predicate<Appointment> filter);


    /**
     * Finds all appointments matching given filter.
     *
     * @param filter to determine which items to select.
     * @return list of matching appointments.
     */
    List<Appointment> findAppointments(Predicate<Appointment> filter);

}
