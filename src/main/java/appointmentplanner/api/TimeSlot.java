package appointmentplanner.api;

import java.time.Duration;
import java.time.LocalTime;

/**
 * A time slot represents an (un)allocated range of time defined as
 * [start,end). The starting square bracket and the parenthesis at the end
 * of [start,end) mean that start is part of the slot, end belongs to the
 * following and is the start thereof. In mathematics such notation is called a
 * half open range.
 *
 * The implementer should implement a proper to string showing start time (localTime),
 * end time (LocalTime) and duration of this slot.
 * 
 * The time slots are comparable by length of the slot only!
 * If you keep the slots in a linked list there is no need to compare them
 * by start or end time, because the list will keep them in natural order.
 * The end should never be before start, however a TimeSlot with start and
 * end equal, such that it has an effective duration of
 * zero minutes, zero nanoseconds may have its use as for instance a sentinel
 * value.
 */
public interface TimeSlot extends Comparable<TimeSlot> {

    /**
     * Get start time of this time slot.
     *
     * @return start Time.
     */
    LocalTime startTime();
    
    /**
     * Get end time of this time slot.
     *
     * @return end Time.
     */
    LocalTime endTime();
    
    /**
     * Get the duration of this slot.
     * The duration may be equal to "Duration.ZERO" if used in sentinel
     * values of TimeSlot.
     *
     * @return the duration as Duration
     */
    default Duration duration() {
        return Duration.between(startTime(), endTime());
    }

    /**
     * Compare two time slots by length.
     *
     * @param other TimeSlot to compare with this one.
     * @return comparison result, less than 0, 0 or greater 0.
     */
    @Override
    public default int compareTo(TimeSlot other) {
        return this.duration().compareTo(other.duration());
    }

    /**
     * Is this time slot sufficient to accommodate a specified duration.
     *
     * @param duration to test
     * @return true if start and end are sufficiently apart to fit the given
     *         duration.
     */
    default boolean fits(Duration duration) {
        return this.duration().compareTo(duration) >= 0;
    }

    /**
     * Does the given time slot fit inside this time slot.
     *
     * @param other TimeSlot to test
     * @return true if other does not start earlier nor ends later than this time slot.
     */
    default boolean fits(TimeSlot other) {
        return this.startTime().compareTo(other.startTime()) <= 0
                && this.endTime().compareTo(other.endTime()) >= 0;
    }
    
    /**
     * Describes the TimeSlot.
     * Start and end times are displayed as in "08:30-10:00". 
     * @return String describing the TimeSlot.
     */
    @Override
    String toString();
}
