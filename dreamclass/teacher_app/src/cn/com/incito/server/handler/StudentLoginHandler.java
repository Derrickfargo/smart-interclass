package cn.com.incito.server.handler;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.core.SocketServiceCore;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.JSONUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 登陆消息处理器
 * 
 * @author 刘世平
 * 
 */
public class StudentLoginHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(StudentLoginHandler.class.getName());
	@Override
	public void handleMessage() {
        final String uname = data.getString("name");
        int sex = data.getIntValue("sex");
        final String number = data.getString("number");
        final String imei = data.getString("imei");
        int type = data.getIntValue("type");//0登陆,1取消登陆,2注册
        Application app = Application.getInstance();
        switch (type){
        case 0:
        	logger.info("收到学生登陆消息:" + data);
        	String result = service.login(uname, sex, number, imei);
        	logger.info("登陆:" + result);
        	Integer groupId = getGroupId(result);
			if (groupId != -1) {
				logger.info("回复学生登陆消息:" + result);
				app.addSocketChannel(groupId, ctx);
        		sendResponse(result,app.getClientChannelByGroup(groupId));
        		logger.info(result);
        	}
        	break;
        case 1:
        	logger.info("收到学生退出消息:" + data);
        	result = service.logout(uname, sex, number, imei);
        	logger.info("退出:" + result);
        	groupId = getGroupId(result);
			if (groupId != -1) {
				logger.info("回复学生退出消息:" + result);
				app.addSocketChannel(groupId, ctx);
        		sendResponse(result,app.getClientChannelByGroup(groupId));
        		logger.info(result);
        	}
    		break;
        case 2:
        	logger.info("收到注册学生消息:" + data);
			Student student = service.getStudentByNumber(number);
			if(student != null){//该学号已注册
				result = JSONUtils.renderJSONString(-2, number);// 学号已注册
				logger.info("注册学生结果，学号已注册:" + result);
				MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
				messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(result));
//				byte[] handShakResponse = messagePacking.pack().array();
//		        ByteBuffer buffer = ByteBuffer.allocate(handShakResponse.length);
//		        buffer.put(handShakResponse);
//		        buffer.flip();
//		        ctx.channel().writeAndFlush(buffer);
		        SocketServiceCore.getInstance().sendMsg(messagePacking, ctx);
		        return;
			}
        	result = service.register(uname, sex, number, imei);
        	logger.info("注册学生结果:" + result);
        	groupId = getGroupId(result);
			if (groupId != -1) {
				logger.info("回复注册学生消息:" + result);
				app.addSocketChannel(groupId, ctx);
        		sendResponse(result,app.getClientChannelByGroup(groupId));
        		logger.info(result);
        	}
        	break;
        }
	}
	
	
	private void sendResponse(String json,List<ChannelHandlerContext> channels) {
		for (ChannelHandlerContext channel : channels) {
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
	        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
//	        byte[] messageData = messagePacking.pack().array();
//	        ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
//	        buffer.put(messageData);
//	        buffer.flip();
			if (channel.channel().isActive()) { 
				SocketServiceCore.getInstance().sendMsg(messagePacking, channel);
			}
		}
	}
	
	private Integer getGroupId(String json){
		JSONObject jsonObject = JSON.parseObject(json);
		if(jsonObject.getIntValue("code") != 0){
			return -1;
		}
		String data = jsonObject.getString("data");
		JSONObject dataObject = JSON.parseObject(data);
		return dataObject.getInteger("id");
	}
}
