package cn.com.incito.socket.handler;

import cn.com.incito.socket.core.ConnectionManager;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.wisdom.sdk.log.WLog;
/**
 * 登陆处理hanlder
 * Created by liushiping on 2014/7/28.
 */
public class DeviceLoginHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		WLog.i(DeviceLoginHandler.class, "收到登陆回复,连接建立成功,开始启动心跳!");
		//启动心跳检测
		ConnectionManager.getInstance(message.getChannel());
	}

}
