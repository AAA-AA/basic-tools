package com.github.tools.pub;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Slf4j
public final class Dates {
    private Dates() {

    }

    /**
     * 昨天零点
     *
     * @return
     */
    public static Date yesterday() {
        LocalDateTime startOfDay = LocalDate.now().minusDays(1).atStartOfDay();
        Date yesterDay = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
        return yesterDay;
    }

    /**
     * 今天零点
     *
     * @return
     */
    public static Date today() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        Date today = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
        return today;
    }

    /**
     * 当前时间
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static String format(Date date, Format format) {
        DateFormat dateFormat = new SimpleDateFormat(getPattern(format));
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            log.error("格式化时间错误！formatter: " + format, e);
            throw new IllegalArgumentException("格式化时间错误！请检查输入参数");
        }
    }

    /**
     * 默认格式化为yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String format(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            log.error("格式化时间错误！formatter: " + format, e);
            throw new IllegalArgumentException("格式化时间错误！请检查输入参数");
        }
    }

    /**
     * 默认格式化为yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        DateFormat format = new SimpleDateFormat(Format.DATE_TIME_FORMAT.getValue());
        try {
            return format.format(date);
        } catch (Exception e) {
            log.error("格式化时间错误！date: " + date, e);
            throw new IllegalArgumentException("格式化时间错误！");
        }
    }

    public static Date parse(String datetimeStr, Format format) {
        try {
            String pattern = getPattern(format);
            DateFormat mFormat = new SimpleDateFormat(pattern);
            return mFormat.parse(datetimeStr);
        } catch (ParseException e) {
            log.error("parse date error, str:" + datetimeStr + ", format: " + format, e);
            throw new IllegalArgumentException("时间格式化错误，请检查输入参数！");
        }
    }

    private static String getPattern(Format format) {
        Format[] values = Format.values();
        for (Format f : values) {
            if (format.equals(f)) {
                return f.getValue();
            }
        }
        throw new IllegalArgumentException("未找到对应格式化类型！" + format.getValue());
    }

    public static Date parse(LocalDateTime dateTime) {
        return parse(revert(dateTime));
    }

    /**
     * 默认以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param datetimeStr
     * @return
     */
    public static Date parse(String datetimeStr) {
        return parse(datetimeStr, Format.DATE_TIME_FORMAT);
    }

    /**
     * 默认以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param timestamp
     * @return
     */
    public static Date parse(long timestamp) {
        return new Date(timestamp);
    }

    public static String format(LocalDateTime time, Format format) {
        return DateTimeFormatter.ofPattern(getPattern(format)).format(time);
    }

    public static DateTimeFormatter formatter(Format format) {
        return DateTimeFormatter.ofPattern(getPattern(format));
    }

    public static String format(String src, Format srcFormat, Format dstFormat) {
        return DateTimeFormatter.ofPattern(getPattern(dstFormat)).format(LocalDateTime.parse(src, formatter(srcFormat)));
    }

    public static String format(LocalDateTime time) {
        return format(time, Format.DATE_TIME_FORMAT);
    }

    /**
     * 将毫秒时间戳转为默认字符串
     *
     * @param timestamp
     * @return
     */
    public static String format(long timestamp) {
        LocalDateTime convert = convert(timestamp);
        return format(convert, Format.DATE_TIME_FORMAT);
    }

    /**
     * 将毫秒时间戳转为默认字符串
     *
     * @param timestamp
     * @param format
     * @return
     */
    public static String format(long timestamp,  Format format) {
        LocalDateTime convert = convert(timestamp);
        return format(convert, format);
    }

    public static long revert(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }


    public static LocalDateTime convert(long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), TimeZone.getDefault().toZoneId());
    }

    public static LocalDateTime parseToLDT(String text) {
        return LocalDateTime.parse(standardDateTime(text), DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.CHINA));
    }

    private static String standardDateTime(String text) {
        String standard = text.replaceAll("\\D", "");
        StringBuilder bu = new StringBuilder(standard);

        while (true) {
            while (bu.length() < 14) {
                if (bu.length() == 5 && bu.charAt(4) == '0' || bu.length() == 7 && bu.charAt(6) == '0') {
                    bu.append('1');
                } else {
                    bu.append('0');
                }
            }

            standard = bu.toString();
            if (standard.length() > 14) {
                standard = standard.substring(0, 14);
            }

            return standard;
        }
    }


    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static Date addYears(Date date, int amount) {
        return add(date, 1, amount);
    }

    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    public static Date addWeeks(Date date, int amount) {
        return add(date, 3, amount);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    public static Date addHours(Date date, int amount) {
        return add(date, 11, amount);
    }

    public static Date addMinutes(Date date, int amount) {
        return add(date, 12, amount);
    }

    public static Date addSeconds(Date date, int amount) {
        return add(date, 13, amount);
    }

    public static Date addMilliseconds(Date date, int amount) {
        return add(date, 14, amount);
    }

    private static Date add(Date date, int calendarField, int amount) {
        validateDateNotNull(date);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    public static Date setYears(Date date, int amount) {
        return set(date, 1, amount);
    }

    public static Date setMonths(Date date, int amount) {
        return set(date, 2, amount);
    }

    public static Date setDays(Date date, int amount) {
        return set(date, 5, amount);
    }

    public static Date setHours(Date date, int amount) {
        return set(date, 11, amount);
    }

    public static Date setMinutes(Date date, int amount) {
        return set(date, 12, amount);
    }

    public static Date setSeconds(Date date, int amount) {
        return set(date, 13, amount);
    }

    public static Date setMilliseconds(Date date, int amount) {
        return set(date, 14, amount);
    }

    private static Date set(Date date, int calendarField, int amount) {
        validateDateNotNull(date);
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.set(calendarField, amount);
        return c.getTime();
    }

    private static void validateDateNotNull(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date can't be null");
        }
    }
}
