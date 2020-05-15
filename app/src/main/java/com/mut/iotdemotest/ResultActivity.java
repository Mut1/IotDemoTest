package com.mut.iotdemotest;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ResultActivity extends BaseActivity {

    private TabLayout mTablayoutResult;
    private TextView tv_result;
    private ViewPager viewpager_result;
    private List<Fragment> mFragmentList;
    private List<String> titles;
    private MyfragmentAdapter madpter;
    //MQTT监听参数
    public String message[] = new String[100];
    private String messageContent;
    private int i;
    public static int message_total = 0;
    public String chexing;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addData();
        initView();
        listener();
        setViewPager();
    }
    private void addData() {
        mFragmentList = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("1001");
        titles.add("tab2");
        titles.add("tab3");
        titles.add("tab4");
        setContentView(R.layout.activity_result);
        mFragmentList.add(new ResultFragment());
        mFragmentList.add(new ResultFragment());
        mFragmentList.add(new ResultFragment());
        mFragmentList.add(new ResultFragment());
    }

    private void setViewPager() {
        mTablayoutResult.setupWithViewPager(viewpager_result);
        madpter = new MyfragmentAdapter(getSupportFragmentManager(),mFragmentList);
        mTablayoutResult.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewpager_result.setAdapter(madpter);
    }

    private IConnectNotifyListener notifyListener = new IConnectNotifyListener() {
        @Override
        //s是LINK_PRESISTENT    s1是topic名称
        public void onNotify(String s, String s1, AMessage aMessage) {
            titles.add("new");
            mFragmentList.add(new ResultFragment());
            madpter.notifyDataSetChanged();
            showToast("收到下行消息 topic=" + s1);
            message_total+=1;
            //数据流转 （设备端的数据发送后到阿里云平台并流转至APP端）
            messageContent = new String((byte[]) aMessage.data);
            Gson gson= new Gson();
            databean databean1 =  gson.fromJson(messageContent,databean.class);
            if (databean1!=null){
                EventBus.getDefault().post(new MessageEvent(messageContent,chexing));
            }
        }
        @Override
        public boolean shouldHandle(String s, String s1) {
            return true;
        }
        @Override
        public void onConnectStateChange(String s, ConnectState connectState) {
        }
    };

    private void addtab() {
        mTablayoutResult.addTab(mTablayoutResult.newTab().setText("cs1").setTag("tag1"), true);
        mTablayoutResult.addTab(mTablayoutResult.newTab().setText("cs2"));
        mTablayoutResult.addTab(mTablayoutResult.newTab().setText("cs3"));
        mTablayoutResult.addTab(mTablayoutResult.newTab().setText("cs4"));
        mTablayoutResult.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void listener() {
        mTablayoutResult.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               chexing=""+tab.getText();
                tv_result.setText(tab.getText() + "" + tab.getPosition() + tab.getTag());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
public class MyfragmentAdapter extends FragmentPagerAdapter
{

    public MyfragmentAdapter(@NonNull FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        mFragmentList=fragmentList;
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
    private void initView() {
        mTablayoutResult = (TabLayout) findViewById(R.id.tablayout_result);
        tv_result = (TextView) findViewById(R.id.tv_result);
        viewpager_result = (ViewPager) findViewById(R.id.viewpager_result);
    }

}