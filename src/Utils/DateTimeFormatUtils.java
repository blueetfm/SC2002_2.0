/**
 * The {@code DateTimeFormatUtils} class provides utility constants for common date and time formatting patterns.
 * This class includes pre-configured {@link DateTimeFormatter} instances for formatting and parsing dates and times.
 * 
 * <p>These formatters are commonly used for handling dates and times in the "yyyy-MM-dd" (date) and "HH:mm" (time) formats.</p>
 * 
 * <p>This class cannot be instantiated, as it is designed to be a utility class that provides static constants and methods.</p>
 * @author SCSCGroup4
 * @version 1.0
 * @since 2024-11-21
 */
package Utils;

import java.time.format.DateTimeFormatter;


public class DateTimeFormatUtils {

    /**
     * A {@link DateTimeFormatter} instance for formatting and parsing dates in the "yyyy-MM-dd" format.
     * This format represents the year, month, and day (e.g., "2024-11-22").
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * LocalDate date = LocalDate.parse("2024-11-22", DateTimeFormatUtils.DATE_FORMATTER);
     * String formattedDate = date.format(DateTimeFormatUtils.DATE_FORMATTER);
     * }</pre>
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * A {@link DateTimeFormatter} instance for formatting and parsing times in the "HH:mm" format.
     * This format represents hours and minutes in 24-hour time (e.g., "14:30" for 2:30 PM).
     * 
     * <p>Example usage:</p>
     * <pre>{@code
     * LocalTime time = LocalTime.parse("14:30", DateTimeFormatUtils.TIME_FORMATTER);
     * String formattedTime = time.format(DateTimeFormatUtils.TIME_FORMATTER);
     * }</pre>
     */
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Private constructor to prevent instantiation of this utility class.
     * 
     * @throws UnsupportedOperationException If invoked, this will throw an exception, as instantiation is not allowed.
     */
    private DateTimeFormatUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
}
