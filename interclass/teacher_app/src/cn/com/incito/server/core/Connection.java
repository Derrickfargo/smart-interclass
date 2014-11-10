package cn.com.incito.server.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.JSONUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 单个客户端连接，主要管理心跳
 * 
 * @author 刘世平
 * 
 */
public class Connection {
	private final static long TIMEOUT = 30000;//超时时间
	private final static long SCAN_CYCLE = 20000;//心跳扫描周期12s
	private final static Logger log = Logger.getLogger(Connection.class);
	private String imei;
	private SocketChannel channel;
	private long lastActTime = 0;
	private ConnectActiveMonitor monitor;//用于定时扫描心跳
	
	Connection(String imei, SocketChannel channel) {
		this.imei = imei;
		this.channel = channel;
		lastActTime = System.currentTimeMillis();
		monitor = new ConnectActiveMonitor();
		monitor.start();
	}

	/**
	 * 最后一次心跳时间
	 */
	public void heartbeat(){
		lastActTime = System.currentTimeMillis();
	}
	
	public synchronized void close() {
		//停止检测心跳
		monitor.setRunning(false);
		ConnectionManager.removeConnection(imei);
		Application.getInstance().getClientChannel().remove(imei);
		if (channel != null && channel.isConnected()) {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//刷新界面
		doLogout();
	}
	
	private void doLogout(){
//		Application app = Application.getInstance();
//		Device device = app.getImeiDevice().get(imei);
//		if (device == null) {
//			return;
//		}
		//TODO 
//		Group group = app.getTableGroup().get(table.getId());
//		List<Student> students = app.getStudentByImei(imei);
//		if (students != null) {
//			for (Student student : students) {
//				for (Student aStudent : group.getStudents()) {
//					if (student.getName().equals(aStudent.getName())
//							&& student.getNumber().equals(aStudent.getNumber())) {
//						aStudent.setLogin(false);
//					}
//				}
//			}
//		}
//		app.removeLoginStudent(imei);
//		app.getOnlineDevice().remove(imei);
//		Application.getInstance().getClientChannel().remove(imei);
//		app.refresh();// 更新UI
//		
//		//向其他设备发送退出通知
//		List<SocketChannel> channels = Application.getInstance().getClientChannelByGroup(group.getId());
//		JSONObject json = new JSONObject();
//		json.put("code", JSONUtils.SUCCESS);
//		json.put("data", group);
//		sendResponse(json.toJSONString(), channels);
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
				if (this.channel != channel && channel.isConnected()) { 
					channel.write(buffer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getImei() {
		return imei;
	}

	public SocketChannel getChannel() {
		return channel;
	}

	class ConnectActiveMonitor extends Thread {
		private volatile boolean isRunning = true;

		public void run() {
			while (isRunning) {
				try {
					Thread.sleep(SCAN_CYCLE);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				long time = System.currentTimeMillis();
				if (time - lastActTime > TIMEOUT) {
					log.info("30秒内没有检测到心跳，设备退出!");
					close();
					break;
				}
			}
		}

		public void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}
	}
}
