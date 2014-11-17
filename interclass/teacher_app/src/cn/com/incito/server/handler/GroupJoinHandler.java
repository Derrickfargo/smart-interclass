package cn.com.incito.server.handler;

import java.util.ArrayList;
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

public class GroupJoinHandler extends MessageHandler{
	private Logger logger = Logger.getLogger(GroupJoinHandler.class.getName());
	@Override
	protected void handleMessage() {
		logger.info("收到加入小组消息：" + data);
		int groupId =data.getIntValue("groupId") ;
		Student student=data.getObject("student", Student.class);
		JSONObject result = new JSONObject();
		Group group=Application.getInstance().getTempGroup().get(groupId);
		if(group!=null){
			if(group.getStudents().size()<=4){
				group.getStudents().add(student);
				result.put("code", 0);
			}else{
				result.put("code", 1);
			}
		}else{
			result.put("code", 1);
		}
		List<Group> groupList=new ArrayList<Group>();
		//遍历临时分组 将还没有分组的小组列表传回给pad端
		for (Integer key : Application.getInstance().getTempGroup().keySet()) {
			groupList.add(Application.getInstance().getTempGroup().get(key));
		}
		result.put("data",groupList);
		logger.info("回复加入小组消息：" + result);
		sendResponse(result.toString());
	}
	private void sendResponse(String json) {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_JOIN);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
        byte[] messageData = messagePacking.pack().array();
        CoreSocket.getInstance().sendMessage(messageData);
	}

}
