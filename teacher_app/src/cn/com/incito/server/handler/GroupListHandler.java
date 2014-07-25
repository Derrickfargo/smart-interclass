package cn.com.incito.server.handler;

import cn.com.incito.server.core.MessageHandler;

/**
 * 获取分组消息处理器
 * 
 * @author 刘世平
 * 
 */
public class GroupListHandler extends MessageHandler {

	@Override
	public void handleMessage() {
		System.out.println("收到获取分组消息:" + data);
	}

}
