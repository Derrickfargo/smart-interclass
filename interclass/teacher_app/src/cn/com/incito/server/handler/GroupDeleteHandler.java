package cn.com.incito.server.handler;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

public class GroupDeleteHandler extends MessageHandler {
	private Logger logger = Logger
			.getLogger(GroupDeleteHandler.class.getName());

	@Override
	protected void handleMessage() {
		logger.info("收到删除小组消息：" + data);
		int captainId = data.getIntValue("captainId");
		int studentId = data.getIntValue("studentId");
		JSONObject result = new JSONObject();
		Application app = Application.getInstance();
		if (captainId == studentId) {
			Group group = app.getTempGroup().remove(studentId);
			app.getGroupList().remove(group);
		} else {
			Group group = app.getTempGroup().get(captainId);
			List<Student> studentList = group.getStudents();
			Iterator<Student> it = studentList.iterator();
			while (it.hasNext()) {
				Student student=it.next();
				if (student.getId() == studentId){
					studentList.remove(student);
				}
					
			}
		}
		result.put("studentId", studentId);
		result.put("code", 0);
		result.put("data", Application.getInstance().getGroupList());
		logger.info("回复删除小组消息：" + result);
		sendResponse(result.toString());
	}

	private void sendResponse(String json) {
		MessagePacking messagePacking = new MessagePacking(
				Message.MESSAGE_GROUP_DELETE);
		messagePacking.putBodyData(DataType.INT,
				BufferUtils.writeUTFString(json));
		byte[] messageData = messagePacking.pack().array();
		CoreSocket.getInstance().sendMessage(messageData);
	}
}
