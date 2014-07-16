package cn.com.incito.socket.core;

import java.nio.ByteBuffer;

/**
 * 通用消息类
 * @author 刘世平
 *
 */
public class Message {

	/**
	 * 报文识别码
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

	/**
	 * 消息Id
	 */
	private byte msgID;
	/**
	 * 消息体长度
	 */
	private int msgSize;
	/**
	 * 消息体数据
	 */
	private ByteBuffer bodyBuffer;

	public byte getMsgID() {
		return msgID;
	}

	public void setMsgID(byte msgId) {
		this.msgID = msgId;
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

	public void executeMessage() {
		MessageHandler handler = MessageHandlerResource.getHandlerResources()
				.getMessageHandler(getMsgID());
		handler.handleMessage(this);
	}
}
