package cn.com.incito.server.handler;

import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;

public class HeartbeatHandler implements MessageHandler {

	 
	@Override
	public void handleMessage(Message msg) {
		System.out.println("收到心跳消息");
	}

}
