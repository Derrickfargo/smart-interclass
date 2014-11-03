package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

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
        final String imei = data.getString("imei");
        logger.info("登陆0-----------------------:" + imei);
        Application app = Application.getInstance();
        	String result = service.login(imei);
        	logger.info("收到设备登陆消息:" + result);
        	
        	SocketChannel mSocketChannel=app.getClientChannel().get("imei");
        	sendResponse(result,mSocketChannel);
	}
	private void sendResponse(String json,SocketChannel channels) {
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_BIND);
	        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
	        byte[] messageData = messagePacking.pack().array();
	        ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
	        buffer.put(messageData);
	        buffer.flip();
			try {
				if (channels.isConnected()) { 
					channels.write(buffer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
}
