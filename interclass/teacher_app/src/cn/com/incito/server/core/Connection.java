package cn.com.incito.server.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.TempStudent;
import cn.com.incito.interclass.ui.MainFrame;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

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
		Application app = Application.getInstance();
		Map<Integer, Group> tempGroup = app.getTempGroup();
		//通过imei获得学生ID
		CoreService coreService=new CoreService();
		TempStudent student=coreService.getStudentIdByImei(imei);
		tempGroup.remove(student.getId());
		List<Group> groupList=new ArrayList<Group>();
		//遍历临时分组 将还没有分组的小组列表传回给pad端
		for (Integer key : Application.getInstance().getTempGroup().keySet()) {
			groupList.add(Application.getInstance().getTempGroup().get(key));
		}
		
		for (int i = 0; i < groupList.size(); i++) {
			Group group=groupList.get(i);
			List<Student> studentList=group.getStudents();
			Iterator<Student> it = studentList.iterator();
			while (it.hasNext()) {
				Student studentTemp=it.next();
				if (student.getId() == studentTemp.getId()){
					studentList.remove(studentTemp);
					break;
				}
			}
		}
		JSONObject result = new JSONObject();
		result.put("studentId", student.getId());
		result.put("code", 0);
		result.put("data", groupList);
		app.refresh();// 更新UI
		sendResponse(result.toString());
		Application.getInstance().getClientChannel().remove(imei);
		
	}
	private void sendResponse(String json) {
		MessagePacking messagePacking = new MessagePacking(
				Message.MESSAGE_GROUP_DELETE);
		messagePacking.putBodyData(DataType.INT,
				BufferUtils.writeUTFString(json));
		byte[] messageData = messagePacking.pack().array();
		CoreSocket.getInstance().sendMessage(messageData);
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
