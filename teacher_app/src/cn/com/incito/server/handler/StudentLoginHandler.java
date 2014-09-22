package cn.com.incito.server.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.JSONUtils;

/**
 * 登陆消息处理器
 * 
 * @author 刘世平
 * 
 */
public class StudentLoginHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(StudentLoginHandler.class.getName());
	@Override
	public void handleMessage() {
        final String uname = data.getString("name");
        int sex = data.getIntValue("sex");
        final String number = data.getString("number");
        final String imei = data.getString("imei");
        int type = data.getIntValue("type");//0登陆,1取消登陆,2注册
        switch (type){
        case 0:
        	logger.info("消息类型为学生登陆:" + data);
        	String result = service.login(uname, sex, number, imei);
        	Integer groupId = getGroupId(result);
			if (groupId != -1) {
				Application.getInstance().addSocketChannel(groupId, message.getChannel());
        		sendResponse(result,Application.getInstance().getClientChannelByGroup(groupId));
        		logger.info(result);
        	}
        	break;
        case 1:
        	logger.info("消息类型为学生退出:" + data);
        	result = service.logout(uname, sex, number, imei);
        	groupId = getGroupId(result);
			if (groupId != -1) {
				Application.getInstance().addSocketChannel(groupId, message.getChannel());
        		sendResponse(result,Application.getInstance().getClientChannelByGroup(groupId));
        		logger.info(result);
        	}
    		break;
        case 2:
        	logger.info("消息类型为注册学生:" + data);
        	result = service.register(uname, sex, number, imei);
        	groupId = getGroupId(result);
			if (groupId != -1) {
        		//检查当前注册的学生是否之前在别的小组，存在则从之前的组中剔除
				sendOtherPadLogout(uname, number, groupId);
				Application.getInstance().addSocketChannel(groupId, message.getChannel());
        		sendResponse(result,Application.getInstance().getClientChannelByGroup(groupId));
        		logger.info(result);
        	}
        	break;
        }
	}
	
	/**
	 * 将其他组的相同人员剔除
	 * @param uname
	 * @param number
	 * @param groupId 新组id
	 */
	private void sendOtherPadLogout(String uname, String number, int groupId) {
		Application app = Application.getInstance();
		List<Group> groupList = app.getGroupList();
		if (groupList == null || groupList.size() == 0) {
			return;
		}
		Group group = findGroup(groupList, uname, number, groupId);
		if(group != null){
			Iterator<Student> it = group.getStudents().iterator();
			while (it.hasNext()) {
				Student temp = it.next();
				if ((temp.getName() + temp.getNumber()).equals(uname + number)) {
					it.remove();
					break;
				}
			}
			app.addGroup(group);
			app.getTableGroup().put(group.getTableId(), group);
			app.refresh();// 更新UI
			String result = JSONUtils.renderJSONString(0, group);//更新消息发往pad端
			sendResponse(result,Application.getInstance().getClientChannelByGroup(group.getId()));
		}
	}
	
	private Group findGroup(List<Group> groupList, String uname, String number, int groupId){
		for (Group group : groupList) {
			if (group.getId() == groupId) {
				continue;
			}
			List<Student> students = group.getStudents();
			if (students == null || students.size() == 0) {
				continue;
			}
			for (Student temp : students) {
				if ((temp.getName() + temp.getNumber()).equals(uname + number)) {
					return group;
				}
			}
		}
		return null;
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
	
	private Integer getGroupId(String json){
		JSONObject jsonObject = JSON.parseObject(json);
		if(jsonObject.getIntValue("code") != 0){
			return -1;
		}
		String data = jsonObject.getString("data");
		JSONObject dataObject = JSON.parseObject(data);
		return dataObject.getInteger("id");
	}
}
