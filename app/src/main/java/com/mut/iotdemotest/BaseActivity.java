package com.mut.iotdemotest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.alink.linksdk.tools.ALog;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    public void showToast(final String message){
        ALog.d(TAG, "showToast() called with: message = [" + message + "]");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
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
