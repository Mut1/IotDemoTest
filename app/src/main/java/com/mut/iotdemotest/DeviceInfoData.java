package com.mut.iotdemotest;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.dm.api.DeviceInfo;

import java.util.List;

public class DeviceInfoData extends DeviceInfo {
    public List<DeviceInfo> subDevice = null;
    public String password = null;
    public String username = null;
    public String clientId = null;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
