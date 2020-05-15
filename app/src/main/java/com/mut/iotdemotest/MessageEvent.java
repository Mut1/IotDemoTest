package com.mut.iotdemotest;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import androidx.annotation.NonNull;

public class MessageEvent {
    private String mJSON;

    public MessageEvent(String JSON, String chexing) {
        mJSON = JSON;
        this.chexing = chexing;
    }

    public String getJSON() {
        return mJSON;
    }

    public void setJSON(String JSON) {
        mJSON = JSON;
    }

    public String getChexing() {
        return chexing;
    }

    public void setChexing(String chexing) {
        this.chexing = chexing;
    }

    private String chexing;

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
