package cn.com.incito.server.core;

import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import io.netty.channel.Channel;
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
			if (ctx.channel().isActive()) {
				ctx.close();
				log.info("检测到设备心跳超时，设备退出  ： " + ctx.channel().remoteAddress());
				doLogout(imei, ctx.channel());
			}
		} else {
				log.info("设备正常退出，但是map中没有相关数据");
		}
		app.getClientChannel().remove(imei);
	}

	private void doLogout(String imei, Channel channel) {
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
		app.removeLoginStudent(imei);
		app.getOnlineDevice().remove(imei);
		Application.getInstance().getClientChannel().remove(imei);
		app.refresh();// 更新UI

		// 向其他设备发送退出通知
		List<ChannelHandlerContext> channels = Application.getInstance().getClientChannelByGroup(group.getId());
		JSONObject json = new JSONObject();
		json.put("code", JSONUtils.SUCCESS);
		json.put("data", group);
		sendResponse(json.toJSONString(), channels, channel);
	}

	private void sendResponse(String json, List<ChannelHandlerContext> channels, Channel cn) {
		for (ChannelHandlerContext channel : channels) {
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
			messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
			// byte[] messageData = messagePacking.pack().array();
			// ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
			// buffer.put(messageData);
			// buffer.flip();
			if (!channel.channel().equals(cn)) {
				if (channel.channel().isActive()) {
					SocketServiceCore.getInstance().sendMsg(messagePacking, channel);
				}
			}
		}
	}
}
