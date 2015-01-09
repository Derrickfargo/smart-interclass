package cn.com.incito.server.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import cn.com.incito.server.api.ApiClient;
import cn.com.incito.server.utils.BufferUtils;

/**
 * 消息解析器
 * @author 刘世平
 *
 */
public class MessageParser {
	private Logger logger = Logger.getLogger(MessageParser.class.getName());
	/**
	 * fake id长度为两个字节
	 */
	private static final int FAKE_ID_LENGTH = 2;

	/**
	 * 消息头的长度
	 */
	private static final int HEADER_LENGTH = 7;
	
	private Message message;
	private SocketChannel channel = null;
	
	
	/**
	 * 得到read事件，建立与pc的socket通讯连接并进行处理
	 * 
	 * @param key
	 *            注册了acceptable的相应通道的key，可由此key得到通道
	 */
//	public void parseMessage(SelectionKey key) {
//		// 创建一个headerBuffer用来接收消息头前6个字节
//		ByteBuffer headerBuffer = BufferUtils.prepareToReadOrPut(HEADER_LENGTH);
//		channel = (SocketChannel) key.channel();
//		try {
//			// 从通道中读取消息头,如果该通道已到达流的末尾，则返回 -1
//			if (channel.read(headerBuffer) == -1) {
//				// 关闭通道
//				channel.close();
//				return;
//			}
//		} catch (IOException e) {
//			logger.fatal("解析消息失败:", e);
//			ApiClient.uploadErrorLog(e.toString());
//			try {
//				channel.close();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			return;
//		}
//		headerBuffer.flip();
//		logger.info("开始解析消息..");
//		// 消息头fakeId是否正确
//		if (!parseFakeId(headerBuffer)) {
//			return;
//		}
//		// 获取消息头中有用的信息,msgId,msgSize
//		parseMessageHeader(headerBuffer);
//		// 解析消息体和命令
//		if (!parseMessageBody()) {
//			return;
//		}
//		message.setChannel(channel);
//		if(message.getMsgID() == Message.MESSAGE_HEART_BEAT){
//			//如果为心跳消息，优先级最高，不经过队列，直接处理
//			message.handleMessage();
//			return;
//		}
//		// 把消息压入消息队列
//		logger.info("消息解析成功，开始压入消息队列，MSG_ID=" + message.getMsgID());
//		MessageManager.getInstance().addQueue(message);
//	}

	
	/**
	 * 解析消息头的Fake Id
	 * 
	 * @param buffer 消息头（6个字节）
	 * @return boolean 解析消息的fakeId是否为0xFAFB
	 */
	private boolean parseFakeId(ByteBuffer buffer) {
		// 创建一个Byte数组来接收fake id的内容
		byte[] fakeIdByte = new byte[FAKE_ID_LENGTH];
		// 报文总长度
		int fakeId = 0;
		try {
			// 读取ByteBuffer的内容到fakeIdByte
			buffer.get(fakeIdByte);
			// 获取fake id的值
			fakeId = Integer.parseInt(BufferUtils.decodeIntLittleEndian(fakeIdByte, 0, fakeIdByte.length) + "");
		} catch (Exception e) {
			logger.error("解析fake Id出错:", e);
			return false;
		}

		// 如果消息的fakeId与定义的fakeId值不符，则丢弃掉该条消息
		if (Message.MESSAGE_FAKE_ID != fakeId) {
			String reason = "该消息头不是需要的消息头,fakeId:" + fakeId;
			logger.error(reason);
			ApiClient.uploadErrorLog(reason);
			return false;
		}
		return true;
	}

	/**
	 * 解析消息头得到extSize和msgSize
	 * 
	 * @param buffer
	 *            消息体字节数组
	 */
	private void parseMessageHeader(ByteBuffer buffer) {
		message = new Message();
		//获取消息id（1个byte）
		message.setMsgID(buffer.get());
        //获取消息体的长度（4个byte）
		message.setMsgSize(buffer.getInt());
	}

	/**
	 * 解析消息体
	 * 
	 * @param buffer
	 *            消息体内容
	 * @return true，解析成功，false表示为其他类型包无需压入消息队列
	 */
	private boolean parseMessageBody() {
		logger.info("开始解析消息体... ...");
		int bodySize = message.getMsgSize();
		ByteBuffer bodyBuffer = BufferUtils.prepareToReadOrPut(bodySize);
		try {
			while (bodyBuffer.position() < bodyBuffer.capacity()){
				channel.read(bodyBuffer);
			}
			message.setBodyBuffer(bodyBuffer);
			return true;
		} catch (Exception e) {
			logger.error("获取消息体失败:", e);
			ApiClient.uploadErrorLog(e.toString());
			return false;
		}
	}

}
