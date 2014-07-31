package cn.com.incito.socket.handler;

import android.os.Bundle;

import com.popoy.common.TAApplication;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.LoginResVo;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 登陆消息处理器
 *
 * @author 刘世平
 */
public class DeviceHasBindHandler extends MessageHandler {
    @Override
    protected void handleMessage(Bundle data) {

    }

//    @Override
//    public void handleMessage() {
//        System.out.println("收到是否绑定回复消息:" + data);
//
//        String code = data.getString("code");
//        if ("0".equals(code)) {
//
//        }
//
//    }

}
