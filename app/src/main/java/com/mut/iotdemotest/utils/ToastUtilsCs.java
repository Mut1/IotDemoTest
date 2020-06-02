package com.mut.iotdemotest.utils;

import android.content.Context;
import android.widget.Toast;

import com.aliyun.alink.linksdk.tools.ThreadTools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class ToastUtilsCs  {
    public static  void showToast_success(final Context context , final String message) {
        ThreadTools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(DemoApplication.this, message, Toast.LENGTH_SHORT).show();
                Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();

            }
        });
    }
    public static void showToast_error(final Context context , final String message) {
        ThreadTools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(DemoApplication.this, message, Toast.LENGTH_SHORT).show();
                Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();

            }
        });
    }
    public static void showToast_info(final Context context , final String message) {
        ThreadTools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(DemoApplication.this, message, Toast.LENGTH_SHORT).show();

                Toasty.info(context, message, Toast.LENGTH_SHORT, true).show();
            }
        });
    }
    //当前时间串+n个小时
    public static String addDateMinut(String time, int hour) {
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
    //当前时间串+n个小时
    public static String addDateMinut2(String time, int hour) {
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
}
