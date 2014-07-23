package cn.com.incito.server.core;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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
	 * 心跳消息
	 */
	public static final Byte MESSAGE_HEARTBEAT = (byte) 0xFF;

	/**
	 * 登陆消息
	 */
	public static final Byte MESSAGE_LOGIN = 0x01;

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

	public void executeMessage() {
		handler.handleMessage(this);
	}
}
