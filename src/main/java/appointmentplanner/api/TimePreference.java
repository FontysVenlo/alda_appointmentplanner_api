package appointmentplanner.api;

/**
 * Which of the possible periods to choose.
 *
 * <p>
 * There is deliberately no EARLIEST_AFTER or LATEST_BEFORE. A request carries
 * the horizon it may be planned in, and that already expresses both: "earliest
 * after 14:00" is EARLIEST over a horizon starting at 14:00, and "latest before
 * 11:00" is LATEST over a horizon ending at 11:00.
 */
public enum TimePreference {

    /** The first suitable period in the horizon. */
    EARLIEST,

    /** The last suitable period in the horizon. */
    LATEST,

    /** No preference; treated as EARLIEST. */
    UNSPECIFIED;
}
