package cn.com.incito.server.core;

import java.util.HashMap;
import java.util.Map;

import cn.com.incito.server.handler.GroupListHandler;
import cn.com.incito.server.handler.HandShakeHandler;
import cn.com.incito.server.handler.HeartbeatHandler;
import cn.com.incito.server.handler.StudentLoginHandler;

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
		//握手消息
		handlerResources.put(Message.MESSAGE_HAND_SHAKE, HandShakeHandler.class);
		//心跳消息
		handlerResources.put(Message.MESSAGE_HEART_BEAT, HeartbeatHandler.class);
		//获取分组消息
		handlerResources.put(Message.MESSAGE_GROUP_LIST, GroupListHandler.class);
		//学生登陆消息
		handlerResources.put(Message.MESSAGE_STUDENT_LOGIN, StudentLoginHandler.class);
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