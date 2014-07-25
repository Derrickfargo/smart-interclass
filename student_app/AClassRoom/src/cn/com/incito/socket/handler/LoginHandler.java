package cn.com.incito.socket.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.popoy.common.TAApplication;

import java.nio.ByteBuffer;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.LoginResVo;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.utils.BufferUtils;

/**
 * 登陆消息处理器
 *
 * @author 刘世平
 */
public class LoginHandler implements MessageHandler {

    @Override
    public void handleMessage(Message msg) {
        ByteBuffer data = msg.getBodyBuffer();
        data.flip();

        // 获取Imei(占位一个byte)长度
        int length = data.get();
        byte[] bytes = new byte[length];
        data.get(bytes);
        // 封装imei
        String json = BufferUtils.readUTFString(bytes);
        JSONObject jsonObject = JSON.parseObject(json);
        String code = jsonObject.getString("code");
        if ("0".equals(code)) {
            LoginResVo loginResVo = jsonObject.getObject("data", LoginResVo.class);
            ((MyApplication) TAApplication.getApplication()).setLoginResVo(loginResVo);
        }
        System.out.println("收到登陆消息:" + json);
    }

}
