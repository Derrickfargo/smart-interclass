package cn.com.incito.server.core;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import cn.com.incito.server.exception.NoHandlerException;

/**
 * 通用消息类
 * 
 * @author 刘世平
 * 
 */
public class Message {

	/**
	 * 消息识别码
	 */
	public static final int MESSAGE_FAKE_ID = 0xFAFB;

	/**
	 * 握手消息
	 */
	public static final Byte MESSAGE_HAND_SHAKE = (byte)0xFF;
	
	/**
	 * 心跳消息
	 */
	public static final Byte MESSAGE_HEART_BEAT = (byte) 0xFE;

	/**
	 * 获取分组信息
	 */
	public static final Byte MESSAGE_GROUP_LIST = 0x01;
	
	/**
	 * 登陆消息
	 */
	public static final Byte MESSAGE_STUDENT_LOGIN = 0x02;
	
	/**
	 * 设备绑定消息
	 */
	public static final Byte MESSAGE_DEVICE_BIND = 0x03;
	
	/**
	 * 判断设备是否绑定
	 */
	public static final Byte MESSAGE_DEVICE_HAS_BIND = 0x04;
	
	/**
	 * 编辑小组信息(pc-android)
	 */
	public static final Byte MESSAGE_GROUP_EDIT = 0x05;
	
	/**
	 * 确认小组信息(pc-android),编辑完成后点保存
	 */
	public static final Byte MESSAGE_GROUP_CONFIRM = 0x06;
	/**
	 * 保存小组信息(android-pc)
	 */
	public static final Byte MESSAGE_GROUP_VOTE = 0x07;
	/**
	 * 发送作业(android-pc)
	 */
	public static final Byte MESSAGE_DISTRIBUTE_PAPER = 0x08;
	/**
	 * 保存作业(android-pc)
	 */
	public static final Byte MESSAGE_SAVE_PAPER = 0x09;
	
	/**
	 * 保存作业后传给andorid端的保存信息
	 */
	public static final Byte MESSAGE_SAVE_PAPER_RESULT=0x0A;
	
	/**
	 * 提交作业申请
	 */
	public static final Byte MESSAGE_SEND_PAPER=0x0D;
	
	/**
	 * 设备退出
	 */
	public static final Byte MESSAGE_DEVICE_LOGOUT=0x0B;
	/**
	 * 锁屏
	 */
	public static final Byte MESSAGE_LOCK_SCREEN=0x0C;
	/**
	 * 抢答
	 */
	public static final Byte MESSAGE_STUDENT_RESPONDER=0x0E;
	/**
	 * 随机分组
	 */
	public static final Byte MESSAGE_GROUP_RDMGROUP=0x0F;
	
	/**
	 * 作业互评
	 */
	public static final Byte MESSAGE_QUIZ_FEEDBACK=0x10;
	
	private Logger log = Logger.getLogger(Message.class);
	
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

	public void handleMessage() throws NoHandlerException{
		if (handler == null) {
			log.error("执行命令出错：没有找到想应的Handler!");
			throw new NoHandlerException();
		}
		handler.handleMessage(this);
	}
}
