/*
 * Copyright (c) 2019 Informatics Fontys FHTenL University of Applied Science Venlo.
 */
package appointmentplanner.api;

/**
 * Strategy to find a Time slot to add an Appointment.
 *
 * @author Richard van den Ham {@code r.vandenham@fontys.nl}
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public enum TimePreference {
    /**
     * Plan the appointment as early as possible on a day.
     * This preference can work without a fixed time.
     */
    EARLIEST,
    /**
     * Plan as late as possible on a day.
     * This preference can work without a fixed time.
     */
    LATEST,
    /**
     * In case no time preference is specified, this is the default.
     * In general cases this will fall back to EARLIEST
     */
    UNSPECIFIED,
    /**
     * Plan as late as possible before a given time.
     * This preference cannot be used without a fixed time.
     *
     * NOTE: this preference behaves differently from other preferences,
     * as it is the only preference where the appointment should not be planned on the fixed time if possible.
     * The entire appointment should fit BEFORE the fixed time (so the appointment should end before the fixed time).
     */
    LATEST_BEFORE,
    /**
     * Plan as early as possible after a given time.
     * This preference cannot be used without a fixed time.
     *
     * NOTE: if planning is possible at the given time, then that is a valid earliest after.
     */
    EARLIEST_AFTER;
}
