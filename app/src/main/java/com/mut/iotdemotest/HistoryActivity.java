package com.mut.iotdemotest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.mut.iotdemotest.entity.data2;
import com.mut.iotdemotest.fragment.HistoryFragment;
import com.mut.iotdemotest.utils.TimeUtilsCS;
import com.orient.me.widget.placeholder.StatusView;

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


public class HistoryActivity extends BaseActivity {
    private List<data2> mdatalist;
    private TabLayout tablayout_history;
    private ViewPager viewpager_history;
    private List<Fragment> mFragmentList;
    private List<String> titles;
    private MyfragmentAdapter madpter;
    private StatusView mStatusView;
    private Integer tabsize=50;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
        addData();
       listener();
        IDataStorage dataStorage = DataStorageFactory.getInstance(getBaseContext(), DataStorageFactory.TYPE_DATABASE);
        mdatalist = dataStorage.loadAll(data2.class);
        if (mdatalist.size() == 0) {
            //mStatusView.bind();
            mStatusView.setVisibility(View.VISIBLE);
               mStatusView.triggerError(R.string.history_error);
        } else {
            int size = mdatalist.size();
            if (size>tabsize)
            {
            for (int i = size - 1; i >= size-tabsize ; i--) {
                String time = TimeUtilsCS.date2time(dataStorage.load(data2.class,String.valueOf(i)).getTime()) ;
                titles.add(addDatehour(time,8));
              //  titles.add(addDatehour(mdatalist.get(i).getTime(), 8));
                Log.e("title","title+"+i+"   "+addDatehour(dataStorage.load(data2.class,String.valueOf(i)).getTime(),8));
                HistoryFragment fm = new HistoryFragment();
                fm.setid(dataStorage.load(data2.class,String.valueOf(i)).getTime());
                mFragmentList.add(fm);
            }
            }
        else
            {
                for (int i = size - 1; i >= 0 ; i--) {
                    String time = TimeUtilsCS.date2time(dataStorage.load(data2.class,String.valueOf(i)).getTime()) ;
                    titles.add(addDatehour(time,8));
                    Log.e("title","title+"+i+"   "+addDatehour(mdatalist.get(i).getTime(), 8));
                    HistoryFragment fm = new HistoryFragment();
                    fm.setid(mdatalist.get(i).getTime());
                    mFragmentList.add(fm);
                }
            }
        }
        setViewPager();
        madpter.notifyDataSetChanged();
    }
    private void addData() {
        mFragmentList = new ArrayList<>();
        titles = new ArrayList<>();
            mdatalist=new ArrayList<>();

    }
    private void listener() {
        tablayout_history.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //  tv_result.setText(tab.getText() + "" + tab.getPosition() + tab.getTag());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    private void setViewPager() {
        tablayout_history.setupWithViewPager(viewpager_history);
         madpter = new MyfragmentAdapter(getSupportFragmentManager(),mFragmentList);

        // madpter.notifyDataSetChanged();
        // TODO: 适配器监听变化 位置进行调整，未测试。原位置line 96
        tablayout_history.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewpager_history.setAdapter(madpter);
    }
    public class MyfragmentAdapter extends FragmentPagerAdapter
    {
        public MyfragmentAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
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
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initView() {
        tablayout_history = (TabLayout) findViewById(R.id.tablayout_history);
        viewpager_history = (ViewPager) findViewById(R.id.viewpager_history);
        mStatusView=findViewById(R.id.et_content);
    }
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
