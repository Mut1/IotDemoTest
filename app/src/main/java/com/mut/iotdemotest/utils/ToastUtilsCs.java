package com.mut.iotdemotest.utils;

import android.content.Context;
import android.widget.Toast;

import com.aliyun.alink.linksdk.tools.ThreadTools;

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
}
