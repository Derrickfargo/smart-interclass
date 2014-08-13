package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

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
		logger.info("消息类型为学生登陆:" + data);
		
        String uname = data.getString("name");
        int sex = data.getIntValue("sex");
        String number = data.getString("number");
        String imei = data.getString("imei");
        int type = data.getIntValue("type");//0登陆,1取消登陆,2注册
        switch (type){
        case 0:
        	String result = service.login(uname, sex, number, imei);
        	Integer groupId = getGroupId(result);
			if (groupId != -1) {
				Application.getInstance().addSocketChannel(groupId, message.getChannel());
        		sendResponse(result,Application.getInstance().getClientChannelByGroup(groupId));
//				sendResponse(result);
        		logger.info(result);
        	}
        	break;
        case 1:
        	result = service.logout(uname, sex, number, imei);
        	groupId = getGroupId(result);
			if (groupId != -1) {
				Application.getInstance().addSocketChannel(groupId, message.getChannel());
        		sendResponse(result,Application.getInstance().getClientChannelByGroup(groupId));
//				sendResponse(result);
        		logger.info(result);
        	}
    		break;
        case 2:
        	result = service.register(uname, sex, number, imei);
        	groupId = getGroupId(result);
			if (groupId != -1) {
				Application.getInstance().addSocketChannel(groupId, message.getChannel());
        		sendResponse(result,Application.getInstance().getClientChannelByGroup(groupId));
//				sendResponse(result);
        		logger.info(result);
        	}
        	break;
        }
	}
	
	private void sendResponse(String json) {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
        byte[] messageData = messagePacking.pack().array();
        ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
        buffer.put(messageData);
        buffer.flip();
        try {
        	if(message.getChannel().isConnected()){
        		message.getChannel().write(buffer);
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void sendResponse(String json,List<SocketChannel> channels) {
		for (SocketChannel channel : channels) {
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
	        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
	        byte[] messageData = messagePacking.pack().array();
	        ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
	        buffer.put(messageData);
	        buffer.flip();
			try {
				if (channel.isConnected()) { 
					channel.write(buffer);
				}
			} catch (IOException e) {
				e.printStackTrace();
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
