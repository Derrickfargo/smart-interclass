package cn.com.incito.server.handler;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.core.CoreService;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.core.SocketServiceCore;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

/**
 * 获取分组消息处理器
 * 
 * @author 刘世平
 * 
 */
public class GroupListHandler extends MessageHandler {
	private Logger logger = Logger.getLogger(GroupListHandler.class.getName());
	private Application app = Application.getInstance();
	
	@Override
	public void handleMessage() {
		logger.info("收到获取分组消息:" + data);
		
		String imei = data.getString("imei");
		logger.info("IMEI:" + imei);
		//需要给组中所以的设备发送
		String result = service.getGroupByIMEI(imei);
		List<Student> students = app.getRecStudents().get(imei);
		if (students != null) {
			for (Student stu : students) {
				app.addLoginStudent(imei, stu);// 學生從新登陸
				app.getOnlineStudent().add(stu);
			}
			app.refresh();//刷新界面
		}
		app.getRecStudents().remove(imei);
		logger.info(result);
		sendResponse(result,imei);
	}

	private void sendResponse(String json,String imei) {
		logger.info("回复获取分组消息:" + json);
		JSONObject result = JSON.parseObject(json);
		Group group = result.getObject("data", Group.class);
		List<ChannelHandlerContext> channelList = app.getGroupChannel().get(group.getId());
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_LIST);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
        SocketServiceCore.getInstance().sendMsg(messagePacking, ctx);
        for(ChannelHandlerContext channel:channelList){
			if(channel!=null){
				if(channel.channel().isActive()){
					SocketServiceCore.getInstance().sendMsg(messagePacking, channel);								
				}
			}
		}
	}
}
