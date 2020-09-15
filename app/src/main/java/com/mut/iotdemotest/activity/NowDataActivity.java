package com.mut.iotdemotest.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import com.google.gson.JsonObject;
import com.mut.iotdemotest.BaseActivity;
import com.mut.iotdemotest.MessageEvent;
import com.mut.iotdemotest.R;
import com.mut.iotdemotest.entity.ShuidaoBean;
import com.mut.iotdemotest.fragment.NowDataFragment;
import com.mut.iotdemotest.utils.TimeUtilsCS;
import com.orient.me.widget.placeholder.StatusView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import xiaofei.library.datastorage.DataStorageFactory;
import xiaofei.library.datastorage.IDataStorage;

public class NowDataActivity extends BaseActivity {
    private StatusView mStatusView;
    private TabLayout mTablayoutNowdata;
    private ViewPager mViewpagerNowdata;
    private boolean isLoading = true;
    private List<Fragment> mFragmentList;
    private List<String> titles;
    //MQTT监听参数
    public String message[] = new String[100];
    private String messageContent;
    private MyfragmentAdapter madpter;
    public String chexing;
    String GpsMsg = "";
    String CanMsg = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_nowdata);
        initView();
        addData();
        listener();
        setViewPager();
        super.onCreate(savedInstanceState);
    }

    private void addData() {
        mFragmentList = new ArrayList<>();
        titles = new ArrayList<>();
    }

    private void initView() {

        mTablayoutNowdata = (TabLayout) findViewById(R.id.tablayout_nowdata);
        mViewpagerNowdata = (ViewPager) findViewById(R.id.viewpager_nowdata);
        mStatusView = (StatusView) findViewById(R.id.et_content);
        mStatusView.triggerLoading();
        mCountDownTimer_loading.start();

    }

    private IConnectNotifyListener notifyListener = new IConnectNotifyListener() {
        @Override
        //s是LINK_PRESISTENT    s1是topic名称
        public void onNotify(String s, String s1, AMessage aMessage) {
            //数据流转 （设备端的数据发送后到阿里云平台并流转至APP端）
            messageContent = new String((byte[]) aMessage.data);
            Log.d(TAG, "收到下行消息 topic=" + s1);
            Log.d(TAG, "收到Json数据" + messageContent);


            try {
                JSONObject J = new JSONObject(messageContent);
                if (J.has("GPSerr")) {
                    Log.d(TAG, "收到一条Gps数据");
                    GpsMsg = J.toString();
                } else if (J.has("Canerr")) {
                    Log.d(TAG, "收到一条Can数据");
                    CanMsg = J.toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (!GpsMsg.equals("") && !CanMsg.equals("")) {
                String data2 = null;
                try {
                    data2 = combineJson(GpsMsg, CanMsg);
                    Log.d(TAG, "合并后的Json数据" + data2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                ShuidaoBean mdata = gson.fromJson(data2, ShuidaoBean.class);
                Log.d(TAG, "合并后的bean" + mdata);


                IDataStorage dataStorage = DataStorageFactory.getInstance(getApplicationContext(), DataStorageFactory.TYPE_DATABASE);
                int i = dataStorage.loadAll(ShuidaoBean.class).size();
                Log.d("title", "数据库长度:" + i);
                // Log.e("title", "时间:" + addDatehour(mdata.getTime(), 8));
                if (TimeUtilsCS.isValidTime(mdata.getTime())) {
                    mdata.setTime(TimeUtilsCS.timeplusdate(mdata.getTime()));
                    dataStorage.storeOrUpdate(mdata, String.valueOf(i));
                }
                if (!TimeUtilsCS.isValidTime(mdata.getTime())) {
                    mdata.setTime("1900-01-01 16:00:00");
                    dataStorage.storeOrUpdate(mdata, String.valueOf(i));
                }
                if (mdata != null) {
                    if (titles.contains(mdata.getMark())) {
                    } else {
                        if (isLoading) {
                            mStatusView.setVisibility(View.GONE);
                            isLoading = false;
                        }
                        titles.add(mdata.getMark());
                        NowDataFragment fm = new NowDataFragment();
                        // 创建fragment时，传入车型
                        fm.setMark(mdata.getMark());
                        mFragmentList.add(fm);
                    }
                }
                EventBus.getDefault().post(new MessageEvent(data2, chexing));
                madpter.notifyDataSetChanged();

                GpsMsg = "";
                CanMsg = "";
                Log.d(TAG, "清空缓存Msg");

            }


//            Gson gson = new Gson();
//            ShuidaoBean mdata = gson.fromJson(messageContent, ShuidaoBean.class);
//            Log.d(TAG, "一条完整的数据" + mdata);
//
//
//
//
//            IDataStorage dataStorage = DataStorageFactory.getInstance(getApplicationContext(), DataStorageFactory.TYPE_DATABASE);
//                int i = dataStorage.loadAll(ShuidaoBean.class).size();
//                Log.e("title", "数据库长度:" + i);
//                // Log.e("title", "时间:" + addDatehour(mdata.getTime(), 8));
//            if (TimeUtilsCS.isValidTime(mdata.getTime())) {
//                mdata.setTime(TimeUtilsCS.timeplusdate(mdata.getTime()));
//                dataStorage.storeOrUpdate(mdata, String.valueOf(i));
//            }
//            if (!TimeUtilsCS.isValidTime(mdata.getTime())) {
//                mdata.setTime("1900-01-01 16:00:00");
//                dataStorage.storeOrUpdate(mdata, String.valueOf(i));
//            }
//
//
//            if (mdata != null) {
//                if (titles.contains(mdata.getMark())) {
//
//                } else {
//                    if (isLoading) {
//                        mStatusView.setVisibility(View.GONE);
//                        isLoading = false;
//                    }
//                    titles.add(mdata.getMark());
//                    NowDataFragment fm = new NowDataFragment();
//                   // 创建fragment时，传入车型
//                    fm.setMark(mdata.getMark());
//                    mFragmentList.add(fm);
//                }
//            }
//            EventBus.getDefault().post(new MessageEvent(messageContent, chexing));
//            madpter.notifyDataSetChanged();
//            ALog.d(TAG, "接受一次数据");

        }

        @Override
        public boolean shouldHandle(String s, String s1) {
            return true;
        }

        @Override
        public void onConnectStateChange(String s, ConnectState connectState) {
            Log.e(TAG, "网络连接变化");
        }
    };

    private void setViewPager() {
        mTablayoutNowdata.setupWithViewPager(mViewpagerNowdata);
        madpter = new MyfragmentAdapter(getSupportFragmentManager(), mFragmentList);

        // madpter.notifyDataSetChanged();
        // TODO: 适配器监听变化 位置进行调整，未测试。原位置line 96
        mTablayoutNowdata.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewpagerNowdata.setAdapter(madpter);
    }


    public class MyfragmentAdapter extends FragmentPagerAdapter {
        public MyfragmentAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            mFragmentList = fragmentList;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinkKit.getInstance().registerOnPushListener(notifyListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LinkKit.getInstance().unRegisterOnPushListener(notifyListener);
    }


    private void listener() {
        mTablayoutNowdata.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private CountDownTimer mCountDownTimer_loading = new CountDownTimer(60000, 5000) {

        @Override
        public void onTick(long millisUntilFinished) {
            Log.d(TAG, "剩余" + (millisUntilFinished / 1000) + "秒");
        }

        @Override
        public void onFinish() {
            Log.d(TAG, "倒计时结束");
            if (isLoading) {
                isLoading = false;
                mStatusView.triggerEmpty();
            }
            mCountDownTimer_loading.cancel();
        }
    };


    private String combineJson(String srcJObjStr, String addJObjStr) throws JSONException {
        if (addJObjStr == null || addJObjStr.isEmpty()) {
            return srcJObjStr;
        }
        if (srcJObjStr == null || srcJObjStr.isEmpty()) {
            return addJObjStr;
        }

        JSONObject srcJObj = new JSONObject(srcJObjStr);
        JSONObject addJObj = new JSONObject(addJObjStr);

        combineJson(srcJObj, addJObj);

        return srcJObj.toString();
    }

    private JSONObject combineJson(JSONObject srcObj, JSONObject addObj) throws JSONException {

        Iterator<String> itKeys1 = addObj.keys();
        String key, value;
        while (itKeys1.hasNext()) {
            key = itKeys1.next();
            value = addObj.optString(key);

            srcObj.put(key, value);
        }
        return srcObj;
    }

}
