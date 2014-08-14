package cn.com.incito.server.core;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ConnectionManager {
	/**
	 * 心跳周期（单位：毫秒）
	 */
	private volatile static long activeCycle = 10000;

	/**
	 * 存放产生的长连接
	 */
	private static Set<Connection> pool = Collections
			.synchronizedSet(new HashSet<Connection>());

	/**
	 * 用于定时发送心跳包
	 */
	private static ConnectActiveMonitor monitor = new ConnectActiveMonitor();

	static {
		monitor.start();
	}

	public static Connection createConnection(SocketChannel channel)
			throws IOException {
		Connection conn = new Connection(channel);
		pool.add(conn);
		return conn;
	}

	public static void removeConnection(Connection conn) {
		pool.remove(conn);
	}

	static class ConnectActiveMonitor extends Thread {
		private volatile boolean running = true;

		public void run() {
			while (running) {
				long time = System.currentTimeMillis();
				for (Connection con : pool) {
					if (con.getLastActTime() + activeCycle < time) {
						// TODO
					}
				}
				yield();
			}
		}

		void setRunning(boolean b) {
			running = b;
		}
	}
}
