package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreService;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.exception.AppException;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

public class GroupJoinHandler extends MessageHandler{

	@Override
	protected void handleMessage() {
		int groupId =Integer.parseInt((String) data.get("group")) ;
		String deviceId=(String) data.get("imei");
		Device  device=new Device();
		device.setImei(deviceId);
		List<SocketChannel> channels = service.getGroupSocketChannelByGroupId(groupId);
		List<Group> groupList=Application.getInstance().getGroupList();
		for (int i = 0; i < groupList.size(); i++) {
			if(groupList.get(i).getId()==groupId){
				groupList.get(i).getDevices().add(device);
			}
		}
		
		JSONObject result = new JSONObject();
		result.put("code", 0);
		result.put("data",Application.getInstance().getGroupList());
		sendResponse(result.toString(), channels);
	}
	private void sendResponse(String json, List<SocketChannel> channels) {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_JOIN_RESPONSE);
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
