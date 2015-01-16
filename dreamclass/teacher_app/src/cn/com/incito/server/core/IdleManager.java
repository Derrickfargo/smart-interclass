package cn.com.incito.server.core;

import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.JSONUtils;

public class IdleManager {

	private static IdleManager instance;
	private Application app = Application.getInstance();
	private Logger log = Logger.getLogger(IdleManager.class.getName());
	
	public static IdleManager getInstance() {
		if (instance == null) {
			instance = new IdleManager();
		}
		return instance;
	}

	public synchronized void close(String imei) {
		ChannelHandlerContext ctx = app.getClientChannel().get(imei);
		if (ctx != null) {
			if(ctx.channel()==null){
				log.info("通道已經關閉："+imei);
				return;
			}
			if (ctx.channel().isActive()) {
				ctx.close();
				log.info("检测到设备心跳超时或异常退出  ： " + imei);
			}
		} else {
			log.info("關閉通道失敗，context為空");
			return;
		}
		doLogout(imei, ctx);
	}

	private void doLogout(String imei, ChannelHandlerContext channel) {
		Device device = app.getImeiDevice().get(imei);
		if (device == null) {
			return;
		}
		Table table = app.getDeviceTable().get(device.getId());
		if (table == null) {
			return;
		}
		Group group = app.getTableGroup().get(table.getId());
		List<Student> students = app.getStudentByImei(imei);
		if (students != null) {
			for (Student student : students) {
				for (Student aStudent : group.getStudents()) {
					if (student.getName().equals(aStudent.getName())
							&& student.getNumber().equals(aStudent.getNumber())) {
						aStudent.setLogin(false);
					}
				}
			}
		}
		app.holdDroppedStus(imei);//保存当前设备在线的学生
		app.removeLoginStudent(imei);
		app.getOnlineDevice().remove(imei);
		app.getClientChannel().remove(imei);
		app.removeGroupChannel(group.getId(), channel);
		app.refresh();// 更新UI

		// 向其他设备发送退出通知
		List<ChannelHandlerContext> channels = Application.getInstance().getClientChannelByGroup(group.getId());
		JSONObject json = new JSONObject();
		json.put("code", JSONUtils.SUCCESS);
		json.put("data", group);
		sendResponse(json.toJSONString(), channels, channel);
	}

	private void sendResponse(String json, List<ChannelHandlerContext> channels, ChannelHandlerContext cn) {
		for (ChannelHandlerContext channel : channels) {
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
			messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
			if(channel!=null){
				if (channel.channel().isActive()) {
					SocketServiceCore.getInstance().sendMsg(messagePacking, channel);
				}				
			}
		}
	}
}
