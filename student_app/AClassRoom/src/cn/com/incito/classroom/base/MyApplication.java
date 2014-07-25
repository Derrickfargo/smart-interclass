package cn.com.incito.classroom.base;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import cn.com.incito.classroom.service.SocketService;
import cn.com.incito.classroom.vo.LoginResVo;

import com.popoy.common.TAApplication;

public class MyApplication extends TAApplication {
    private LoginResVo loginResVo;
    public static String deviceId;

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }

    private void initApplication() {
        TelephonyManager tm = (TelephonyManager) TAApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = tm.getDeviceId();
        Intent service = new Intent(this, SocketService.class);
        startService(service);
    }

    public LoginResVo getLoginResVo() {
        return loginResVo;
    }

    public void setLoginResVo(LoginResVo loginResVo) {
        this.loginResVo = loginResVo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
