package com.mut.iotdemotest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.linksdk.tools.ThreadTools;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class BaseActivity extends AppCompatActivity {
    protected static SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

    protected String logStr = null;
    protected TextView textView = null;
    protected String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }


    public void showToast_success(final String message) {
        ThreadTools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(DemoApplication.this, message, Toast.LENGTH_SHORT).show();
                Toasty.success(getBaseContext(), message, Toast.LENGTH_SHORT, true).show();

            }
        });
    }
    public void showToast_error(final String message) {
        ThreadTools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(DemoApplication.this, message, Toast.LENGTH_SHORT).show();
                Toasty.error(getBaseContext(), message, Toast.LENGTH_SHORT, true).show();

            }
        });
    }
    public void showToast_info(final String message) {
        ThreadTools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(DemoApplication.this, message, Toast.LENGTH_SHORT).show();

                Toasty.info(getBaseContext(), message, Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        logStr = null;
    }


    public void log(final String tag, final String message){
        if (textView == null){
           // textView = findViewById(R.id.textview_console);
        }
        if (textView == null) {
            return;
        }
        try {
            textView.post(new Runnable() {
                @Override
                public void run() {
                    if (textView != null){
                        logStr += message + "\n";
                        textView.setText(logStr);
                    } else {
                        Log.d(TAG, "textview=null");
                    }
                    Log.d(TAG, message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getTime(){
        return fm.format(new Date());
    }

    protected void showLoading(){

    }

    protected void hideLoading(){

    }
}
