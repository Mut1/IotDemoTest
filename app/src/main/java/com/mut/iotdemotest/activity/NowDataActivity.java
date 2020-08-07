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

import com.mut.iotdemotest.BaseActivity;
import com.mut.iotdemotest.MessageEvent;
import com.mut.iotdemotest.R;
import com.mut.iotdemotest.entity.ShuidaoBean;
import com.mut.iotdemotest.fragment.NowDataFragment;
import com.orient.me.widget.placeholder.StatusView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
            Gson gson = new Gson();
            ShuidaoBean mdata = gson.fromJson(messageContent, ShuidaoBean.class);
            Log.d(TAG, "收到信息包" + mdata);

//            IDataStorage dataStorage = DataStorageFactory.getInstance(getApplicationContext(), DataStorageFactory.TYPE_DATABASE);
//            int i = dataStorage.loadAll(ShuidaoBean.class).size();
//            Log.e("title", "数据库长度:" + i);
//           // Log.e("title", "时间:" + addDatehour(mdata.getTime(), 8));
//            mdata.setTime(TimeUtilsCS.timeplusdate(mdata.getTime()));
//            dataStorage.storeOrUpdate(mdata, String.valueOf(i));

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
            EventBus.getDefault().post(new MessageEvent(messageContent, chexing));
            madpter.notifyDataSetChanged();
            ALog.d(TAG, "接受一次数据");

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

}
