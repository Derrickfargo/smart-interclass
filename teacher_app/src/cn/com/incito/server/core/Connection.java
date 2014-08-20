package cn.com.incito.server.core;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import cn.com.incito.server.api.Application;

/**
 * 单个客户端连接，主要管理心跳
 * 
 * @author 刘世平
 * 
 */
public class Connection {
	/**
	 * 心跳扫描周期（单位：毫秒）
	 */
	private volatile static long activeCycle = 30000;
	private String imei;
	private SocketChannel channel;
	private long lastActTime = 0;
	private ConnectActiveMonitor monitor;//用于定时扫描心跳
	
	Connection(String imei, SocketChannel channel) {
		this.imei = imei;
		this.channel = channel;
		monitor = new ConnectActiveMonitor();
		monitor.start();
	}

	public synchronized void close() throws IOException {
		lastActTime = System.currentTimeMillis();
		ConnectionManager.removeConnection(imei);
		if (channel != null && channel.isConnected()){
			channel.close();
		}
		//TODO remove cache...
		Application app = Application.getInstance();
		
	}

	public synchronized long getLastActTime() {
		return lastActTime;
	}
	
	class ConnectActiveMonitor extends Thread {
		private volatile boolean isRunning = true;

		public void run() {
			while (isRunning) {
				long time = System.currentTimeMillis();
				if (getLastActTime() + activeCycle < time) {
					
				}
			}
		}

		void setRunning(boolean isRunning) {
			this.isRunning = isRunning;
		}
	}
}
