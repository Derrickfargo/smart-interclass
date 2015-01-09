package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import cn.com.incito.server.core.ConnectionManager;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

public class HeartbeatHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		// TODO Auto-generated method stub
		
	}
//	private Logger logger = Logger.getLogger(HeartbeatHandler.class.getName());
//	 
//	@Override
//	public void handleMessage() {
//		logger.info("收到客户端心跳包：" + data);
//		String imei = data.getString("imei");
//		//通知心跳
//		ConnectionManager.notification(imei, message.getChannel());
//		sendResponse(data.toJSONString());
//	}
//
//	private void sendResponse(String json) {
//		logger.info("回复客户端心跳包：" + json);
//		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HEART_BEAT);
//        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
//        byte[] messageData = messagePacking.pack().array();
//        ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
//        buffer.put(messageData);
//        buffer.flip();
//        try {
//        	if(message.getChannel().isConnected()){
//        		message.getChannel().write(buffer);
//        	}
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//	}
}
