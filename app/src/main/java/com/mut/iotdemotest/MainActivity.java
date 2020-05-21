package com.mut.iotdemotest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LanguageUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final String TAG1 = "111";
    private Button btn_init;
    private Button btn_deinit;
    private Button btn_start;
    private List<databean> databeanList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_init=(Button)findViewById(R.id.init);
        btn_deinit=(Button)findViewById(R.id.deinit);
        btn_start=(Button)findViewById(R.id.start);
        btn_init.setOnClickListener(this);
        btn_deinit.setOnClickListener(this);
        btn_start.setOnClickListener(this);
        ALog.d(TAG, "onCreate");

      //  connect();
    }



    private boolean checkReady() {
        if (DemoApplication.userDevInfoError) {
            showToast("设备三元组信息res/raw/deviceinfo格式错误");
            return false;
        }
        if (!DemoApplication.isInitDone) {
            showToast("初始化尚未成功，请稍后点击");
            return false;
        }
        return true;
    }

    public void start() {
        if (!checkReady()) {
            return;
        }
        //Intent intent = new Intent(this, ReceiveActivity.class);
        Intent intent = new Intent(this, ResultActivity.class);
          startActivity(intent);
    }

    /**
     * 初始化
     */
    private void connect() {
        Log.d(TAG, "connect() called");

        // SDK初始化
//        DemoApplication.productKey = "a1VOjEIP4g7";
//        DemoApplication.deviceName = "xtqMobile";
//        DemoApplication.deviceSecret = "y9f1KLJ0yHDIAzj1RSnirMInw0RPGTPt";
        InitManager.init(this, DemoApplication.productKey, DemoApplication.deviceName,
                DemoApplication.deviceSecret, DemoApplication.productSecret, new IDemoCallback() {
                    @Override
                    public void onError(AError aError) {
                        Log.d(TAG1, "onError() called with: aError = [" + aError + "]");
                        // 初始化失败，初始化失败之后需要用户负责重新初始化
                        // 如一开始网络不通导致初始化失败，后续网络回复之后需要重新初始化
                        showToast("初始化失败");
                    }

                    @Override
                    public void onInitDone(Object data) {
                        Log.d(TAG, "onInitDone() called with: data = [" + data + "]");
                        DemoApplication.isInitDone = true;
                        showToast("初始化成功");
                    }
                });
    }

    private void deinit() {
        ALog.d(TAG1, "deinit");
        DemoApplication.isInitDone = false;
        LinkKit.getInstance().deinit();
        showToast("反初始化成功");
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.init:
                connect();
                break;
            case R.id.deinit:

                deinit();
                connect();
                //LinkKit.getInstance().unRegisterOnPushListener(notifyListener);
                break;
            case R.id.start:
                start();
                break;
        }

    }
}
