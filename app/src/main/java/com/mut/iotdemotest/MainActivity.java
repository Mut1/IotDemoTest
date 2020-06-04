package com.mut.iotdemotest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.aliyun.alink.dm.api.BaseInfo;
import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.mut.iotdemotest.activity.POIExcelActivity;
import com.mut.iotdemotest.entity.data2;
import com.mut.iotdemotest.utils.TimeUtilsCS;
import com.mut.iotdemotest.utils.ToastUtilsCs;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import xiaofei.library.datastorage.DataStorageFactory;
import xiaofei.library.datastorage.IDataStorage;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final String TAG1 = "111";

    private QMUIRoundButton btn_nowdata;
    private QMUIRoundButton btn_historydata;
    private QMUIRoundButton btn_refresh;
    private QMUIRoundButton btn_excel;

    private List<databean> databeanList;
    private Integer id_type;

    public static boolean isInitDone = false;
    public static boolean isIniting = false;
    public static boolean userDevInfoError = false;
    public static DeviceInfoData mDeviceInfoData = null;
    public static String productKey = null, deviceName = null, deviceSecret = null, productSecret = null, password = null, username = null, clientId = null;
    public static Context mAppContext = null;
    private List<data2> mdatalist;
    private QMUIRoundButton btn_add1;
    private QMUIRoundButton btn_add2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mdatalist = new ArrayList<>();
        btn_nowdata = (QMUIRoundButton) findViewById(R.id.btn_nowdata);
        btn_historydata = (QMUIRoundButton) findViewById(R.id.btn_historydata);
        btn_refresh = (QMUIRoundButton) findViewById(R.id.btn_refresh);
        btn_excel = findViewById(R.id.btn_excel);
        btn_excel.setOnClickListener(this);
        btn_nowdata.setOnClickListener(this);
        btn_historydata.setOnClickListener(this);
        btn_refresh.setOnClickListener(this);
        ALog.d(TAG, "onCreate");
        id_type = getIntent().getIntExtra("id", 0);
        //  connect();
        Log.e("1111", id_type + "");
        String testData = getFromRaw(checkID(id_type));
        // 解析数据
        getDeviceInfoFrom(testData);
        deinit();
        connect();

    }


    private boolean checkReady() {
        if (userDevInfoError) {
            showToast_error("设备三元组信息res/raw/deviceinfo格式错误");
            return false;
        }
        if (!isInitDone) {
            showToast_error("初始化尚未成功，请稍后点击");
            return false;
        }
        return true;
    }

    public void start_NowData() {
        if (!checkReady()) {
            return;
        }
        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }

    /**
     * 初始化
     */
    private void connect() {
        if (isInitDone) {
            showToast_info("初始化已经完成");

        } else {
            Log.d(TAG, "connect() called");
            isIniting = true;
            InitManager.init(this, productKey, deviceName,
                    deviceSecret, productSecret, new IDemoCallback() {
                        @Override
                        public void onError(AError aError) {
                            Log.d(TAG1, "onError() called with: aError = [" + aError + "]");
                            // 初始化失败，初始化失败之后需要用户负责重新初始化
                            // 如一开始网络不通导致初始化失败，后续网络回复之后需要重新初始化
                            showToast_error("初始化失败");
                            isIniting = false;
                        }

                        @Override
                        public void onInitDone(Object data) {
                            Log.d(TAG, "onInitDone() called with: data = [" + data + "]");
                            isInitDone = true;
                            showToast_success("初始化成功");
                            isIniting = false;
                        }
                    });
        }
    }

    private void deinit() {

        ALog.d(TAG1, "deinit");
        LinkKit.getInstance().deinit();
        isInitDone = false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refresh:
                if (isIniting) {
                    return;
                } else if (isIniting == false) {
//                    IDataStorage dataStorage = DataStorageFactory.getInstance(getBaseContext(), DataStorageFactory.TYPE_DATABASE);
//                    dataStorage.deleteAll(data2.class);
                    deinit();
                    connect();
                }

                break;
            case R.id.btn_nowdata:
                IDataStorage dataStorage = DataStorageFactory.getInstance(getBaseContext(), DataStorageFactory.TYPE_DATABASE);
                mdatalist = dataStorage.loadAll(data2.class);
                if (mdatalist.size() >= 60000) {
                    pop();
                } else
                    start_NowData();
                break;
            case R.id.btn_historydata:
                Intent intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_excel:
                //  Intent intent1 = new Intent(this, ExcelActivity.class);
                Intent intent1 = new Intent(this, POIExcelActivity.class);
                startActivity(intent1);
                break;
//            case R.id.btn_add1:
//                adddata(1);
//                break;
//            case R.id.btn_add2:
//                adddata10000(70000);
//                break;
        }

    }


    public String getFromRaw(Integer i) {
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            inputReader = new InputStreamReader(getResources().openRawResource(i));
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
                if (inputReader != null) {
                    inputReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
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

    public Integer checkID(Integer id_type) {
        int i = id_type;
        if (i == 1)
            return R.raw.deviceinfo;
        if (i == 2)
            return R.raw.deviceinfo2;
        if (i == 3)
            return R.raw.deviceinfo3;
        if (i == 4)
            return R.raw.deviceinfo4;
        if (i == 5)
            return R.raw.deviceinfo5;
        return R.raw.deviceinfo5;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // deinit();
    }

    private void pop() {
        new XPopup.Builder(MainActivity.this)
//                         .dismissOnTouchOutside(false)
//                         .autoDismiss(false)
//                        .popupAnimation(PopupAnimation.NoAnimation)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onCreated() {
                        Log.e("tag", "弹窗创建了");
                    }

                    @Override
                    public void onShow() {
                        Log.e("tag", "onShow");
                    }

                    @Override
                    public void onDismiss() {
                        Log.e("tag", "onDismiss");
                    }

                    //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
                    @Override
                    public boolean onBackPressed() {
                        // ToastUtils.showShort("我拦截的返回按键，按返回键XPopup不会关闭了");
                        return true;
                    }
                }).asConfirm("数据过多！", "已保存数据超过60000条，请及时导出数据！以防数据丢失",
                "稍后再说", "去导出",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        //  toast("click confirm");
                        Intent i = new Intent(MainActivity.this, POIExcelActivity.class);
                        startActivity(i);
                    }
                }, new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        start_NowData();
                    }
                }, false)
                .show();
    }

    private void adddata(int size) {
        String messageContent = "{\"GPSerr\":\"0\",\"CANerr\":\"0\",\"Chesu\":\"72\",\"HanZL\":\"4\",\"E\":\"1920.658\",\"Getai\":\"86\",\"Mark\":\"A100101\",\"Time\":\"12:32:5\",\"N\":\"230.125\",\"Fukuan\":\"338\",\"QieLTL\":\"17\",\"LiZSP\":\"690\",\"ZongZTL\":\"680\",\"Shusongzhou\":\"192\",\"PoSL\":\"0\",\"QuDL\":\"14\",\"Zuoye\":\"0\",\"ZaYSP\":\"14\",\"QinXSS\":\"9\",\"ZhengDS\":\"332\",\"Bohelun\":\"27\",\"GeCGD\":\"44\",\"JiaDSS\":\"583\",\"FongJZS\":\"1495\",\"YuLSD\":\"9\",\"LiZLL\":\"3\"}";
        Gson gson = new Gson();
        data2 data = gson.fromJson(messageContent, data2.class);
        IDataStorage dataStorage1 = DataStorageFactory.getInstance(getBaseContext(), DataStorageFactory.TYPE_DATABASE);
        data.setTime(TimeUtilsCS.timeplusdate(data.getTime()));

        for (int i = 0; i < size; i++) {
            //999912
            int j = dataStorage1.loadAll(data2.class).size();
            dataStorage1.storeOrUpdate(data, String.valueOf(j));
        }
        ToastUtilsCs.showToast_info(MainActivity.this,dataStorage1.loadAll(data2.class).size()+"");

    }

    private void adddata10000(int size) {
        String messageContent = "{\"GPSerr\":\"0\",\"CANerr\":\"0\",\"Chesu\":\"72\",\"HanZL\":\"4\",\"E\":\"1920.658\",\"Getai\":\"86\",\"Mark\":\"A100101\",\"Time\":\"12:32:5\",\"N\":\"230.125\",\"Fukuan\":\"338\",\"QieLTL\":\"17\",\"LiZSP\":\"690\",\"ZongZTL\":\"680\",\"Shusongzhou\":\"192\",\"PoSL\":\"0\",\"QuDL\":\"14\",\"Zuoye\":\"0\",\"ZaYSP\":\"14\",\"QinXSS\":\"9\",\"ZhengDS\":\"332\",\"Bohelun\":\"27\",\"GeCGD\":\"44\",\"JiaDSS\":\"583\",\"FongJZS\":\"1495\",\"YuLSD\":\"9\",\"LiZLL\":\"3\"}";
        Gson gson = new Gson();
        data2 data = gson.fromJson(messageContent, data2.class);
        IDataStorage dataStorage1 = DataStorageFactory.getInstance(getBaseContext(), DataStorageFactory.TYPE_DATABASE);
        data.setTime(TimeUtilsCS.timeplusdate(data.getTime()));
        for (int i = 0; i < size; i++) {
            //999912

            dataStorage1.storeOrUpdate(data, String.valueOf(i));
        }
        ToastUtilsCs.showToast_info(MainActivity.this,dataStorage1.loadAll(data2.class).size()+"");
    }
    private void initView() {
//        btn_add1 = (QMUIRoundButton) findViewById(R.id.btn_add1);
//        btn_add2 = (QMUIRoundButton) findViewById(R.id.btn_add2);
//        btn_add1.setOnClickListener(this);
//        btn_add2.setOnClickListener(this);
    }
}
