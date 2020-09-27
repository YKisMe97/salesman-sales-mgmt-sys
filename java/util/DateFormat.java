package com.example.priyanka.mapsnearbyplaces.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormat {
    public static String getTodayDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String getYear() {
        return Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
    }

    public static String changeDateTimeFormatByString(String oldFormat, String newFormat, String datetime){
        SimpleDateFormat oldSdf = new SimpleDateFormat(oldFormat);
        SimpleDateFormat newSdf = new SimpleDateFormat(newFormat);
        try {
            return newSdf.format(oldSdf.parse(datetime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateTimeFormatByDate(String format, Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
