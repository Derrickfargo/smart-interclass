package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;

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

	@Override
	public void handleMessage() {
		System.out.println("消息类型为学生登陆:" + data);
		
        String uname = data.getString("name");
        int sex = data.getIntValue("sex");
        String number = data.getString("number");
        String imei = data.getString("imei");
        int type = data.getIntValue("type");//0登陆,1取消登陆,2注册
        switch (type){
        case 0:
        	String result = service.login(uname, sex, number, imei);
    		sendResponse(result);
        	break;
        case 1:
        	result = service.logout(uname, sex, number, imei);
    		sendResponse(result);
        case 2:
        	result = service.register(uname, sex, number, imei);
    		sendResponse(result);
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
        	message.getChannel().write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
