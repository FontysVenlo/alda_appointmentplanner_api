package appointmentplanner.api;

import java.time.Duration;

/**
 * Details of an Appointment or AppointmentRequest.
 *
 * The data include a description and the expected duration. Think of a lesson
 * taking 45 minutes.
 *
 * Another example is having a treatment at a dentist or a beauty parlor. The
 * duration and description are known, but there is no time or date
 * allocated yet.
 */
public interface AppointmentData {

    /**
     * The duration of this appointment data.
     *
     * @return the duration of the appointment.
     */
    Duration duration();

    /**
     * The description of the appointment.
     *
     * @return non-empty string describing the appointment.
     */
    String description();

    /**
     * Get the textual representation of AppointmentData. Contains description and duration.
     *
     * @return AppointmentData text.
     */
    @Override
    String toString();
}
