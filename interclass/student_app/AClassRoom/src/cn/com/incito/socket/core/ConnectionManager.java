package cn.com.incito.socket.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.utils.ApiClient;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 单个客户端连接，主要管理心跳
 * 
 * @author 刘世平
 * 
 */
public class ConnectionManager {
	private final static long TIMEOUT = 30000;// 超时时间
	private final static long SCAN_CYCLE = 20000;// 心跳扫描周期20s
	private static ConnectionManager instance;
	private SocketChannel channel;
	private long lastActTime = 0;
	private ConnectActiveMonitor monitor;// 用于定时扫描服务端心跳
	private HeartbeatGenerator generator;// 用于产生心跳

	static ConnectionManager getInstance() {
		if (instance == null) {
			instance = new ConnectionManager(null);
		}
		return instance;
	}

	public static ConnectionManager getInstance(SocketChannel channel) {
		if (instance == null) {
			instance = new ConnectionManager(channel);
		} else if (instance.getChannel() == null
				|| !instance.getChannel().isOpen()) {
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
	public void heartbeat() {
		lastActTime = System.currentTimeMillis();
	}

	/**
	 * 网络退出
	 * 
	 * @param isNormal
	 *            是否正常退出
	 */
	public void close(boolean isNormal) {
		// 停止检测心跳
		monitor.setRunning(false);
		generator.setRunning(false);
		CoreSocket.getInstance().disconnect();
		if (channel != null && channel.isConnected()) {
			try {
				channel.close();
			} catch (IOException e) {
				MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+":"+e.getMessage());
				e.printStackTrace();
			}
		}
		// 非正常退出,重连
		if (!isNormal) {
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":ConnectionManager:Socket非正常退出!");
			MyApplication.getInstance().lockScreen(false);
			restartConnector();
		}
	}

	public SocketChannel getChannel() {
		return channel;
	}

	/**
	 * 心跳监控器
	 * 
	 * @author 刘世平
	 * 
	 */
	class ConnectActiveMonitor extends Thread {
		private volatile boolean isRunning = true;

		public void run() {
			while (isRunning) {
				try {
					Thread.sleep(SCAN_CYCLE);
				} catch (InterruptedException e) {
					MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+":"+e.getMessage());
					e.printStackTrace();
				}
				long time = System.currentTimeMillis();
				if (time - lastActTime > TIMEOUT) {
					close(Boolean.FALSE);// 非正常退出
					break;
				}
			}
		}

		public void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}
	}

	/**
	 * 心跳发生器
	 * 
	 * @author 刘世平
	 * 
	 */
	class HeartbeatGenerator extends Thread {
		private volatile boolean isRunning = true;

		public void run() {
			while (isRunning) {
				try {
					Thread.sleep(SCAN_CYCLE);
					// 发送心跳包
					MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "ConnectionManager::发送心跳包!");
					sendHeartbeat();
				} catch (Exception e) {
					MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "ConnectionManager::心跳异常:" + e.getMessage());
					ApiClient.uploadErrorLog(e.getMessage());
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

	/**
	 * 重新建立连接
	 */
	public void restartConnector() {
		new Thread() {
			public void run() {
				while (Boolean.TRUE) {
					MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "ConnectionManager::Socket开始重连!");
					CoreSocket.getInstance().restartConnection();
					sleep(5000);// 等待1秒后检查连接
					if (!CoreSocket.getInstance().isConnected()) {
						MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "ConnectionManager::Socket重连失败!");
						CoreSocket.getInstance().disconnect();
						sleep(5000);
						continue;
					}
					MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "ConnectionManager::Socket已重新连接!");
					break;
				}
			}
			private void sleep(int seconds) {
				try {
					Thread.sleep(seconds);
				} catch (InterruptedException e) {
					MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "ConnectionManager::线程中断异常" + e.getMessage());
					e.printStackTrace();
				}
			}
		}.start();
	}
}
