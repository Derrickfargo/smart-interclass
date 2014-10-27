package cn.com.incito.socket.handler;

import cn.com.incito.socket.core.ConnectionManager;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.wisdom.sdk.log.WLog;

/**
 * 心跳处理hanlder Created by liushiping on 2014/7/28.
 */
public class HeartbeatHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		// 收到心跳回复
		WLog.i(HeartbeatHandler.class, "收到服务端心跳包!");
		ConnectionManager.getInstance(message.getChannel()).heartbeat();
	}

}
