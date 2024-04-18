package appointmentplanner.api;

import java.time.Duration;

/**
 * Details of an Appointment or AppointmentRequest.
 *
 * The data include a description and the expected duration. Think of a lesson
 * taking 45 minutes.
 *
 * Another example is having a treatment at a dentist or a beauty parlor. The
 * duration, priority, and description are known, but there is no time or date
 * allocated yet.
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
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
     * Get the priority for the appointment.
     *
     * @return the priority
     */
    Priority priority();

    /**
     * Get the textual representation of AppointmentData. Contains description,
     * duration and priority.
     *
     * @return AppointmentData text.
     */
    @Override
    String toString();

    /**
     * Defines equality. Must be based on all fields of this class.
     *
     * @param obj the other object to check equality with
     * @return true if the two appointments are equal
     */
    @Override
    public boolean equals(Object obj);

    /**
     * Calculate a hash code value for the object.
     *
     * @return hashCode for this object
     */
    @Override
    public int hashCode();
}
