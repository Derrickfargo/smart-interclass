package cn.com.incito.server.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.incito.server.handler.DeviceBindHandler;
import cn.com.incito.server.handler.DeviceHasBindHandler;
import cn.com.incito.server.handler.DeviceLoginHandler;
import cn.com.incito.server.handler.DeviceLogoutHandler;
import cn.com.incito.server.handler.GroupListHandler;
import cn.com.incito.server.handler.GroupSubmitHandler;
import cn.com.incito.server.handler.GroupVoteHandler;
import cn.com.incito.server.handler.HeartbeatHandler;
import cn.com.incito.server.handler.QuizEvaluateSubmitHandler;
import cn.com.incito.server.handler.ReconnectionHandler;
import cn.com.incito.server.handler.ResponderHandler;
import cn.com.incito.server.handler.SavePaperHandler;
import cn.com.incito.server.handler.SendPaperHandler;
import cn.com.incito.server.handler.StudentLoginHandler;

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
		//握手消息
		handlerResources.put(Message.MESSAGE_HAND_SHAKE, DeviceLoginHandler.class);
		//心跳消息
		handlerResources.put(Message.MESSAGE_HEART_BEAT, HeartbeatHandler.class);
		//获取分组消息
		handlerResources.put(Message.MESSAGE_GROUP_LIST, GroupListHandler.class);
		//学生登陆消息
		handlerResources.put(Message.MESSAGE_STUDENT_LOGIN, StudentLoginHandler.class);
		//设备是否绑定消息
		handlerResources.put(Message.MESSAGE_DEVICE_HAS_BIND, DeviceHasBindHandler.class);
		//设备绑定消息
		handlerResources.put(Message.MESSAGE_DEVICE_BIND, DeviceBindHandler.class);
		//编辑组信息
		handlerResources.put(Message.MESSAGE_GROUP_EDIT, GroupSubmitHandler.class);
		//小组投票消息
		handlerResources.put(Message.MESSAGE_GROUP_VOTE, GroupVoteHandler.class);
		//收作业信息
		handlerResources.put(Message.MESSAGE_SAVE_PAPER, SavePaperHandler.class);
		//作业提交申请
		handlerResources.put(Message.MESSAGE_SEND_PAPER, SendPaperHandler.class);
		//设备退出
		handlerResources.put(Message.MESSAGE_DEVICE_LOGOUT, DeviceLogoutHandler.class);
		//抢答结束
		handlerResources.put(Message.MESSAGE_STUDENT_RESPONDER, ResponderHandler.class);
		//提交互评
		handlerResources.put(Message.MESSAGE_QUIZ_FEEDBACK_SUBMIT, QuizEvaluateSubmitHandler.class);
		//设备重连
		handlerResources.put(Message.MESSAGE_RECONNECT, ReconnectionHandler.class);
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
