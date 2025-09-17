package appointmentplanner.api;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

/**
 * Details of an Appointment or AppointmentRequest.
 *
 * The data include a description and the expected duration. Think of a lesson
 * taking 45 minutes.
 *
 * Another example is having a treatment at a dentist or a beauty parlor. The
 * duration and description are known, but there is no time or date allocated
 * yet.
 */
public interface AppointmentRequest {

    LocalDate localDate();
    
    ZoneId zoneId();
    
    /**
     * Intended start time.
     * @return intended start time of the appointment.
     */
    LocalTime startTime();

    /**
     * Intended end time.
     * @return intended end time of the appointment.
     */
    default LocalTime endTime(){
        return startTime().plus(duration());
    };

    /**
     * The duration of the intended appointment.
     *
     * @return the duration of the appointment request.
     */
    Duration duration();

    /**
     * The description of the appointment.
     *
     * @return non-empty string describing the appointment.
     */
    String description();

    /**
     * Time preference given with this appointment request. The default is
     * TimePreference.UNSPECIFIED.
     *
     * @return the time preference
     */
    default TimePreference timePreference() {
        return TimePreference.UNSPECIFIED;
    }

    String[] calendars();
    
    default List<String> calendarsAsList(){
        return Arrays.asList(calendars());
    };
    
    /**
     * Get the textual representation of AppointmentData. Contains description
     * and duration.
     *
     * @return AppointmentData text.
     */
    @Override
    String toString();
}
