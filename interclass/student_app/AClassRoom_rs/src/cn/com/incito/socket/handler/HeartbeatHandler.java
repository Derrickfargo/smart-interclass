package cn.com.incito.socket.handler;

import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;

public class HeartbeatHandler implements MessageHandler {

	 
	@Override
	public void handleMessage(Message msg) {
		System.out.println("收到心跳消息");
	}

}
