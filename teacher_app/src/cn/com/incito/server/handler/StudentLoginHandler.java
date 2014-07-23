package cn.com.incito.server.handler;

import java.nio.ByteBuffer;

import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.utils.BufferUtils;

/**
 * 登陆消息处理器
 * 
 * @author 刘世平
 * 
 */
public class StudentLoginHandler implements MessageHandler {

	@Override
	public void handleMessage(Message msg) {
		ByteBuffer data = msg.getBodyBuffer();
		data.flip();
		
		//解析JSON
		byte[] intSize = new byte[4];//int
		data.get(intSize);
		int jsonLength = Integer.parseInt(BufferUtils.decodeIntLittleEndian(intSize, 0, intSize.length) + "");
		byte[] jsonByte = new byte[jsonLength];
		data.get(jsonByte);
		String json = BufferUtils.readUTFString(jsonByte);
		
		System.out.println("收到登陆消息，JSON:" + json);
//		try {
//			final String result = ApiClient.loginForStudent(imei);
//			if (result != null) {
//				return JSON.parseObject(result, Student.class);
//			}
//		} catch (Exception e) {
//			System.out.println("学生登陆异常：" + imei);
//			System.out.println(e.getMessage());
//		}
	}

}
