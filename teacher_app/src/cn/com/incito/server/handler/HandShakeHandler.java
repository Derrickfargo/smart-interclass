package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import cn.com.incito.server.api.Application;
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
	private Logger logger = Logger.getLogger(HandShakeHandler.class.getName());
	private String imei;

	@Override
	public void handleMessage() {
		imei = data.getString("imei");
		logger.info("收到设备登陆消息，IMEI:" + imei);
		service.deviceLogin(imei);
		Application.getInstance().addSocketChannel(imei, message.getChannel());
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
