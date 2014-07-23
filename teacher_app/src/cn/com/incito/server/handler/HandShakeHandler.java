package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

/**
 * 握手消息处理器
 * 
 * @author 刘世平
 * 
 */
public class HandShakeHandler implements MessageHandler {

	private String imei;

	@Override
	public void handleMessage(Message msg) {
		ByteBuffer data = msg.getBodyBuffer();
		data.flip();
		
		//解析IMEI
		byte[] intSize = new byte[4];//int
		data.get(intSize);
		int imeiLength = Integer.parseInt(BufferUtils.decodeIntLittleEndian(intSize, 0, intSize.length) + "");
		byte[] imeiByte = new byte[imeiLength];
		data.get(imeiByte);
		imei = BufferUtils.readUTFString(imeiByte);
		
		System.out.println("收到握手消息，IMEI:" + imei);
		
		//回复握手消息
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HAND_SHAKE);
		byte[] handShakResponse = messagePacking.pack().array();
        ByteBuffer buffer = ByteBuffer.allocate(handShakResponse.length);
        buffer.put(handShakResponse);
        buffer.flip();
        try {
			msg.getChannel().write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
