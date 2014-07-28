package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.LoginResVo;
import cn.com.incito.socket.core.MessageHandler;

import com.popoy.common.TAApplication;

/**
 * 登陆消息处理器
 *
 * @author 刘世平
 */
public class SytemInitHandler1 extends MessageHandler {

    @Override
    public void handleMessage() {
    	System.out.println("收到获取分组回复消息:" + data);
    	
        String code = data.getString("code");
        if ("0".equals(code)) {
            LoginResVo loginResVo = data.getObject("data", LoginResVo.class);
            ((MyApplication) TAApplication.getApplication()).setLoginResVo(loginResVo);
        }else {

        }
    }

}
