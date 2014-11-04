package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

public class GroupCreatHandler extends MessageHandler{

	@Override
	protected void handleMessage() {
		Group group=(Group) data.get("group");
		Application.getInstance().addGroup(group);
		Map<String, SocketChannel> channels = Application.getInstance().getClientChannel();
		List<SocketChannel>  channelsRes=new ArrayList<SocketChannel>();
		for (String key : channels.keySet()) {
			channelsRes.add(channels.get(key));
		}
		JSONObject result = new JSONObject();
		result.put("code", 0);
		result.put("data",Application.getInstance().getGroupList());
		sendResponse(result.toString(), channelsRes);
	}
	private void sendResponse(String json, List<SocketChannel> channels) {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_CREAT_RESPONSE);
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
