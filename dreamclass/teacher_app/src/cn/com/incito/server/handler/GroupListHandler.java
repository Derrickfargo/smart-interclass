package cn.com.incito.server.handler;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.core.SocketServiceCore;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

/**
 * 获取分组消息处理器
 * 
 * @author 刘世平
 * 
 */
public class GroupListHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(GroupListHandler.class.getName());
	
	@Override
	public void handleMessage() {
		logger.info("收到获取分组消息:" + data);
		
		String imei = data.getString("imei");
		logger.info("IMEI:" + imei);
		//需要给组中所以的设备发送
		String result = service.getGroupByIMEI(imei);
		logger.info(result);
		sendResponse(result);
	}

	private void sendResponse(String json) {
		logger.info("回复获取分组消息:" + json);
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_LIST);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
//        byte[] messageData = messagePacking.pack().array();
//        ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
//        buffer.put(messageData);
//        buffer.flip();
//        ctx.channel().writeAndFlush(buffer);
        SocketServiceCore.getInstance().sendMsg(messagePacking, ctx);
	}
}
