package com.mut.iotdemotest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogMenuItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {
    public QMUIAlphaTextView message_total;
    public QMUIAlphaTextView baoming;
    public QMUIAlphaTextView bohelun;
    public QMUIAlphaTextView zuoye;
    private String messageContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        message_total = (QMUIAlphaTextView) getView().findViewById(R.id.message_total);
        baoming = (QMUIAlphaTextView) getView().findViewById(R.id.baoming);
        bohelun = (QMUIAlphaTextView) getView().findViewById(R.id.bohelun);
        zuoye = (QMUIAlphaTextView) getView().findViewById(R.id.zuoye);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
// TODO
        // message_total.setText((ResultActivity)getActivity().getchexing());
        messageContent = event.getJSON();
        Gson gson = new Gson();
        databean databean1 = gson.fromJson(messageContent, databean.class);
        if (databean1 != null) {
            if ((databean1.getBaoming()) == 1001) {
//  ALog.e(TAG, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                // tv_message_total.setText("目前接收条数："+message_total);
                baoming.setText("包名：" + databean1.getBaoming());
                //  tv_bohelun.setText("拨禾轮："+databean1.getBohelun());
                //  tv_zuoye.setText("作业："+databean1.getZuoye());

            } else {
                baoming.setText("chexing+" + event.getChexing());
            }
        }
        message_total.setText(event.getChexing());
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
}
