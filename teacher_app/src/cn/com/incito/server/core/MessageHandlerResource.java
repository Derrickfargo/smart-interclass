package cn.com.incito.server.core;

import java.util.HashMap;
import java.util.Map;

import cn.com.incito.server.handler.HeartbeatHandler;
import cn.com.incito.server.handler.LoginHandler;

/**
 * 消息处理器列表 
 * 该类用来维护消息和消息处理器的关系
 * 
 * @author 刘世平
 */
public final class MessageHandlerResource {

	private static MessageHandlerResource resources;
	private Map<Byte, Class<? extends MessageHandler>> handlerResources;
	
	public static MessageHandlerResource getHandlerResources() {
		if (resources == null) {
			resources = new MessageHandlerResource();
		}
		return resources;
	}
	
	private MessageHandlerResource(){
		handlerResources = new HashMap<Byte, Class<? extends MessageHandler>>();
		//心跳消息
		handlerResources.put(Message.MESSAGE_HEARTBEAT, HeartbeatHandler.class);
		//学生登陆消息
		handlerResources.put(Message.MESSAGE_LOGIN, LoginHandler.class);
	}
	
	public MessageHandler getMessageHandler(Byte key){
		// 查询是否有该消息ID的消息处理器
		if (handlerResources.containsKey(key)) {
			try {
				// 通过反射取得对应的处理器
				return handlerResources.get(key).newInstance();
			} catch (Exception e) {
				System.out.println("获取MessageHandler出错:" + e.getMessage());
				return null;
			}
		}
		return null;
	}
	
}
