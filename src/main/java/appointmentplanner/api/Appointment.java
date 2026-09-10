package appointmentplanner.api;

/**
 * A period in absolute time, claimed by the request that asked for it.
 */
public interface Appointment extends TimeSlot {

    /**
     * The request this appointment was planned from.
     *
     * @return the originating request, never null.
     */
    AppointmentRequest appointmentRequest();

    /**
     * Human readable rendering of this appointment.
     *
     * @return this appointment as text.
     */
    @Override
    String toString();
}
