package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

public class GroupCreatHandler extends MessageHandler{
	private Logger logger = Logger.getLogger(GroupCreatHandler.class.getName());
	
	@Override
	protected void handleMessage() {
		logger.info("收到创建分组消息：" + data);
		Group group = data.getObject("group", Group.class);
		Student student = data.getObject("student", Student.class);
		group.getStudents().add(student);
		group.setCaptainid(student.getId());
		Application.getInstance().addGroup(group);
		Application.getInstance().getTempGroup().put(student.getId(), group);
		
		Map<String, SocketChannel> channels = Application.getInstance().getClientChannel();
		List<SocketChannel>  channelsRes=new ArrayList<SocketChannel>();
		for (String key : channels.keySet()) {
			channelsRes.add(channels.get(key));
		}
		JSONObject result = new JSONObject();
		result.put("code", 0);
		result.put("data",Application.getInstance().getGroupList());
		logger.info("回复创建分组消息：" + result);
		sendResponse(result.toString(), channelsRes);
	}
	private void sendResponse(String json, List<SocketChannel> channels) {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_SUBMIT);
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
