package com.github.tools.pub;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Slf4j
public final class Dates {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_NO_SS = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME_SSSS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private Dates() {

    }

    /**
     * 昨天零点
     *
     * @return
     */
    public static Date yesterday() {
        LocalDateTime startOfDay = LocalDate.now().minusDays(1).atStartOfDay();
        Date today = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
        return new Date();
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

    /**
     * 默认格式化为yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String format(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            log.error("格式化时间错误！formatter: "+ format, e);
            throw new IllegalArgumentException("格式化时间错误！请检查输入参数");
        }
    }
    /**
     * 默认格式化为yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String format(Date date) {
        DateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);
        try {
            return format.format(date);
        } catch (Exception e) {
            log.error("格式化时间错误！date: "+ date, e);
            throw new IllegalArgumentException("格式化时间错误！");
        }
    }

    public static Date parse(String datetimeStr, String pattern) {
        try {
            DateFormat mFormat = new SimpleDateFormat(pattern);
            return mFormat.parse(datetimeStr);
        } catch (ParseException e) {
            log.error("parse date error, str:" + datetimeStr+", pattern: "+ pattern, e);
            throw new IllegalArgumentException("时间格式化错误，请检查输入参数！");
        }
    }

    /**
     * 默认以yyyy-MM-dd HH:mm:ss格式化
     *
     * @param datetimeStr
     * @return
     */
    public static Date parse(String datetimeStr) {
        DateFormat mFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        try {
            return mFormat.parse(datetimeStr);
        } catch (ParseException e) {
            log.error("parse date error, str:" + datetimeStr, e);
            throw new IllegalArgumentException("时间格式化错误，请检查输入参数！");
        }
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

    public static void main(String[] args) {
        System.out.println(Dates.addDays(Dates.now(), 1));
    }

}
