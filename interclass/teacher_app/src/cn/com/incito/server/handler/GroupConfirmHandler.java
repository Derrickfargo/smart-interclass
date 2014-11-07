package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.ui.MainFrame;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreService;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.exception.AppException;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

public class GroupConfirmHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(GroupCreatHandler.class.getName());

	@Override
	protected void handleMessage() {
		logger.info("收到pad端的提交小组信息：" + data);
		Group group = data.getObject("group", Group.class);
		Application.getInstance().getTempGroup().remove(group.getCaptainid());
		List<Group> groupList=new ArrayList<Group>();
		//遍历临时分组 将还没有分组的小组列表传回给pad端
		for (Integer key : Application.getInstance().getTempGroup().keySet()) {
			groupList.add(Application.getInstance().getTempGroup().get(key));
		}
		try {
			CoreService.saveGroup(group);
			logger.info("保存小组信息到数据库返回：" + CoreService.saveGroup(group));
		} catch (AppException e) {
			e.printStackTrace();
		}
		MainFrame.getInstance().refresh();
		Map<String, SocketChannel> channels = Application.getInstance().getClientChannel();
		List<SocketChannel> channelsRes = new ArrayList<SocketChannel>();
		for (String key : channels.keySet()) {
			channelsRes.add(channels.get(key));
		}
		JSONObject result = new JSONObject();
		result.put("code", 0);
		result.put("data", groupList);
		logger.info("确认分组消息返回内容：" + result);
		sendResponse(result.toString(), channelsRes);
	}

	private void sendResponse(String json, List<SocketChannel> channels) {
		MessagePacking messagePacking = new MessagePacking(
				Message.MESSAGE_GROUP_CONFIRM);
		messagePacking.putBodyData(DataType.INT,
				BufferUtils.writeUTFString(json));
		byte[] messageData = messagePacking.pack().array();
		ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
		for (SocketChannel channel : channels) {
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
