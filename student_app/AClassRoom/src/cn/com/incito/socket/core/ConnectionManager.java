package cn.com.incito.socket.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

/**
 * 单个客户端连接，主要管理心跳
 * 
 * @author 刘世平
 * 
 */
public class ConnectionManager {
	private final static long TIMEOUT = 30000;//超时时间
	private final static long SCAN_CYCLE = 10000;//心跳扫描周期10s
	private static ConnectionManager instance;
	private SocketChannel channel;
	private long lastActTime = 0;
	private ConnectActiveMonitor monitor;//用于定时扫描服务端心跳
	private HeartbeatGenerator generator;//用于产生心跳
	
	public static ConnectionManager getInstance(SocketChannel channel) {
		if (instance == null) {
			instance = new ConnectionManager(channel);
		} else if (instance.getChannel() == null
				|| !instance.getChannel().isConnected()) {
			instance = new ConnectionManager(channel);
		}
		return instance;
	}
	
	private ConnectionManager(SocketChannel channel) {
		this.channel = channel;
		lastActTime = System.currentTimeMillis();
		monitor = new ConnectActiveMonitor();
		monitor.start();
		generator = new HeartbeatGenerator();
		generator.start();
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
		generator.setRunning(false);
		if (channel != null && channel.isConnected()) {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
					close();
					break;
				}
			}
		}

		public void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}
	}
	
	class HeartbeatGenerator extends Thread{
		private volatile boolean isRunning = true;
		public void run() {
			while (isRunning) {
				try {
					Thread.sleep(SCAN_CYCLE);
					//发送心跳包
					sendHeartbeat();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		private void sendHeartbeat() throws IOException {
			JSONObject json = new JSONObject();
			json.put("imei", MyApplication.deviceId);
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_HEART_BEAT);
	        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json.toJSONString()));
	        byte[] messageData = messagePacking.pack().array();
	        ByteBuffer buffer = ByteBuffer.allocate(messageData.length);
	        buffer.put(messageData);
	        buffer.flip();
			if (channel != null && channel.isConnected()) {
				channel.write(buffer);
			}
		}

		public void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}
	}
}
