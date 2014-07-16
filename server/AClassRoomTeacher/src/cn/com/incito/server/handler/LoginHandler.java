package cn.com.incito.server.handler;

import java.io.Serializable;
import java.nio.ByteBuffer;

import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.ApiClient;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.utils.BufferUtils;

import com.alibaba.fastjson.JSON;

/**
 * 登陆消息处理器
 * 
 * @author 刘世平
 * 
 */
public class LoginHandler implements MessageHandler {

	private String imei;

	@Override
	public Serializable handleMessage(Message msg) {
		ByteBuffer data = msg.getBodyBuffer();
		data.flip();
		
		//解析IMEI
		byte[] intSize = new byte[4];//int
		data.get(intSize);
		int imeiLength = Integer.parseInt(BufferUtils.decodeIntLittleEndian(intSize, 0, intSize.length) + "");
		byte[] imeiByte = new byte[imeiLength];
		data.get(imeiByte);
		imei = BufferUtils.readUTFString(imeiByte);
		
		System.out.println("收到登陆消息，IMEI:" + imei);
		try {
			final String result = ApiClient.loginForStudent(imei);
			if (result != null) {
				return JSON.parseObject(result, Student.class);
			}
		} catch (Exception e) {
			System.out.println("学生登陆异常：" + imei);
			System.out.println(e.getMessage());
		}
		return null;
	}

}
