package cn.com.incito.socket.handler;

import java.nio.ByteBuffer;

import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.utils.BufferUtils;

/**
 * 登陆消息处理器
 * 
 * @author 刘世平
 * 
 */
public class LoginHandler implements MessageHandler {

	private String imei;

	@Override
	public void handleMessage(Message msg) {
		ByteBuffer data = msg.getBodyBuffer();
		data.flip();
		
		// 获取Imei(占位一个byte)长度
		int imeiLength = data.get();
		byte[] imeiByte = new byte[imeiLength];
		data.get(imeiByte);
		// 封装imei
		imei = BufferUtils.readUTFString(imeiByte);
		
		System.out.println("收到登陆消息，IMEI:" + imei);
	}

}
