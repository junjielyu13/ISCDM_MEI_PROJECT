package util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class DateTimeUtil {

    public static String formatIsoToLocal(String isoTimestamp) {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (isoTimestamp == null || isoTimestamp.isEmpty()) {
            throw new IllegalArgumentException("String not be null");
        }

        Instant instant = Instant.parse(isoTimestamp.substring(0, 23) + "Z");
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime.format(FORMATTER);
    }
}

