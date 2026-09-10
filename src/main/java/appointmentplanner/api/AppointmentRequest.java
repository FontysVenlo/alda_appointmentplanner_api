package appointmentplanner.api;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

/**
 * Class holding the full details of an appointment request.
 *
 * Note that an appointment request is NOT an appointment but only an expression
 * of intent!
 * 
 * The AppointmentRequest is not input for the external API, but it is returned
 * when removing Appointment(s). The idea is that, based on the AppointmentRequest
 * of the removed Appointment, the appointment could be rescheduled.
 * 
 * The AppointmentRequest is mainly used internally in the TimeLine, when an 
 * actual appointment is created.
 * 
 * The AppointmentRequest holds LocalTime startTime, AppointmentData and 
 * TimePreference. Normally, either startTime or TimePreference is not null. If
 * no TimePreference is set, it defaults to TimePreference.UNSPECIFIED. An 
 * AppointmentRequest with no startTime defined and with TimePreference 
 * UNSPECIFIED means that the invoker does not have any preference regarding
 * time. It's up to the addAppointment method to decide how to deal with such a
 * request.
 */
public interface AppointmentRequest {
    
    //TODO Specify how to deal with TimePreferences EARLIEST_AFTER and LATEST_BEFORE together with startTime null
    

    /**
     * Get the start time of the intended appointment.
     * If the time is not specified, this method may return null.
     *
     * @param onDay the LocalDay the time is on.
     * @return the start time as instant, potentially null.
     */
    default Instant start(LocalDay onDay) {
        return onDay.ofLocalTime(startTime());
    }

    /**
     * Get the requested local start time.
     *
     * @return the start time
     */
    LocalTime startTime();

    /**
     * Get the appointment details of this appointment.
     *
     * @return the data
     */
    AppointmentData appointmentData();

    /**
     * Time preference given with this appointment request.
     * The default is TimePreference.UNSPECIFIED.
     *
     * @return the time preference
     */
    default TimePreference timePreference() {
        return TimePreference.UNSPECIFIED;
    }

    /**
     * Get the duration of the appointment request.
     * @return the duration of the request
     */
    default Duration duration() {
        return appointmentData().duration();
    }

    /**
     * What the appointment is for.
     *
     * @return the description, never null or empty.
     */
    default String description() {
        return appointmentData().description();
    }

    /**
     * The period to search when planning this request. A continuous timeline
     * has no end, so a search has to be bounded, and this is that bound.
     *
     * <p>
     * The horizon also subsumes what used to need dedicated time preferences:
     * "earliest after 14:00" is EARLIEST within a horizon starting at 14:00,
     * and "latest before 16:00" is LATEST within a horizon ending at 16:00.
     *
     * @return the horizon, or null for a request built through the day-plan
     * API that is on its way out.
     */
    default TimeSlot horizon() {
        return null;
    }

    /**
     * Names of the calendars that must all attend. A request naming several
     * owners is only satisfiable in a period that is free and available in
     * every one of them.
     *
     * @return the calendar owners, empty for a request built through the
     * day-plan API that is on its way out.
     */
    default List<String> calendars() {
        return List.of();
    }
}
