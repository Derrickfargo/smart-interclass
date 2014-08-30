package cn.com.incito.server.core;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import cn.com.incito.server.api.Application;

/**
 * 单个客户端连接，主要管理心跳
 * 
 * @author 刘世平
 * 
 */
public class Connection {
	private final static long TIMEOUT = 30000;//超时时间
	private final static long SCAN_CYCLE = 10000;//心跳扫描周期10s
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
		Application app = Application.getInstance();
		SocketChannel sc = app.getClientChannel().remove(imei);
		// TODO 测试代码
		if (sc != channel) {
			log.debug("关闭连接");
			if (sc != null && sc.isConnected()) {
				try {
					sc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (channel != null && channel.isConnected()) {
			try {
				channel.close();
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
