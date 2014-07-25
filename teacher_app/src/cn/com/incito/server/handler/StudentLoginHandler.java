package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.com.incito.server.api.ApiClient;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 登陆消息处理器
 * 
 * @author 刘世平
 * 
 */
public class StudentLoginHandler extends MessageHandler {

	@Override
	public void handleMessage() {
		System.out.println("收到登陆消息:" + data);
		
        String uname = data.getString("name");
        int sex = data.getIntValue("sex");
        String number = data.getString("number");
        String imei = data.getString("imei");
        int type = data.getIntValue("type");//0登陆,1取消登陆,2注册
        
		try {
			final String result = ApiClient.loginForStudent(uname, sex, number, imei);
			if (result != null) {
				JSONObject jsonResult = JSON.parseObject(result);
				if (jsonResult.getIntValue("code") != 0) {
					sendResponse(jsonResult.getString("data"));
					return;
				}
				
			}
		} catch (Exception e) {
			System.out.println("学生登陆异常：" + imei);
			System.out.println(e.getMessage());
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
        	message.getChannel().write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
