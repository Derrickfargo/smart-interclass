package cn.com.incito.socket.core;

import cn.com.incito.socket.message.MessagePacking;


/**
 * 通讯核心
 * 主要是连接,断开连接
 * @author hm
 *
 */
public interface ICoreSocket {

	/**
	 * 开始进行服务器连接
	 * @param ip  服务器ip地址
	 * @param port  服务器端口号
	 */
	public void startConnection(final String ip,int port) throws InterruptedException;
	
	
	/**
	 * 重连
	 * @param ip
	 * @param port
	 */
	public void connection();
	
	
	/**
	 * 断开与服务器的连接
	 * 用于在客户端断网时自动断开连接
	 */
	public void stopConnection();
	
	
	
	/**
	 * 发送消息
	 * @param nettyMessage
	 */
	public void sendMessage(MessagePacking messagePacking);
	
	
}
