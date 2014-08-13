package cn.com.incito.server.handler;

import org.apache.log4j.Logger;

import cn.com.incito.server.core.MessageHandler;

public class HeartbeatHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(HeartbeatHandler.class.getName());
	 
	@Override
	public void handleMessage() {
		logger.info("收到心跳消息");
	}

}
