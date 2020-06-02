package com.mut.iotdemotest;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.tools.ALog;
import com.blankj.utilcode.util.ArrayUtils;
import com.blankj.utilcode.util.CollectionUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.mut.iotdemotest.entity.data2;
import com.mut.iotdemotest.utils.TimeUtilsCS;
import com.orient.me.widget.placeholder.StatusView;
import com.qmuiteam.qmui.widget.QMUILoadingView;

import org.greenrobot.eventbus.EventBus;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import xiaofei.library.datastorage.DataStorageFactory;
import xiaofei.library.datastorage.IDataStorage;

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
    public static int message_total = 0;
    public String chexing;
    private QMUILoadingView mLoadingView;
private boolean isLoading=true;

private StatusView mStatusView;
private List<data2>  mData2List;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        addData();
        initView();     //获取控件
        listener();     //设置tablayout监听
        setViewPager(); //viewpager连接tablayout，viewpager设置适配器

    }


    private void addData() {
        mFragmentList = new ArrayList<>();
        titles = new ArrayList<>();
        mData2List=new ArrayList<>();
    }
    private void setViewPager() {
        mTablayoutResult.setupWithViewPager(viewpager_result);
        madpter = new MyfragmentAdapter(getSupportFragmentManager(),mFragmentList);

       // madpter.notifyDataSetChanged();
        // TODO: 适配器监听变化 位置进行调整，未测试。原位置line 96 
        mTablayoutResult.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewpager_result.setAdapter(madpter);
    }
    private IConnectNotifyListener notifyListener = new IConnectNotifyListener() {
        @Override
        //s是LINK_PRESISTENT    s1是topic名称
        public void onNotify(String s, String s1, AMessage aMessage) {
            //数据流转 （设备端的数据发送后到阿里云平台并流转至APP端）
            messageContent = new String((byte[]) aMessage.data);
            Log.d(TAG,"收到下行消息 topic=" + s1);
            Gson gson= new Gson();
            data2   mdata= gson.fromJson(messageContent,data2.class);

            Log.d(TAG,"收到信息包" + mdata);

            IDataStorage dataStorage = DataStorageFactory.getInstance(getApplicationContext(), DataStorageFactory.TYPE_DATABASE);
            int i = dataStorage.loadAll(data2.class).size();
            Log.e("title","数据库长度:"+i);
            Log.e("title","时间:"+addDatehour(mdata.getTime(),8));
            mdata.setTime(TimeUtilsCS.timeplusdate(mdata.getTime()));
            dataStorage.storeOrUpdate(mdata,String.valueOf(i));
            if (mdata!=null){
               if( titles.contains(mdata.getMark()))
               {

               }
               else{
                   if(isLoading) {
                       mStatusView.setVisibility(View.GONE);
                       isLoading=false;
                   }
                titles.add(mdata.getMark());
                   ResultFragment fm=new ResultFragment();
                   //创建fragment时，传入车型
                   fm.setMchexing(mdata.getMark());
                mFragmentList.add(fm);
               }
               }
            EventBus.getDefault().post(new MessageEvent(messageContent,chexing));
            madpter.notifyDataSetChanged();
            ALog.e(TAG,"接受一次数据");

        }
        @Override
        public boolean shouldHandle(String s, String s1) {
            return true;
        }
        @Override
        public void onConnectStateChange(String s, ConnectState connectState) {
            Log.e(TAG,"网络连接变化");
        }
    };



    private void listener() {
        mTablayoutResult.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    //获取控件
    private void initView() {
        mTablayoutResult = (TabLayout) findViewById(R.id.tablayout_result);
     //   tv_result = (TextView) findViewById(R.id.tv_result);
        viewpager_result = (ViewPager) findViewById(R.id.viewpager_result);
        mLoadingView=(QMUILoadingView) findViewById(R.id.loading_view);
        mLoadingView.setVisibility(View.GONE);
       // mLoadingView.start();
        mCountDownTimer_loading.start();

        mStatusView=findViewById(R.id.et_content);
        mStatusView.triggerLoading();
    }
    private CountDownTimer mCountDownTimer_loading = new CountDownTimer(60000, 5000) {

        @Override
        public void onTick(long millisUntilFinished) {
           Log.d(TAG, "剩余" + (millisUntilFinished / 1000) + "秒");
        }

        @Override
        public void onFinish() {
            Log.d(TAG, "倒计时结束");
            if(isLoading) {
                isLoading = false;
              mStatusView.triggerEmpty();
            }
            mCountDownTimer_loading.cancel();
        }
    };

    //当前时间串+n个小时
    public static String addDatehour(String time, int hour) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        cal = null;
        return format.format(date);

    }


}