/*
 * Copyright (c) 2019 Informatics Fontys FHTenL University of Applied Science Venlo
 */
package appointmentplanner.api;

import java.time.Duration;

/**
 * Appointment that is scheduled using the appointment planner.
 *
 * An Appointment is not created from 'the outside API', but created by the
 * appointment planner service, being the actual TimeLine implementation. In
 * the TimeLine, you'll determine if a request can be accepted and an
 * Appointment object can be created. Having an appointment therefore implies
 * that there has been a successful allocation on a day at a time. The
 * appointment is only valid when it is allocated on some timeline.
 *
 * An appointment holds an AppointmentRequest and a start Instant.
 *
 * @author Richard van den Ham {@code r.vandenham@fontys.nl}
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public interface Appointment extends TimeSlot, AppointmentData {

    /**
     * Get the priority of this appointment.
     *
     * @return Priority.
     */
    @Override
    Priority priority();

    /**
     * Get the appointment data for this appointment.
     *
     * @return the data
     */
    AppointmentData appointmentData();

    /**
     * Get the request that led to this appointment.
     *
     * @return the request.
     */
    AppointmentRequest request();

    /**
     * Override the default toString. toString() returns startTime, endTime,
     * description and priority like: "2020-09-12 14:00 - 15:55 ALDA Lesson
     * (HIGH)" This will make your testing and debugging life so much easier.
     *
     * @return String representation of Appointment.
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

    /**
     * Resolves diamond problem. Git the TimeSlot implementation preference.
     *
     * @return the duration of the time slot.
     */
    @Override
    default Duration duration() {
        return TimeSlot.super.duration();
    }
}
