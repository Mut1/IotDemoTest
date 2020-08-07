package com.mut.iotdemotest.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtilsCS {

    /**
     *
     * @param time HH:mm:ss
     * @return  now date + HH:mm:ss
     */
    public static String timeplusdate(String time)
    {  Date Timeplusdate=null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        SimpleDateFormat ds = new SimpleDateFormat("yyyy-MM-dd ");
        try {
          Timeplusdate = df.parse(ds.format(now) +time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
      return df.format(Timeplusdate);
    }

    /**
     *
     * yyyy-MM-dd HH:mm:ss --> HH:mm:ss
     * @return HH:mm:ss
     */
    public static String date2time(String date)
    {  Date date1=null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat ds = new SimpleDateFormat("HH:mm:ss");
        try {
            date1 = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ds.format(date1);
    }

    //当前时间串yyyy-MM-dd HH:mm:ss+n个小时
    public static String addDateHour(String time, int hour) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        cal = null;
        return format.format(date);

    }


    //当前时间串HH:mm:ss+n个小时
    public static String addTimeHour(String time, int hour) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        cal = null;
        return format.format(date);

    }

}
