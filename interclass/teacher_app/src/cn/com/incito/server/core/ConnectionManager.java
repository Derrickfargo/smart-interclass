package cn.com.incito.server.core;

import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 连接管理器，主要管理客户端连接，心跳机制
 * @author 刘世平
 *
 */
public class ConnectionManager {

	/**
	 * 存放产生的长连接
	 */
	private static Map<String, Connection> pool = Collections
			.synchronizedMap(new HashMap<String, Connection>());

	public static void notification(String imei, SocketChannel channel){
		Connection connection = pool.get(imei);
		if (connection == null) {//第一次连接
			pool.put(imei, new Connection(imei, channel));
			return;
		}
		connection.heartbeat();
	}
	
	public static void stopMonitor(String imei) {
		Connection connection = removeConnection(imei);
		if (connection != null) {
			connection.close();
		}
	}
	
	static Connection removeConnection(String imei){
		return pool.remove(imei);
	}

}
