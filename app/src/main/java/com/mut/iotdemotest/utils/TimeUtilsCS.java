package com.mut.iotdemotest.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class TimeUtilsCS {

    /**
     *
     * @param time HH:mm:ss
     * @return  now date + HH:mm:ss
     */
    public static String timeplusdate(String time)
    {
        if (time.equals(""))
        {
            return "1900-01-01 00:00:00";
        }
        else {


            Date Timeplusdate = null;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            SimpleDateFormat ds = new SimpleDateFormat("yyyy-MM-dd ");
            try {
                Timeplusdate = df.parse(ds.format(now) + time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return df.format(Timeplusdate);
        }
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
        if (time.equals(""))
        {
            return "NULL";
        }
        else
        {
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

    }}


    /**
     * 验证日期格式是否满足要求
     *
     * @param str          需要验证的日期格式
     * @param  ，如：（yyyy/MM/dd HH:mm:ss）
     * @return 返回验证结果
     */
    public static boolean isValidTime(String str) {

        // 指定日期格式，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }


    public static boolean isParseDouble(String s)
    {
        Double a;
        try
        {
            a = Double.parseDouble(s);
        }catch(Exception e)
        {
            return false;
        }
        return true;
    }
}
