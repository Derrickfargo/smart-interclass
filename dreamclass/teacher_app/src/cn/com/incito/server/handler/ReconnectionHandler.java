package cn.com.incito.server.handler;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.core.SocketServiceCore;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;

public class ReconnectionHandler extends MessageHandler {
	private Application app = Application.getInstance();
	private Logger log = Logger.getLogger(ReconnectionHandler.class.getName());

	@Override
	protected void handleMessage() {
		String imei = data.getString("imei");
		log.info("收到設備重連命令："+imei);
		List<Student> students = app.getRecStudents().get(imei);
		if (students != null) {
			for (Student stu : students) {
				app.addLoginStudent(imei, stu);// 學生從新登陸
				app.getOnlineStudent().add(stu);
			}
			app.refresh();//刷新界面
		}
		Group group = app.getGroupOnline(imei);// 得到該設備包含在線學生的小組
		sendResponse(imei, group);
	}

	private void sendResponse(String imei, Group group) {
		List<ChannelHandlerContext> channelList = app.getGroupChannel().get(group.getId());
		MessagePacking msg = new MessagePacking(Message.MESSAGE_RECONNECT);
		JSONObject json = new JSONObject();
		json.put("data", group);
		json.put("code", 0);
		msg.putBodyData(DataType.INT, json.toJSONString().getBytes());
		for(ChannelHandlerContext channel:channelList){
			if(channel!=null){
				if(channel.channel().isActive()){
					SocketServiceCore.getInstance().sendMsg(msg, channel);								
				}
			}
		}
	}
	
	
}
