package com.waypointer.gpsfileconverter;

import java.util.*;

public final class TestUtils {

    private final static Random random = new Random();

    public static Double randomLatitude() {
        return (random.nextDouble() * 180) - 90;
    }

    public static Double randomLongitude() {
        return (random.nextDouble() * 360) - 180;
    }

    //todo: replace with Java8 date
    public static Date getDate(Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        return cal.getTime();
    }

}
