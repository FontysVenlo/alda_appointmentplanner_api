package appointmentplanner.api;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Calendar {
    
    String owner();
    
    void addZonedDayPlan(ZonedDayPlan zonedDayPlan);
    
    Optional<ZonedDayPlan> getZonedDayPlanOn(LocalDate date);
    
    Optional<Appointment> addAppointment(AppointmentRequest request);
    
    AppointmentRequest removeAppointment(Appointment appointment);
    
    List<AppointmentRequest> removeAppointments(Predicate<Appointment> filter);
    
    List<Appointment> findAppointments(Predicate<Appointment> filter);
    
    // TODO Remove methodes toevoegen
    
    
//    LocalDayPlan createLocalDayPlan(LocalDate date, LocalTime startTime, LocalTime endTime);
//    
//    LocalDayPlan localDayPlanFor(LocalDate date);
//    
//    TimeLine addTimeLine(LocalDate date, LocalTime startTime, LocalTime endTime);
//    
//    TimeLine timeLineFor(LocalDate date);
}
