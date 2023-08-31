package appointmentplanner.api;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;

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
 * 
 * @author Pieter van den Hombergh
 * @author Richard van den Ham
 */
public interface AppointmentRequest extends AppointmentData {
    
    //TODO Specify how to deal with TimePreferences EARLIEST_AFTER and LATEST_BEFORE together with startTime null
    

    /**
     * Get the start time of the intended appointment.
     * If the time is not specified, this method may return null.
     * @param onDay the LocalDay the time is on.
     * @return the start time as instant, potentially null.
     */
    default Instant getStart( LocalDay onDay ) {
        return onDay.ofLocalTime( getStartTime() );
    }

    /**
     * Get the requested local start time.
     *
     * @return the start time
     */
    LocalTime getStartTime();

    /**
     * Get the appointment details of this appointment.
     *
     * @return the data
     */
    AppointmentData getAppointmentData();

    /**
     * Time preference given with this appointment request.
     * The default is TimePreference.UNSPECIFIED.
     *
     * @return the time preference
     */
    default TimePreference getTimePreference() {
        return TimePreference.UNSPECIFIED;
    }

    /**
     * Get the duration of the appointment request.
     * @return the duration of the request
     */
    @Override
    default Duration getDuration() {
        return getAppointmentData().getDuration();
    }
    
    /**
     * Defines equality, must be based on all fields of this class.
     * @param obj the other object to check equality with
     * @return true if the two appointments are equal
     */
    @Override
    public boolean equals( Object obj );
    
    /**
     * Calculate a hash code value for the object.
     * @return hashCode for this object.
     */
    @Override
    public int hashCode();
    
    
}
