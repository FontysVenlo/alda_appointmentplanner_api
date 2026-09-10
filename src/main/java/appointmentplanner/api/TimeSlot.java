package appointmentplanner.api;

import java.time.Duration;
import java.time.Instant;

/**
 * A time slot represents an (un)allocated range of time defined as
 * [start,end). The starting square bracket and the parenthesis at the end
 * of [start,end) mean that start is part of the slot, end belongs to the
 * following and is the start thereof. In mathematics such notation is called a
 * half open range.
 *
 * The implementer should implement a proper to string showing start instant,
 * end instant and duration of this slot.
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
     * Get the start of the TimeSlot. The start time is included in the
     * range.
     *
     * @return the start time
     */
    Instant start();

    /**
     * Get the end of the TimeSlot. The end time is NOT included in the
     * range.
     *
     * @return the end time
     */
    Instant end();

    /**
     * Get the duration of this slot.
     * The duration may be equal to "Duration.ZERO", typically used in sentinel
     * values of TimeSlot.
     *
     * @return the duration as Duration
     */
    default Duration duration() {
        return Duration.between(start(), end());
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
     * @param other Timeslot to test
     * @return true if other does not start earlier nor ends earlier than this
     *         time slot.
     */
    /**
     * Does this slot contain the given instant? The slot is half open, so the
     * start is inside and the end is not. Two adjacent slots therefore never
     * both contain the instant where they meet.
     *
     * @param instant the instant to test.
     * @return true when start &lt;= instant &lt; end.
     */
    default boolean contains(Instant instant) {
        return start().compareTo(instant) <= 0 && instant.compareTo(end()) < 0;
    }

    default boolean fits(TimeSlot other) {
        return this.start().compareTo(other.start()) <= 0
                && this.end().compareTo(other.end()) >= 0;
    }

    /**
     * Get End Time of the appointment in the given time zone.
     *
     * @param day for the time
     * @return end Time.
     */

    /**
     * Human readable rendering, for example {@code 09:00-10:30}.
     *
     * @return this slot as text.
     */
    @Override
    String toString();
}
