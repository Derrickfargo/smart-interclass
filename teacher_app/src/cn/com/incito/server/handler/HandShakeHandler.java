package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.MessagePacking;

/**
 * 握手消息处理器
 * 
 * @author 刘世平
 * 
 */
public class HandShakeHandler extends MessageHandler {

	private String imei;

	@Override
	public void handleMessage() {
		imei = data.getString("imei");
		System.out.println("收到握手消息，IMEI:" + imei);
		
		//回复握手消息
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HAND_SHAKE);
		byte[] handShakResponse = messagePacking.pack().array();
        ByteBuffer buffer = ByteBuffer.allocate(handShakResponse.length);
        buffer.put(handShakResponse);
        buffer.flip();
        try {
			message.getChannel().write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
