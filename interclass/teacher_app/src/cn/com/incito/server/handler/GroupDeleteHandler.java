package cn.com.incito.server.handler;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

public class GroupDeleteHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(GroupDeleteHandler.class.getName());
	@Override
	protected void handleMessage() {
		logger.info("收到删除小组消息：" + data);
		int studentId = data.getIntValue("studentId");
		Application app = Application.getInstance();
		Group group = app.getTempGroup().remove(studentId);
		app.getGroupList().remove(group);
		JSONObject result = new JSONObject();
		result.put("code", 0);
		result.put("data", Application.getInstance().getGroupList());
		logger.info("回复删除小组消息：" + result);
		sendResponse(result.toString());
	}
	
	private void sendResponse(String json) {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_DELETE);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
        byte[] messageData = messagePacking.pack().array();
        CoreSocket.getInstance().sendMessage(messageData);
	}
}
