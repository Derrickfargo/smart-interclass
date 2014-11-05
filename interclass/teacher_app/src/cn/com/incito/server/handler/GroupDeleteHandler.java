package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

public class GroupDeleteHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		int groupId = data.getIntValue("groupId");
		for (int i = 0; i < Application.getInstance().getGroupList().size(); i++) {
			int id=Application.getInstance().getGroupList().get(i).getId();
			if(id==groupId){
				Application.getInstance().getGroupList().remove(i);
			}
		}
		List<SocketChannel> channels = service.getGroupSocketChannelByGroupId(groupId);
		JSONObject result = new JSONObject();
		result.put("code", 0);
		result.put("data", Application.getInstance().getGroupList());
		sendResponse(result.toString(),channels);
	}
	private void sendResponse(String json, List<SocketChannel> channels) {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_DELETE);
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
