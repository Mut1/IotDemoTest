package com.mut.iotdemotest;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.alink.dm.api.BaseInfo;
import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.model.ResponseModel;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.linksdk.tools.ThreadTools;
import com.google.gson.Gson;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class DemoApplication extends Application {
    private static final String TAG = "DemoApplication";
    private static final String TAG1 = "111";
    /**
     * 判断是否初始化完成
     * 未初始化完成，所有和云端的长链通信都不通
     */
    public static boolean isInitDone = false;
    public static boolean isIniting = false;
    public static boolean userDevInfoError = false;
    public static DeviceInfoData mDeviceInfoData = null;
    public static String productKey = null, deviceName = null, deviceSecret = null, productSecret = null, password = null, username = null,clientId = null;
    public static Context mAppContext = null;


    @Override
    public void onCreate() {
        super.onCreate();

        ALog.setLevel(ALog.LEVEL_DEBUG);
        // 从 raw 读取  三元组信息
        String testData = getFromRaw();
        // 解析数据
        getDeviceInfoFrom(testData);

        if (TextUtils.isEmpty(deviceSecret)) {
           tryGetFromSP();
        }
        if (TextUtils.isEmpty(deviceSecret) && !TextUtils.isEmpty(productSecret)) {
            InitManager.registerDevice(this, productKey, deviceName, productSecret, new IConnectSendListener() {
                @Override
                public void onResponse(ARequest aRequest, AResponse aResponse) {
                    Log.d(TAG, "onResponse() called with: aRequest = [" + aRequest + "], aResponse = [" + (aResponse == null ? "null" : aResponse.data) + "]");
                    if (aResponse != null && aResponse.data != null) {
                        // 解析云端返回的数据
                        ResponseModel<Map<String, String>> response = JSONObject.parseObject(aResponse.data.toString(),
                                new TypeReference<ResponseModel<Map<String, String>>>() {
                                }.getType());
                        if ("200".equals(response.code) && response.data != null && response.data.containsKey("deviceSecret") &&
                                !TextUtils.isEmpty(response.data.get("deviceSecret"))) {
                            /**
                             * 建议将ds保存在非应用目录，确保卸载之后ds仍然可以读取到。
                             */
                            deviceSecret = response.data.get("deviceSecret");
                            // getDeviceSecret success, to build connection.
                            // 持久化 deviceSecret 初始化建联的时候需要
                            // 用户需要按照实际场景持久化设备的三元组信息，用于后续的连接
                            SharedPreferences preferences = getSharedPreferences("deviceAuthInfo", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("deviceId", productKey+deviceName);
                            editor.putString("deviceSecret", deviceSecret);
                            //提交当前数据
                            editor.commit();
                            connect();
                        }
                    }
                }

                @Override
                public void onFailure(ARequest aRequest, AError aError) {
                    Log.d(TAG, "onFailure() called with: aRequest = [" + aRequest + "], aError = [" + aError + "]");
                }
            });
        }
        else if (!TextUtils.isEmpty(deviceSecret) || !TextUtils.isEmpty(password)){
             //三元组信息正确的话，进入APP会调用一次连接 init
              connect();
            // Log.e(TAG, "这里执行了");
        } else {
            Log.e(TAG, "res/raw/deviceinfo invalid.");
        }
    }






    private void showToast_success(final String message) {
        ThreadTools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // Toast.makeText(DemoApplication.this, message, Toast.LENGTH_SHORT).show();
                Toasty.success(DemoApplication.this, message, Toast.LENGTH_SHORT, true).show();

            }
        });
    }
    private void showToast_error(final String message) {
        ThreadTools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Toast.makeText(DemoApplication.this, message, Toast.LENGTH_SHORT).show();
                Toasty.error(DemoApplication.this, message, Toast.LENGTH_SHORT, true).show();
            }
        });
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public String getFromRaw() {
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(getResources().openRawResource(R.raw.deviceinfo));
            bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                }
                if (inputReader != null){
                    inputReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    private void getDeviceInfoFrom(String testData) {
        Log.e(TAG, "getDeviceInfoFrom() called with: testData = [" + testData + "]");
        try {
            Gson mGson = new Gson();
            DeviceInfoData deviceInfoData = mGson.fromJson(testData, DeviceInfoData.class);
            if (deviceInfoData == null) {
                Log.e(TAG, "getDeviceInfoFrom: file format error.");
                userDevInfoError = true;
                return;
            }
            Log.e(TAG, "getDeviceInfoFrom deviceInfoData=" + deviceInfoData);
            if (checkValid(deviceInfoData)) {
                mDeviceInfoData = new DeviceInfoData();
                mDeviceInfoData.productKey = deviceInfoData.productKey;
                mDeviceInfoData.productSecret = deviceInfoData.productSecret;
                mDeviceInfoData.deviceName = deviceInfoData.deviceName;
                mDeviceInfoData.deviceSecret = deviceInfoData.deviceSecret;
                mDeviceInfoData.username = deviceInfoData.username;
                mDeviceInfoData.password = deviceInfoData.password;
                mDeviceInfoData.clientId = deviceInfoData.clientId;
                mDeviceInfoData.subDevice = new ArrayList<>();
                if (deviceInfoData.subDevice == null) {
                    Log.d(TAG, "getDeviceInfoFrom: subDevice empty..");
                    return;
                }
                for (int i = 0; i < deviceInfoData.subDevice.size(); i++) {
                    if (checkValid(deviceInfoData.subDevice.get(i))) {
                        mDeviceInfoData.subDevice.add(deviceInfoData.subDevice.get(i));
                    } else {
                        Log.d(TAG, "getDeviceInfoFrom: subDevice info invalid. discard.");
                    }
                }

                productKey = mDeviceInfoData.productKey;
                deviceName = mDeviceInfoData.deviceName;
                deviceSecret = mDeviceInfoData.deviceSecret;
                productSecret = mDeviceInfoData.productSecret;
                password = mDeviceInfoData.password;
                username = mDeviceInfoData.username;
                clientId = mDeviceInfoData.clientId;

                Log.d(TAG, "getDeviceInfoFrom: final data=" + mDeviceInfoData);
            } else {
                Log.e(TAG, "res/raw/deviceinfo error.");
                userDevInfoError = true;
            }

        } catch (Exception e) {
            Log.e(TAG, "getDeviceInfoFrom: e", e);
            userDevInfoError = true;
        }

    }
    private boolean checkValid(BaseInfo baseInfo) {
        if (baseInfo == null) {
            return false;
        }
        if (TextUtils.isEmpty(baseInfo.productKey) || TextUtils.isEmpty(baseInfo.deviceName)) {
            return false;
        }
        if (baseInfo instanceof DeviceInfoData) {
            if (TextUtils.isEmpty(((DeviceInfo) baseInfo).productSecret) && TextUtils.isEmpty(((DeviceInfo) baseInfo).deviceSecret) && TextUtils.isEmpty(((DeviceInfoData) baseInfo).password)) {
                return false;
            }
        }
        return true;
    }
    //动态注册  不调用  具体注解见原demo
    private void tryGetFromSP() {
        Log.d(TAG, "tryGetFromSP() called");
        SharedPreferences authInfo = getSharedPreferences("deviceAuthInfo", Activity.MODE_PRIVATE);
        String pkDn = authInfo.getString("deviceId", null);
        String ds = authInfo.getString("deviceSecret", null);
        if (pkDn != null && pkDn.equals(productKey + deviceName) && ds != null) {
            Log.d(TAG, "tryGetFromSP update ds from sp.");
            deviceSecret = ds;
        }
    }

    private void connect() {
        Log.d(TAG, "connect() called");

        // SDK初始化
        MqttConfigure.mqttUserName=username;
       // MqttConfigure.userName = username;
        MqttConfigure.mqttPassWord = password;
        MqttConfigure.clientId = clientId;
        isIniting=true;
        InitManager.init(this, productKey, deviceName, deviceSecret, productSecret, new IDemoCallback() {

            @Override
            public void onError(AError aError) {
                Log.d(TAG1, "onError() called with: aError = [" + aError + "]");
                // 初始化失败，初始化失败之后需要用户负责重新初始化
                // 如一开始网络不通导致初始化失败，后续网络回复之后需要重新初始化
                isIniting=false;
              //  showToast("初始化失败");
                showToast_error("服务器连接失败");


            }

            @Override
            public void onInitDone(Object data) {
                Log.d(TAG, "onInitDone() called with: data = [" + data + "]");
               // showToast("初始化成功");
                showToast_success("服务器连接成功");
                isIniting=false;
                isInitDone = true;
            }
        });
    }
}
