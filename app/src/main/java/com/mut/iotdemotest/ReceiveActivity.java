package com.mut.iotdemotest;

import android.os.Bundle;

import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.tools.ALog;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;

import androidx.annotation.Nullable;

public class ReceiveActivity extends BaseActivity {
    public String message[] = new String[100];
    private String messageContent;
    private int i;
    public static int message_total = 0;
    private QMUIAlphaTextView tv_message_total;
    private QMUIAlphaTextView tv_baoming;
    private QMUIAlphaTextView tv_bohelun;
    private QMUIAlphaTextView tv_zuoye;
    public static final String FORMAT_YEAR_MONTH_DAY = "2020-5-13 16:02:01";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinkKit.getInstance().registerOnPushListener(notifyListener);
        setContentView(R.layout.activity_receive);
        initview();
        if ((Math.abs(TimeUtils.getTimeSpanByNow(FORMAT_YEAR_MONTH_DAY, TimeConstants.SEC))) < 600) {
            tv_message_total.setText("" + TimeUtils.getTimeSpanByNow(FORMAT_YEAR_MONTH_DAY, TimeConstants.MIN));
        }
    }
    private void initview() {
        tv_message_total = (QMUIAlphaTextView) findViewById(R.id.message_total);
        tv_baoming = (QMUIAlphaTextView) findViewById(R.id.baoming);
        tv_bohelun = (QMUIAlphaTextView) findViewById(R.id.bohelun);
        tv_zuoye = (QMUIAlphaTextView) findViewById(R.id.zuoye);
    }
    private IConnectNotifyListener notifyListener = new IConnectNotifyListener() {
        @Override
        //s是LINK_PRESISTENT    s1是topic名称
        public void onNotify(String s, String s1, AMessage aMessage) {
            showToast("收到下行消息 topic=" + s1);
            message_total += 1;
            //数据流转 （设备端的数据发送后到阿里云平台并流转至APP端）
            messageContent = new String((byte[]) aMessage.data);
            Gson gson = new Gson();
            databean databean1 = gson.fromJson(messageContent, databean.class);
            if (databean1 != null) {
//                if(databean1.getBaoming()==1001)
//                    ALog.e(TAG, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                // tv_message_total.setText("目前接收条数："+message_total);
                tv_baoming.setText("包名：" + databean1.getBaoming());
                tv_bohelun.setText("拨禾轮：" + databean1.getBohelun());
                tv_zuoye.setText("作业：" + databean1.getZuoye());

            }
            //textMessages.setText(messageContent);
//            try {
//
//
//                JSONObject firstObject = new JSONObject(messageContent);
//                int baoming = firstObject.getInt("baoming");
//                switch (baoming) {
//                    case 1001:
//                        showToast("" + firstObject.getDouble("bohelun"));
//                        break;
//                    default:
//                        break;
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public boolean shouldHandle(String s, String s1) {
            return true;
        }

        @Override
        public void onConnectStateChange(String s, ConnectState connectState) {
        }
    };
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        message_total = 0;
    }
}
