package dev.scx.http.routing.x.static_files.http_date;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class HttpDateHelper {

    public static Instant parse(String v) throws IllegalHttpDateException {
        try {
            return ZonedDateTime
                .parse(v, DateTimeFormatter.RFC_1123_DATE_TIME)
                .toInstant();
        } catch (Exception e) {
            throw new IllegalHttpDateException("Invalid HTTP-date: " + v, e);
        }
    }

    public static String encode(Instant instant) {
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(
            instant.truncatedTo(ChronoUnit.SECONDS).atZone(ZoneOffset.UTC)
        );
    }

}
