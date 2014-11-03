package cn.com.incito.socket.core;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 通用消息类
 *
 * @author 刘世平
 */
public class Message implements Serializable {

	/**
	 * 消息识别码
	 */
	public static final int MESSAGE_FAKE_ID = 0xFAFB;

	/**
	 * 握手消息
	 */
	public static final Byte MESSAGE_HAND_SHAKE = (byte) 0xFF;

	/**
	 * 心跳消息
	 */
	public static final Byte MESSAGE_HEART_BEAT = (byte) 0xFE;

	/**
	 * 获取分组信息
	 */
	public static final Byte MESSAGE_GROUP_LIST = 0x01;
	/**
	 * 选择wifi登陆
	 */
	public static final Byte MESSAGE_STUDENT_LOGIN = 0x02;

	/**
	 * 删除小组
	 */
	public static final Byte MESSAGE_GROUP_DELETE = 0x03;
	/**
	 * 判断设备是否绑定
	 */
	public static final Byte MESSAGE_DEVICE_HAS_BIND = 0x04;

	/**
	 * 分组信息(pc-android)
	 */
	public static final Byte MESSAGE_GROUP_EDIT = 0x05;

	/**
	 * 创建小组(android-pc)
	 */
	public static final Byte MESSAGE_GROUP_CREAT = 0x06;

	/**
	 * 加入小组(android-pc)
	 */
	public static final Byte MESSAGE_GROUP_JOIN = 0x07;
	/**
	 * 发送作业(android-pc)
	 */
	public static final Byte MESSAGE_DISTRIBUTE_PAPER = 0x08;
	/**
	 * 保存作业(android-pc)
	 */
	public static final Byte MESSAGE_SAVE_PAPER = 0x09;

	/**
	 * 保存作业后PC端传回的信息
	 */
	public static final Byte MESSAGE_SAVE_PAPER_RESULT = 0x0A;

	/**
	 * 设备退出
	 */
	public static final Byte MESSAGE_DEVICE_LOGOUT = 0x0B;
	/**
	 * 锁屏
	 */
	public static final Byte MESSAGE_LOCK_SCREEN = 0x0C;

	/**
	 * 学生登录后的返回(PC-PAD)
	 */
	public static final Byte MESSAGE_STUDENT_BIND=0x0D;
	
	private byte msgID;
	private int msgSize;
	private ByteBuffer bodyBuffer;
	private MessageHandler handler;
	private SocketChannel channel;

	public byte getMsgID() {
		return msgID;
	}

	public void setMsgID(byte msgId) {
		this.msgID = msgId;
		handler = MessageHandlerResource.getHandlerResources()
				.getMessageHandler(getMsgID());
	}

	public void setHandler(MessageHandler messageHandler) {
		handler = messageHandler;
	}

	public int getMsgSize() {
		return msgSize;
	}

	public void setMsgSize(int msgSize) {
		this.msgSize = msgSize;
	}

	public ByteBuffer getBodyBuffer() {
		return bodyBuffer;
	}

	public void setBodyBuffer(ByteBuffer bodyBuffer) {
		this.bodyBuffer = bodyBuffer;
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}

	public void executeMessage() {
		handler.handleMessage(this);
	}
}
