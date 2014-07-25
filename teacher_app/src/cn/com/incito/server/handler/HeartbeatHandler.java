package cn.com.incito.server.handler;

import cn.com.incito.server.core.MessageHandler;

public class HeartbeatHandler extends MessageHandler {

	 
	@Override
	public void handleMessage() {
		System.out.println("收到心跳消息");
	}

}
