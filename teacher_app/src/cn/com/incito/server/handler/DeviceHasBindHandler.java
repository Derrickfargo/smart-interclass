package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

/**
 * 判断设备是否绑定的处理器
 * @author 刘世平
 *
 */
public class DeviceHasBindHandler extends MessageHandler {
	 
	@Override
	public void handleMessage() {
		String imei = data.getString("imei");
		String result = service.isDeviceBind(imei);
		sendResponse(result);
	}

	private void sendResponse(String json) {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_DEVICE_HAS_BIND);
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
