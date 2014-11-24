package cn.com.incito.server.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.apache.log4j.Logger;

/**
 * 服务端组播Socket不接收数据，只发送数据
 * @author 刘世平
 *
 */
public final class MultiCastSocket {
	static boolean flag = true;
	private static final int BAND_PORT = 9000;//组播端口
	private static MultiCastSocket instance = null;
	private InetAddress group = null; // 组播组地址.
	private MulticastSocket socket = null; // 组播套接字.
	private Logger logger = Logger.getLogger(MultiCastSocket.class.getName());
	
	private MultiCastSocket(){
		try {
			group = InetAddress.getByName("224.0.0.251"); // 设置组播地址为224.0.0.251。
			socket = new MulticastSocket(BAND_PORT); // 组播套接字将在9000端口组播。
			socket.setTimeToLive(1); // 组播套接字发送数据报范围为本地网络。
			socket.joinGroup(group); // 加入组播组,加入group后,socket发送的数据报，可以被加入到group中的成员接收到。
		} catch (IOException e) {
			logger.error("创建组播Socket失败", e);
		}
	}
	
	public static MultiCastSocket getInstance() {
		if (instance == null) {
			instance = new MultiCastSocket();
		}
		return instance;
	}

	public void close() {
		socket.close();
	}

	
	/**
	 * 将组播消息发往客户端
	 * @param data
	 */
	public void sendMessage(final byte[] data){
		DatagramPacket packet = new DatagramPacket(data, data.length, group, BAND_PORT);
		try {
			socket.send(packet);// 发送组播数据包。
		} catch (IOException e) {
			logger.error("发送组播消息失败", e);
		} 
	}
	
}
