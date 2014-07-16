package cn.com.incito.server.handler;

import java.io.Serializable;

import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.core.Message;

public class HeartbeatHandler implements MessageHandler {

	 
	@Override
	public Serializable handleMessage(Message msg) {
		System.out.println("收到心跳消息");
		return null;
	}

}
