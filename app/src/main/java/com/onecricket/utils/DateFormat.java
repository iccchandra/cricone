package com.onecricket.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormat {

    private DateFormat() {}

    public static String getReadableDateFormat(String dateInTime) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date currenTimeZone=new java.util.Date((long)(Long.parseLong(dateInTime))*1000);
// Toast.makeText(context, sdf.format(currenTimeZone), Toast.LENGTH_SHORT).show();
//java.util.Date Time=new java.util.Date((long)(Long.parseLong(matchesInfo.getTime()))*1000);
        return sdf.format(currenTimeZone);
    }

    public static String getReadableTimeFormat(String dateInTime) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date time = new Date((long) (Long.parseLong(dateInTime)) * 1000);
        return timeSdf.format(time);
    }
}
