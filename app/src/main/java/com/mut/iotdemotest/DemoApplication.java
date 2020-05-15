package com.mut.iotdemotest;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.linksdk.tools.ThreadTools;

public class DemoApplication extends Application {
    private static final String TAG = "DemoApplication";
    /**
     * 判断是否初始化完成
     * 未初始化完成，所有和云端的长链通信都不通
     */
    public static boolean isInitDone = false;
    public static boolean userDevInfoError = false;
    public static DeviceInfoData mDeviceInfoData = null;
    public static String productKey = null, deviceName = null, deviceSecret = null, productSecret = null, password = null, username = null,clientId = null;
    public static Context mAppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();

        ALog.setLevel(ALog.LEVEL_DEBUG);

    }






    private void showToast(final String message) {
        ThreadTools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DemoApplication.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static Context getAppContext() {
        return mAppContext;
    }

}
