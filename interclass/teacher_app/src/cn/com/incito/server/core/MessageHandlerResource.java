package cn.com.incito.server.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.incito.server.handler.StudentLoginHandler;
import cn.com.incito.server.handler.StudentLogoutHandler;
import cn.com.incito.server.handler.GroupCreatHandler;
import cn.com.incito.server.handler.GroupDeleteHandler;
import cn.com.incito.server.handler.GroupJoinHandler;
import cn.com.incito.server.handler.HeartbeatHandler;
import cn.com.incito.server.handler.SavePaperHandler;

/**
 * 消息处理器列表 
 * 该类用来维护消息和消息处理器的关系
 * 
 * @author 刘世平
 */
public final class MessageHandlerResource {
	private Logger logger = Logger.getLogger(MessageHandlerResource.class.getName());
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
		
		//设备（学生）登陆消息
		handlerResources.put(Message.MESSAGE_STUDENT_LOGIN, StudentLoginHandler.class);
		//心跳消息
		handlerResources.put(Message.MESSAGE_HEART_BEAT, HeartbeatHandler.class);
		
		//创建分组消息
		handlerResources.put(Message.MESSAGE_GROUP_CREATE, GroupCreatHandler.class);
		//加入小组
		handlerResources.put(Message.MESSAGE_GROUP_JOIN,GroupJoinHandler.class);
		//删除小组
		handlerResources.put(Message.MESSAGE_GROUP_DELETE, GroupDeleteHandler.class);
		
		//收作业信息
		handlerResources.put(Message.MESSAGE_SAVE_PAPER, SavePaperHandler.class);
		
		//设备退出
		handlerResources.put(Message.MESSAGE_DEVICE_LOGOUT, StudentLogoutHandler.class);
	}
	
	public MessageHandler getMessageHandler(Byte key){
		// 查询是否有该消息ID的消息处理器
		if (handlerResources.containsKey(key)) {
			try {
				// 通过反射取得对应的处理器
				return handlerResources.get(key).newInstance();
			} catch (Exception e) {
				logger.error("获取MessageHandler出错:" + e.getMessage());
				return null;
			}
		}
		return null;
	}
	
}
