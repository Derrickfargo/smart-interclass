package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.JSONUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 设备退出
 * 
 * @author 刘世平
 * 
 */
public class DeviceLogoutHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(DeviceLogoutHandler.class.getName());
	private String imei;

	@Override
	public void handleMessage() {
		imei = data.getString("imei");
		logger.info("收到设备退出消息，IMEI:" + imei);
		Group group = service.deviceLogout(imei);
		List<SocketChannel> channels = Application.getInstance().getClientChannelByGroup(group.getId());
		JSONObject json = new JSONObject();
		json.put("code", JSONUtils.SUCCESS);
		json.put("data", group);
		sendResponse(json.toJSONString(), channels);
		
		SocketChannel channel = message.getChannel();
		if (channel != null) {
			try {
				channel.close();
			} catch (IOException e) {
				
			}
		}
		
	}

	private void sendResponse(String json,List<SocketChannel> channels) {
		for (SocketChannel channel : channels) {
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
	        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
	        byte[] messageData = messagePacking.pack().array();
	        ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
	        buffer.put(messageData);
	        buffer.flip();
			try {
				if (channel.isConnected()) { 
					channel.write(buffer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
