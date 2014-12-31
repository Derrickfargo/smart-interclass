package cn.com.incito.server.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;

public class HeartbeatManager extends Thread {
	private final static long ACT_CYCLE = 60 * 1000;// 产生心跳周期
	private static HeartbeatManager instance = null;
	private final static Logger log = Logger.getLogger(HeartbeatManager.class);

	private volatile boolean isRunning = true;

	public static HeartbeatManager getInstance() {
		if (instance == null) {
			instance = new HeartbeatManager();
		}
		return instance;
	}
	
	public void run() {
		while (isRunning) {
			try {
				Thread.sleep(ACT_CYCLE);
				sendHeartbeat();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void sendHeartbeat() throws IOException {
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HEART_BEAT);
		JSONObject json = new JSONObject();
		messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json.toString()));
		final byte[] data = messagePacking.pack().array();
		Application app = Application.getInstance();
		Map<String, SocketChannel> channels = app.getClientChannel();
		if (channels.size() == 0) {
			return;
		}
		// 发送心跳包
		log.info("HeartbeatManager::发送心跳包!");
		Set<Entry<String, SocketChannel>> entrys = channels.entrySet();
		final Iterator<Entry<String, SocketChannel>> it = entrys.iterator();
		new Thread() {
			@Override
			public void run() {
				ByteBuffer buffer = ByteBuffer.allocate(data.length);
				while (it.hasNext()) {
					Entry<String, SocketChannel> entry = it.next();
					// 输出到通道
					buffer.clear();
					buffer.put(data);
					buffer.flip();
					try {
						entry.getValue().write(buffer);
					} catch (IOException e) {
						doLogout(entry.getKey());
					}
				}
			};
		}.start();
	}

	/**
	 * 
	 * @param imei
	 */
	private void doLogout(String imei) {
		Application app = Application.getInstance();
		app.getClientChannel().remove(imei);
		Student studnet = app.getStudentByImei(imei);
		studnet.setLogin(false);
		app.getOfflineStudent().add(studnet);
		app.getOnlineStudent().remove(studnet);
		app.refresh();// 更新UI
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
