package appointmentplanner.api;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

/**
 * A recurring window of wall-clock time within a single day, such as
 * 08:30-17:30. A window carries no date and no time zone; it becomes a
 * concrete interval only when an {@link Availability} resolves it against a
 * date in its own zone.
 *
 * <p>
 * A window may not cross midnight: {@code end} must be strictly after
 * {@code start}. This is the single place where that rule is enforced, and it
 * is why appointments can never span midnight - a request is only ever placed
 * inside a resolved window.
 *
 * @param start first moment of the window, inclusive.
 * @param end moment the window closes, exclusive. Must be after start.
 */
public record LocalTimeWindow(LocalTime start, LocalTime end)
        implements Comparable<LocalTimeWindow> {

    /**
     * Validating constructor.
     *
     * @throws NullPointerException if either bound is null.
     * @throws IllegalArgumentException if end is not after start, which would
     * mean the window crosses or touches midnight.
     */
    public LocalTimeWindow {
        Objects.requireNonNull(start, "start is null");
        Objects.requireNonNull(end, "end is null");
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException(
                    "a window must end after it starts and may not cross midnight, got "
                    + start + "-" + end);
        }
    }

    /**
     * The wall-clock length of this window.
     *
     * @return the duration between start and end.
     */
    public Duration duration() {
        return Duration.between(start, end);
    }

    /**
     * Order windows by start time, then by end time.
     *
     * @param other window to compare to.
     * @return negative, zero or positive as usual.
     */
    @Override
    public int compareTo(LocalTimeWindow other) {
        int byStart = this.start.compareTo(other.start);
        return byStart != 0 ? byStart : this.end.compareTo(other.end);
    }

    /**
     * Textual form, e.g. {@code 08:30-17:30}.
     *
     * @return this window as text.
     */
    @Override
    public String toString() {
        return start + "-" + end;
    }
}
