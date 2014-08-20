package cn.com.incito.server.core;

import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

	public static void addConnection(String imei, SocketChannel channel){
		Connection connection = pool.get(imei);
		if (connection == null) {
			
		}
		pool.put(imei, new Connection(imei, channel));
	}

	public static void removeConnection(Connection connection) {
		Set<Entry<String, Connection>> set = pool.entrySet();
		Iterator<Entry<String, Connection>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, Connection> entry = it.next();
//			String key = entry.getKey();
			if (entry.getValue().equals(connection)) {
				it.remove();
			}
		}
	}
	
	public static void removeConnection(String imei) {
		pool.remove(imei);
	}

}
