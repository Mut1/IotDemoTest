package com.mut.iotdemotest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mut.iotdemotest.entity.data2;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {

    private String messageContent;
    private String mark;
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
   private DecimalFormat df ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       initview();

        tv_mark.setText(mark);

    }

    private void initview() {
        df =new DecimalFormat("#.000");
        tv_mark = (QMUIAlphaTextView) getView().findViewById(R.id.tv_mark);
        tv_time = (QMUIAlphaTextView) getView().findViewById(R.id.tv_time);
        tv_weidu = (TextView) getView().findViewById(R.id.tv_weidu);
        tv_jingdu = (TextView) getView().findViewById(R.id.tv_jingdu);
        tv_bohelun = (TextView) getView().findViewById(R.id.tv_bohelun);
        tv_zuoye = (TextView) getView().findViewById(R.id.tv_zuoye);
        tv_fukuan = (TextView)getView().findViewById(R.id.tv_fukuan);
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
// TODO

        // message_total.setText((ResultActivity)getActivity().getchexing());
        messageContent = event.getJSON();
        //  mchexing=event.getChexing();
        Gson gson = new Gson();
        data2 mdatabean2= gson.fromJson(messageContent,data2.class);
        if (mdatabean2 != null) {
            if ((mdatabean2.getMark()).equals(mark)) {
//  ALog.e(TAG, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                setData(mdatabean2);
            }
        }

    }
    ;

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
        EventBus.getDefault().register(this);
//        messageContent="{\"Chesu\":\"72\",\"HanZL\":\"4\",\"E\":\"1920.658\",\"Getai\":\"86\",\"Mark\":\"A100101\",\"Time\":\"12:32:5\",\"N\":\"230.125\",\"Fukuan\":\"338\",\"QieLTL\":\"17\",\"LiZSP\":\"690\",\"ZongZTL\":\"680\",\"Shusongzhou\":\"192\",\"PoSL\":\"0\",\"QuDL\":\"14\",\"Zuoye\":\"0\",\"ZaYSP\":\"14\",\"QinXSS\":\"9\",\"ZhengDS\":\"332\",\"Bohelun\":\"27\",\"GeCGD\":\"44\",\"JiaDSS\":\"583\",\"FongJZS\":\"1495\",\"YuLSD\":\"9\",\"LiZLL\":\"3\"}";
//        Gson gson = new Gson();
//        data2 mdatabean2= gson.fromJson(messageContent,data2.class);
//        if (mdatabean2 != null) {
//
//            setData(mdatabean2);
//
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

    }

    public void setMchexing(String s) {
        this.mark = s;

    }
    private void setData(data2 data) {
        data2 mdatabean2 = data;
        tv_time.setText(addDateMinut(mdatabean2.getTime(),8));
        tv_weidu.setText(df.format((Double.valueOf(mdatabean2.getN()))/100));
        tv_jingdu.setText(df.format((Double.valueOf(mdatabean2.getE()))/100));
        tv_bohelun.setText(mdatabean2.getBohelun());
        tv_zuoye .setText(mdatabean2.getZuoye());
        tv_fukuan .setText(mdatabean2.getFukuan());
        tv_getai.setText(mdatabean2.getGetai());
        tv_shusongzhou .setText(mdatabean2.getShusongzhou());
        tv_cheshu .setText(mdatabean2.getChesu());
        tv_QieLTL .setText(mdatabean2.getQieLTL());
        tv_LiZSP .setText(mdatabean2.getLiZSP());
        tv_ZaYSP .setText(mdatabean2.getZaYSP());
        tv_GeCGD .setText(mdatabean2.getGeCGD());
        tv_QinXSS .setText(mdatabean2.getQinXSS());
        tv_JiaDSS.setText(mdatabean2.getJiaDSS());
        YuLSD .setText(mdatabean2.getYuLSD());
        HanZL .setText(mdatabean2.getHanZL());
        PoSL .setText(mdatabean2.getPoSL());
        LiZLL .setText(mdatabean2.getLiZLL());
    }
    //当前时间串+n个小时
    public static String addDateMinut(String time, int hour){
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
