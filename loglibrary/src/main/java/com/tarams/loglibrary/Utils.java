package com.tarams.loglibrary;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
    static TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
    static final String DEFAULT_DATE_TIME_PATTERN = "dd/MM/yyyy'T'HH:mm:ss.SSSS a";

    public static String getCurrentTime() {
        return getCurrentTime(DEFAULT_DATE_TIME_PATTERN, UTC_TIME_ZONE);
    }

    private static String getCurrentTime(String defaultDateTimePattern, TimeZone utcTimeZone) {
        SimpleDateFormat format = new SimpleDateFormat(defaultDateTimePattern);
        format.setTimeZone(utcTimeZone);
        return format.format(Calendar.getInstance().getTime());
    }

    public static String getUTCtime() {
        Date dateNow = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_TIME_PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(dateNow);
    }
}
