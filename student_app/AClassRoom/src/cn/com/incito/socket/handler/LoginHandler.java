package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.LoginResVo;
import cn.com.incito.socket.core.MessageHandler;

import com.alibaba.fastjson.JSONObject;
import com.popoy.common.TAApplication;

/**
 * 登陆消息处理器
 *
 * @author 刘世平
 */
public class LoginHandler extends MessageHandler {

    @Override
    public void handleMessage() {
        System.out.println("收到登录回复消息:" + data);

        String code = data.getString("code");
        if ("0".equals(code)) {
            LoginResVo loginResVo = data.getObject("data", LoginResVo.class);
            ((MyApplication) TAApplication.getApplication()).setLoginResVo(loginResVo);
        } else {

        }

    }

}
