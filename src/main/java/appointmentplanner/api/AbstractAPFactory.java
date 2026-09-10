package appointmentplanner.api;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * This is the interface that the user (the teachers) of your implementation use
 * to obtain instances of the classes implementing the given other interfaces.
 */
public interface AbstractAPFactory {

    AppointmentPlanner createAppointmentPlanner();
 
    Calendar createCalendar(String owner);
    
    ZonedTimeLine createZonedDayPlan(
            ZoneId zoneId, 
            LocalDate localDate, 
            LocalTime startTime, 
            LocalTime endTime);
    
    TimeLine createTimeLine(
            LocalTime startTime,
            LocalTime endTime);

    /**
     * Factory method to create an AppointmentData object without specified
     * startTime. The start time will be set (in an Appointment) as soon as the
     * appointment is added to the Day schedule.
     *
     * @param localDate
     * @param zoneId
     * @param startTime
     * @param durationInMinutes
     * @param timePreference
     * @param description
     * @param calendars
     * @return 
     * @throws NullPointerException if any of the parameters are null
     */
    AppointmentRequest createAppointmentRequest(
            LocalDate localDate,
            ZoneId zoneId, 
            LocalTime startTime, 
            Duration durationInMinutes, 
            TimePreference timePreference, 
            String description,
            String... calendars);
}