package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

public class GroupSubmitHandler extends MessageHandler {

	@Override
	public void handleMessage() {
		System.out.println("消息类型为分组信息提交确认:" + data);
		
		int id = data.getIntValue("id");
		Application.getInstance().getTempGroup().put(id, data);
		List<SocketChannel> channels = service.getGroupSocketChannelByGroupId(id);
		JSONObject result = new JSONObject();
		result.put("code", 0);
		result.put("data", data);
		sendResponse(result.toString(), channels);
	}

	private void sendResponse(String json, List<SocketChannel> channels) {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_CONFIRM);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
        byte[] messageData = messagePacking.pack().array();
        ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
        for(SocketChannel channel: channels){
			buffer.clear();
			buffer.put(messageData);
			buffer.flip();
			try {
				channel.write(buffer);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
}