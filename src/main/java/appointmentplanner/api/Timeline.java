/*
 * Copyright (c) 2019 Informatics Fontys FHTenL University of Applied Science Venlo
 */
package appointmentplanner.api;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Timeline of planned appointments.
 *
 * The implementation of the Timeline MUST use YOUR OWN linked list
 * implementation for its internal structure. Existing implementations or
 * arrays/lists are not allowed.
 *
 * However, in the methods returning lists, collections, etc. produced by
 * streams, the usual java classes of such kind are allowed. No need to reinvent
 * all wheels.
 *
 * @author Richard van den Ham {@code r.vandenham@fontys.nl}
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 *
 */
public interface Timeline {

    /**
     * Returns the number of appointments on a day.
     *
     * @return Number of appointments on this timeline.
     */
    int getNrOfAppointments();

    /**
     * Get the number of gaps between start and en of day and between the
     * appointments. The standard day has 4 gaps, form 8:30 to 9:00, from 10:00
     * to 10:30, from 11:00 to 11:10 and from 15:00 to 16:00 An empty day has 1
     * gap, duration the whole time between day start and day end.
     *
     * @return number of gaps between appointments and before and after.
     */
    int nrOfGaps();

    /**
     * Get the start of this timeline as instant.
     *
     * @return the start time of this timeline
     */
    Instant start();

    /**
     * Get the end of this timeline.
     *
     * @return the end time of this timeline
     */
    Instant end();

    /**
     * Add a new appointment to this day.
     *
     * Requirements:
     *
     * An appointment can only be added between start time (including) and
     * end time (excluding) of the day.
     *
     * AppointmentData having a duration greater than the length of the day in
     * minutes will result in an empty Optional to be returned.
     *
     * If the day does not have free space to accommodate the appointment,
     * an empty optional is returned.
     *
     * Appointments aren't allowed to overlap.
     *
     * Not all time preferences make sense without a fixed time. If the time preference is not equal to LATEST then
     * the time preference is set to EARLIEST.
     *
     * @param forDay time partition to fit appointment
     * @param appointment the appointment to add
     * @param timePreference fallback strategy, if not LATEST, than set it to EARLIEST for all other preferences
     * @return Appointment instance with all fields set according to
     * AppointmentData, or empty Optional if the constraints of this day and the requested
     * appointment can't be met.
     * @throws NullPointerException If the appointmentData is null
     */
    Optional<Appointment> addAppointment(LocalDay forDay, AppointmentData appointment, TimePreference timePreference);

    /**
     * Add appointment with a fixed time. If the requested slot is available,
     * that is used and the appointment is returned. Otherwise, an empty Optional
     * is returned.
     *
     * @param forDay time partition to fit appointment
     * @param appointment to add
     * @param startTime preferred start time of the appointment
     * @return the added appointment or an empty Optional on failure.
     */
    Optional<Appointment> addAppointment(LocalDay forDay, AppointmentData appointment, LocalTime startTime);

    /**
     * Create an appointment based on previous appointmentRequest.
     *
     * @param forDay time partition to fit appointment
     * @param appointmentRequest for this appointment.
     * @return the added appointment or an empty Optional on failure.
     */
    default Optional<Appointment> addAppointment(LocalDay forDay,
            AppointmentRequest appointmentRequest) {
        return addAppointment(forDay, appointmentRequest.getAppointmentData(),
                appointmentRequest.getStartTime());
    }

    /**
     * Add appointment with a fixed time. If the requested slot is available,
     * that is used and the appointment is returned. Otherwise, the fallback
     * time preference is tried.
     *
     * @param forDay time partition to fit appointment
     * @param appointment to add
     * @param startTime preferred start time of the appointment
     * @param fallback time preference as fall back if the fixed time does not
     * apply.
     * @return the added appointment or an empty Optional on failure.
     */
    Optional<Appointment> addAppointment(LocalDay forDay, AppointmentData appointment,
            LocalTime startTime, TimePreference fallback);

    /**
     * Removes the given appointment, returning the AppointmentRequest of that appointment, if
     * found. This day is searched for a non-free time slot matching the
     * appointment. The returned data could be used to re-plan the appointment.
     *
     * @param appointment to remove
     * @return the AppointmentRequest of the removed appointment or null
     * if the appointment is not found.
     */
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

    /**
     * Finds all appointments for this TimeLine.
     *
     * @return list of all appointments.
     */
    default List<Appointment> getAppointments() {
        return appointmentStream().collect(Collectors.toList());
    }

    /**
     * Stream of all appointments.
     *
     * @return a stream of all appointments.
     */
    Stream<Appointment> appointmentStream();

    /**
     * Check if day contains the given appointment.
     *
     * @param appointment to search for.
     * @return true if the Appointment is part of the Timeline, false otherwise.
     */
    boolean contains(Appointment appointment);


    /**
     * This method finds all time gaps that can accommodate an appointment of
     * the given duration in natural order.
     *
     * @param duration the requested duration for an appointment
     * @return a list of gaps in which the appointment can be scheduled.
     */
    List<TimeSlot> getGapsFitting(Duration duration);

    /**
     * Check if an appointment of the given duration can be scheduled.
     *
     * @param duration of the appointment
     * @return true is there is a sufficiently big gap, false otherwise
     */
    boolean canAddAppointmentOfDuration(Duration duration);

    /**
     * This method finds all time gaps that can accommodate an appointment of
     * the given duration in last to first order.
     *
     * @param duration the requested duration for an appointment
     * @return a list of start times on which an appointment can be scheduled
     */
    List<TimeSlot> getGapsFittingReversed(Duration duration);

    /**
     * Get the gaps matching the given duration, smallest fitting first.
     *
     * @param duration required
     * @return list of all gaps fitting, ordered, smallest gap first.
     */
    List<TimeSlot> getGapsFittingSmallestFirst(Duration duration);

    /**
     * Get the gaps matching the given duration, largest fitting first.
     *
     * @param duration required
     * @return list of all gaps fitting, ordered, largest gap first.
     */
    List<TimeSlot> getGapsFittingLargestFirst(Duration duration);

    /**
     * Find matching free time slots in this and other TimeLines. To facilitate
     * appointment proposals.
     *
     * @param minLength minimum length required.
     * @param other day plans
     * @return the list of free slots that all DayPlans share.
     */
    List<TimeSlot> getMatchingFreeSlotsOfDuration(Duration minLength, List<Timeline> other);
}
