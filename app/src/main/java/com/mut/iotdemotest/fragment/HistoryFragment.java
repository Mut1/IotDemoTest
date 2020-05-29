package com.mut.iotdemotest.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.mut.iotdemotest.MessageEvent;
import com.mut.iotdemotest.R;
import com.mut.iotdemotest.entity.data2;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import xiaofei.library.datastorage.DataStorageFactory;
import xiaofei.library.datastorage.IDataStorage;
import xiaofei.library.datastorage.util.Condition;

public class HistoryFragment  extends Fragment {
    public QMUIAlphaTextView tv_mark;
    public QMUIAlphaTextView tv_time;
    public TextView tv_weidu;
    public TextView tv_jingdu;
    public TextView tv_bohelun;
    public TextView tv_zuoye;
    public TextView tv_fukuan;
    public TextView tv_getai;
    public TextView tv_shusongzhou;
    public TextView tv_cheshu;
    public TextView tv_QieLTL;
    public TextView tv_LiZSP;
    public TextView tv_ZaYSP;
    public TextView tv_GeCGD;
    public TextView tv_QinXSS;
    public TextView tv_JiaDSS;
    public TextView YuLSD;
    public TextView HanZL;
    public TextView PoSL;
    public TextView LiZLL;

    public TextView tv_ZongZTL;
    public TextView tv_FengJZS;
    public TextView tv_QuDL;
    public TextView tv_ZhengDS;
    private DecimalFormat df;
    private List<data2> mdatalist;
    private String id;
    private String time;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initview();
        setData(getdata());

    }

    private void initview() {
        mdatalist = new ArrayList<>();
        df = new DecimalFormat("#.0000000");
        tv_mark = (QMUIAlphaTextView) getView().findViewById(R.id.tv_mark);
        tv_time = (QMUIAlphaTextView) getView().findViewById(R.id.tv_time);
        tv_weidu = (TextView) getView().findViewById(R.id.tv_weidu);
        tv_jingdu = (TextView) getView().findViewById(R.id.tv_jingdu);
        tv_bohelun = (TextView) getView().findViewById(R.id.tv_bohelun);
        tv_zuoye = (TextView) getView().findViewById(R.id.tv_zuoye);
        tv_fukuan = (TextView) getView().findViewById(R.id.tv_fukuan);
        tv_getai = (TextView) getView().findViewById(R.id.tv_getai);
        tv_shusongzhou = (TextView) getView().findViewById(R.id.tv_shusongzhou);
        tv_cheshu = (TextView) getView().findViewById(R.id.tv_cheshu);
        tv_QieLTL = (TextView) getView().findViewById(R.id.tv_QieLTL);
        tv_LiZSP = (TextView) getView().findViewById(R.id.tv_LiZSP);
        tv_ZaYSP = (TextView) getView().findViewById(R.id.tv_ZaYSP);
        tv_GeCGD = (TextView) getView().findViewById(R.id.tv_GeCGD);
        tv_QinXSS = (TextView) getView().findViewById(R.id.tv_QinXSS);
        tv_JiaDSS = (TextView) getView().findViewById(R.id.tv_JiaDSS);
        YuLSD = (TextView) getView().findViewById(R.id.YuLSD);
        HanZL = (TextView) getView().findViewById(R.id.HanZL);
        PoSL = (TextView) getView().findViewById(R.id.PoSL);
        LiZLL = (TextView) getView().findViewById(R.id.LiZLL);
        tv_ZongZTL = getView().findViewById(R.id.tv_ZongZTL);
        tv_FengJZS = getView().findViewById(R.id.tv_FengJZS);
        tv_QuDL = getView().findViewById(R.id.tv_QuDL);
        tv_ZhengDS = getView().findViewById(R.id.tv_ZhengDS);
    }

public data2 getdata() {
    IDataStorage dataStorage = DataStorageFactory.getInstance(getActivity(), DataStorageFactory.TYPE_DATABASE);
    List<data2> list = dataStorage.load(data2.class, new Condition<data2>() {
                @Override
                public boolean satisfy(data2 o) {
                    return o.getTime()==time;
                }
            });
    return list.get(list.size()-1);
}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    @Override
    public void onPause() {
        super.onPause();


    }

    public void setid(String s) {
        this.time = s;

    }

    private void setData(data2 data) {
        data2 mdatabean2 = data;
        tv_mark.setText(mdatabean2.getMark());
        tv_time.setText(addDateMinut(mdatabean2.getTime(), 8));


        if (mdatabean2.getGPSerr().equals("0")) {
            if (mdatabean2.getN().equals("") || mdatabean2.getE().equals("")) {
                tv_weidu.setText("ERROR!");

                tv_jingdu.setText("ERROR!");
                tv_jingdu.setTextColor(Color.RED);
                tv_weidu.setTextColor(Color.RED);

            } else {
                tv_weidu.setText(df.format((Double.valueOf(mdatabean2.getN())) / 100));
                tv_weidu.setTextColor(Color.BLACK);
                tv_jingdu.setText(df.format((Double.valueOf(mdatabean2.getE())) / 100));
                tv_jingdu.setTextColor(Color.BLACK);

            }
        }
        if (mdatabean2.getGPSerr().equals("1")) {
            tv_weidu.setText("ERROR!");
            tv_jingdu.setText("ERROR!");
            tv_weidu.setTextColor(Color.RED);
            tv_jingdu.setTextColor(Color.RED);

        }


        if (mdatabean2.getCANerr().equals("0")) {
            tv_bohelun.setText(mdatabean2.getBohelun());
            tv_zuoye.setText(mdatabean2.getZuoye());
            tv_fukuan.setText(mdatabean2.getFukuan());
            tv_getai.setText(mdatabean2.getGetai());
            tv_shusongzhou.setText(mdatabean2.getShusongzhou());
            tv_cheshu.setText(mdatabean2.getChesu());
            tv_QieLTL.setText(mdatabean2.getQieLTL());
            tv_LiZSP.setText(mdatabean2.getLiZSP());
            tv_ZaYSP.setText(mdatabean2.getZaYSP());
            tv_GeCGD.setText(mdatabean2.getGeCGD());
            tv_QinXSS.setText(mdatabean2.getQinXSS());
            tv_JiaDSS.setText(mdatabean2.getJiaDSS());
            YuLSD.setText(mdatabean2.getYuLSD());
            HanZL.setText(mdatabean2.getHanZL());
            PoSL.setText(mdatabean2.getPoSL());
            LiZLL.setText(mdatabean2.getLiZLL());
            tv_ZongZTL.setText(mdatabean2.getZongZTL());
            tv_FengJZS.setText(mdatabean2.getFongJZS());
            tv_QuDL.setText(mdatabean2.getQuDL());
            tv_ZhengDS.setText(mdatabean2.getZhengDS());
        }

        if (mdatabean2.getCANerr().equals("1")) {
            tv_bohelun.setText("NULL");
            tv_zuoye.setText("NULL");
            tv_fukuan.setText("NULL");
            tv_getai.setText("NULL");
            tv_shusongzhou.setText("NULL");
            tv_cheshu.setText("NULL");
            tv_QieLTL.setText("NULL");
            tv_LiZSP.setText("NULL");
            tv_ZaYSP.setText("NULL");
            tv_GeCGD.setText("NULL");
            tv_QinXSS.setText("NULL");
            tv_JiaDSS.setText("NULL");
            YuLSD.setText("NULL");
            HanZL.setText("NULL");
            PoSL.setText("NULL");
            LiZLL.setText("NULL");
            tv_ZongZTL.setText("NULL");
            tv_FengJZS.setText("NULL");
            tv_QuDL.setText("NULL");
            tv_ZhengDS.setText("NULL");
        }
    }

    //当前时间串+n个小时
    public static String addDateMinut(String time, int hour) {
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
