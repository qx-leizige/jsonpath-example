package qx.leizige.utils;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class TimeUtils implements StringPool {

    public static String toString(LocalDate localDate) {
        Objects.requireNonNull(localDate, "localDate");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN);
        return localDate.format(fmt);
    }

    /**
     * @param localDateTime localDateTime
     * @param pattern       "yyyy-MM-dd hh:mm:ss" / "yyyy-MM-dd"
     * @return string
     */
    public static String toString(LocalDateTime localDateTime, String pattern) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        if (Objects.isNull(pattern))
            pattern = DATE_TIME_FORMATTER_PATTERN;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(fmt);
    }

    public static Long toLong(LocalDateTime localDateTime) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDateTime toLocalDateTime(String text) {
        Objects.requireNonNull(text, "text");
        try {
            Long time = Long.parseLong(text);
            return toLocalDateTime(time);
        }
        catch (Exception e) {
            DateTimeFormatter formatter;
            if (text.contains("T") && text.contains("Z")) {
                formatter = DateTimeFormatter.ofPattern(T_DATE_TIME_FORMATTER_PATTERN);
            }
            else {
                formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER_PATTERN);
            }
            return LocalDateTime.parse(text, formatter);
        }
    }

    public static LocalDate toLocalDate(String text) {
        Objects.requireNonNull(text, "text");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN);
        return LocalDate.parse(text, formatter);
    }

    /**
     *时间戳转换为日期类型格式
     * @param time 1612518379000
     * @return 2021-02-05 05:46:19
     */
    public static LocalDateTime toLocalDateTime(Long time) {
        Instant instant = Instant.ofEpochMilli(time);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static void main(String[] args) {
        System.out.println(toLocalDateTime(1612518379000L));
    }
}
