package cn.com.incito.server.handler;

import org.apache.log4j.Logger;

import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.core.SocketServiceCore;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

/**
 * 判断设备是否绑定的处理器
 * @author 刘世平
 *
 */
public class DeviceHasBindHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(DeviceHasBindHandler.class.getName());
	
	@Override
	public void handleMessage() {
		logger.info("收到设备是否绑定消息:" + data.toJSONString());
		String imei = data.getString("imei");
		String result = service.isDeviceBind(imei);
		sendResponse(result);
	}

	private void sendResponse(String json) {
		logger.info("回复设备是否绑定消息:" + json);
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_DEVICE_HAS_BIND);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
//        byte[] messageData = messagePacking.pack().array();
//        ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
//        buffer.put(messageData);
//        buffer.flip();
//        ctx.channel().writeAndFlush(buffer);
        SocketServiceCore.getInstance().sendMsg(messagePacking, ctx);
	}
}
