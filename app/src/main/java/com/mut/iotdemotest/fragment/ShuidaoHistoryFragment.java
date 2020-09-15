package com.mut.iotdemotest.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mut.iotdemotest.R;
import com.mut.iotdemotest.entity.ShuidaoBean;
import com.mut.iotdemotest.utils.TimeUtilsCS;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import xiaofei.library.datastorage.DataStorageFactory;
import xiaofei.library.datastorage.IDataStorage;
import xiaofei.library.datastorage.util.Condition;

public class ShuidaoHistoryFragment extends Fragment {
    private String messageContent;
    private String mark;
    private QMUIAlphaTextView tv_mark;
    private QMUIAlphaTextView tv_time;
    private TextView tv_weidu;
    private TextView tv_jingdu;
    private TextView tv_HXCS;
    private TextView tv_ZXCS;
    private TextView tv_XLGWY;
    private TextView tv_XLGZJ;
    private TextView tv_GTGD;
    private TextView tv_BHLGD;
    private TextView tv_XGD;
    private TextView tv_DPZQ;
    private TextView tv_DPYQ;
    private TextView tv_DPZH;
    private TextView tv_DPYH;
    private TextView tv_QJSD;
    private TextView tv_LZLL;
    private TextView tv_ZGDZS;
    private TextView tv_BHLZS;
    private TextView tv_SSCZS;
    private TextView tv_TLGT;
    private TextView tv_FJZS;
    private TextView tv_SLJLZS;
    private TextView tv_ZYJLZS;
    private TextView tv_QXSS;
    private TextView tv_JDSS;
    private TextView tv_YLKSD;
    private TextView tv_GFKD;
    private TextView tv_HZL;
    private TextView tv_PSL;
    private TextView tv_High;
    private TextView tv_Speed;
    private TextView tv_Path;
    private TextView tv_TLJX;
    private TextView tv_DLBKD;
    private TextView tv_JFKKD;

    private DecimalFormat df;
    private String time;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nowdata, container, false);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initview();
        setData(getdata());

    }

    public ShuidaoBean getdata() {
        IDataStorage dataStorage = DataStorageFactory.getInstance(getActivity(), DataStorageFactory.TYPE_DATABASE);
        List<ShuidaoBean> list = dataStorage.load(ShuidaoBean.class, new Condition<ShuidaoBean>() {
            @Override
            public boolean satisfy(ShuidaoBean o) {
                return o.getTime()==time;
            }
        });
        return list.get(list.size()-1);
    }

    public void setTime(String s) {
        this.time = s;

    }
    private void setData(ShuidaoBean mDatabean) {
        ShuidaoBean mData = mDatabean;
        tv_mark.setText(mData.getMark());
        String time = TimeUtilsCS.date2time(mData.getTime());
        tv_time.setText(TimeUtilsCS.addTimeHour(time, 8));

        if (mData.getGPSerr().equals("0")) {
            if (mData.getN().equals("") || mData.getE().equals("")) {
                tv_weidu.setText("NULL");

                tv_jingdu.setText("NULL");
                tv_jingdu.setTextColor(Color.BLACK);
                tv_weidu.setTextColor(Color.BLACK);
                tv_High.setText(mData.getHigh());
                tv_High.setTextColor(Color.BLACK);
                tv_Speed.setText(mData.getSpeed());
                tv_Speed.setTextColor(Color.BLACK);
                tv_Path.setText(mData.getPath());
                tv_Path.setTextColor(Color.BLACK);

            } else if (TimeUtilsCS.isParseDouble(mData.getE())&&TimeUtilsCS.isParseDouble(mData.getN())){
                tv_weidu.setText(df.format((Double.valueOf(mData.getN())) / 100));
                tv_weidu.setTextColor(Color.BLACK);
                tv_jingdu.setText(df.format((Double.valueOf(mData.getE())) / 100));
                tv_jingdu.setTextColor(Color.BLACK);

                tv_High.setText(mData.getHigh());
                tv_High.setTextColor(Color.BLACK);
                tv_Speed.setText(mData.getSpeed());
                tv_Speed.setTextColor(Color.BLACK);
                tv_Path.setText(mData.getPath());
                tv_Path.setTextColor(Color.BLACK);
            }
            else
            {
                tv_weidu.setText("GPS ERROR!");

                tv_jingdu.setText("GPS ERROR!");
                tv_jingdu.setTextColor(Color.RED);
                tv_weidu.setTextColor(Color.RED);

                tv_High.setText("GPS ERROR!");
                tv_High.setTextColor(Color.RED);
                tv_Speed.setText("GPS ERROR!");
                tv_Speed.setTextColor(Color.RED);
                tv_Path.setText("GPS ERROR!");
                tv_Path.setTextColor(Color.RED);
            }
        }
        if (mData.getGPSerr().equals("1")) {
            tv_weidu.setText("GPS ERROR!");

            tv_jingdu.setText("GPS ERROR!");
            tv_jingdu.setTextColor(Color.RED);
            tv_weidu.setTextColor(Color.RED);

            tv_High.setText("GPS ERROR!");
            tv_High.setTextColor(Color.RED);
            tv_Speed.setText("GPS ERROR!");
            tv_Speed.setTextColor(Color.RED);
            tv_Path.setText("GPS ERROR!");
            tv_Path.setTextColor(Color.RED);

        }


        if (mData.getCANerr().equals("0")) {
            tv_HXCS.setText(mData.getHXCS());
            tv_ZXCS.setText(mData.getZXCS());
            tv_XLGWY.setText(mData.getXLGWY());
            tv_XLGZJ.setText(mData.getXLGZJ());
            tv_GTGD.setText(mData.getGTGD());
            tv_BHLGD.setText(mData.getBHLGD());
            tv_XGD.setText(mData.getXGD());
            tv_DPZQ.setText(mData.getDPZQ());
            tv_DPYQ.setText(mData.getDPYQ());
            tv_DPZH.setText(mData.getDPZH());
            tv_DPYH.setText(mData.getDPYH());
            tv_QJSD.setText(mData.getQJSD());
            tv_LZLL.setText(mData.getLZLL());
            tv_ZGDZS.setText(mData.getZGDZS());
            tv_BHLZS.setText(mData.getBHLZS());
            tv_SSCZS.setText(mData.getSSCZS());
            tv_TLGT.setText(mData.getTLGT());
            tv_FJZS.setText(mData.getFJZS());
            tv_SLJLZS.setText(mData.getSLJLZS());
            tv_ZYJLZS.setText(mData.getZYJLZS());
            tv_QXSS.setText(mData.getQXSS());
            tv_JDSS.setText(mData.getJDSS());
            tv_YLKSD.setText(mData.getYLKSD());
            tv_GFKD.setText(mData.getGFKD());
            tv_HZL.setText(mData.getHZL());
            tv_PSL.setText(mData.getPSL());

            tv_TLJX.setText(mData.getTLJX());
            tv_DLBKD.setText(mData.getDLBKD());
            tv_JFKKD.setText(mData.getJFKKD());

        }

        if (mData.getCANerr().equals("1")) {
            tv_HXCS.setText("NULL");
            tv_ZXCS.setText("NULL");
            tv_XLGWY.setText("NULL");
            tv_XLGZJ.setText("NULL");
            tv_GTGD.setText("NULL");
            tv_BHLGD.setText("NULL");
            tv_XGD.setText("NULL");
            tv_DPZQ.setText("NULL");
            tv_DPYQ.setText("NULL");
            tv_DPZH.setText("NULL");
            tv_DPYH.setText("NULL");
            tv_QJSD.setText("NULL");
            tv_LZLL.setText("NULL");
            tv_ZGDZS.setText("NULL");
            tv_BHLZS.setText("NULL");
            tv_SSCZS.setText("NULL");
            tv_TLGT.setText("NULL");
            tv_FJZS.setText("NULL");
            tv_SLJLZS.setText("NULL");
            tv_ZYJLZS.setText("NULL");
            tv_QXSS.setText("NULL");
            tv_JDSS.setText("NULL");
            tv_YLKSD.setText("NULL");
            tv_GFKD.setText("NULL");
            tv_HZL.setText("NULL");
            tv_PSL.setText("NULL");

            tv_TLJX.setText("NULL");
            tv_DLBKD.setText("NULL");
            tv_JFKKD.setText("NULL");
        }
    }



    private void initview() {
        df = new DecimalFormat("#.0000000");

        tv_mark = (QMUIAlphaTextView) getView().findViewById(R.id.tv_mark);
        tv_time = (QMUIAlphaTextView) getView().findViewById(R.id.tv_time);
        tv_weidu = (TextView) getView().findViewById(R.id.tv_weidu);
        tv_jingdu = (TextView) getView().findViewById(R.id.tv_jingdu);
        tv_HXCS = (TextView) getView().findViewById(R.id.tv_HXCS);
        tv_ZXCS = (TextView) getView().findViewById(R.id.tv_ZXCS);
        tv_XLGWY = (TextView) getView().findViewById(R.id.tv_XLGWY);
        tv_XLGZJ = (TextView) getView().findViewById(R.id.tv_XLGZJ);
        tv_GTGD = (TextView) getView().findViewById(R.id.tv_GTGD);
        tv_BHLGD = (TextView) getView().findViewById(R.id.tv_BHLGD);
        tv_XGD = (TextView) getView().findViewById(R.id.tv_XGD);
        tv_DPZQ = (TextView) getView().findViewById(R.id.tv_DPZQ);
        tv_DPYQ = (TextView) getView().findViewById(R.id.tv_DPYQ);
        tv_DPZH = (TextView) getView().findViewById(R.id.tv_DPZH);
        tv_DPYH = (TextView) getView().findViewById(R.id.tv_DPYH);
        tv_QJSD = (TextView) getView().findViewById(R.id.tv_QJSD);
        tv_LZLL = (TextView) getView().findViewById(R.id.tv_LZLL);
        tv_ZGDZS = (TextView) getView().findViewById(R.id.tv_ZGDZS);
        tv_BHLZS = (TextView) getView().findViewById(R.id.tv_BHLZS);
        tv_SSCZS = (TextView) getView().findViewById(R.id.tv_SSCZS);
        tv_TLGT = (TextView) getView().findViewById(R.id.tv_TLGT);
        tv_FJZS = (TextView) getView().findViewById(R.id.tv_FJZS);
        tv_SLJLZS = (TextView) getView().findViewById(R.id.tv_SLJLZS);
        tv_ZYJLZS = (TextView) getView().findViewById(R.id.tv_ZYJLZS);
        tv_QXSS = (TextView) getView().findViewById(R.id.tv_QXSS);
        tv_JDSS = (TextView) getView().findViewById(R.id.tv_JDSS);
        tv_YLKSD = (TextView) getView().findViewById(R.id.tv_YLKSD);
        tv_GFKD = (TextView) getView().findViewById(R.id.tv_GFKD);
        tv_HZL = (TextView) getView().findViewById(R.id.tv_HZL);
        tv_PSL = (TextView) getView().findViewById(R.id.tv_PSL);
        tv_High = (TextView) getView().findViewById(R.id.tv_High);
        tv_Speed = (TextView) getView().findViewById(R.id.tv_Speed);
        tv_Path = (TextView) getView().findViewById(R.id.tv_Path);

        tv_TLJX = (TextView) getView().findViewById(R.id.tv_TLJX);
        tv_DLBKD = (TextView) getView().findViewById(R.id.tv_DLBKD);
        tv_JFKKD = (TextView) getView().findViewById(R.id.tv_JFKKD);



    }

}
