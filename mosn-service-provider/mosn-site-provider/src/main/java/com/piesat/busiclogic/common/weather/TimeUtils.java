package com.piesat.busiclogic.common.weather;

import com.piesat.busiclogic.common.weather.calculator.SunriseSunsetCalculator;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;

public abstract class TimeUtils {
    public static Date[] calcSunriseAndSunset(int year, int month, int day, double lat, double lon) {
        Location location = new Location(lat, lon);
        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, "Asia/Harbin");
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        String sunriseStr = calculator.getOfficialSunriseForDate(cal);
        String sunsetStr = calculator.getOfficialSunsetForDate(cal);

        Calendar sunrise = Calendar.getInstance();
        sunrise.set(year, month - 1, day, Integer.parseInt(StringUtils.split(sunriseStr, ":")[0]), Integer.parseInt(StringUtils.split(sunriseStr, ":")[1]), 0);

        Calendar sunset = Calendar.getInstance();
        sunset.set(year, month - 1, day, Integer.parseInt(StringUtils.split(sunsetStr, ":")[0]), Integer.parseInt(StringUtils.split(sunsetStr, ":")[1]), 0);
        return new Date[]{sunrise.getTime(), sunset.getTime()};
    }

//
//    public static void main(String[] args) {
//        System.out.println(TimeUtils.calcSunriseAndSunset(2020,12,18,31.86,117.27).toString());
//    }
}
