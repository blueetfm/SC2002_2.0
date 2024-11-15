package Utils;

import java.time.format.DateTimeFormatter;

public class DateTimeFormatUtils {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Private constructor to prevent instantiation
    private DateTimeFormatUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
}
