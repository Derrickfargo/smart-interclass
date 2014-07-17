//package cn.com.incito.server.message;
//
//import java.nio.ByteBuffer;
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.com.incito.server.core.Message;
//import cn.com.incito.server.utils.BufferUtils;
//
///**
// * 消息解包工具
// * 
// * @author 刘世平
// * 
// */
//public class MessageUnpacking {
//
//
//	/**
//	 * 消息识别码
//	 */
//	public static final int MESSAGE_FAKE_ID = 0xFAFB;
//
//	private Message message;
//	private ByteBuffer bodyBuffer;
//
//	public MessageUnpacking(Message message) {
//		this.message = message;
//		this.bodyBuffer = message.getBodyBuffer();
//	}
//
//	public byte getMessageId() {
//		return message.getMsgID();
//	}
//
//	public byte getByteData(DataType key) {
//		int size = getDataLength(key);
//		byte[] data = new byte[size];
//		ByteBuffer buffer = BufferUtils.prepareToReadOrPut(size);
//		buffer.get(data);
//	}
//
//	private int getDataLength(DataType key) {
//		int size = 0;
//		switch (key) {
//		case BYTE:
//			byte[] bytes = new byte[1];
//			bodyBuffer.get(bytes);
//			size = Integer.parseInt(BufferUtils.decodeIntLittleEndian(bytes, 0, 1) + "");
//			break;
//		case SHORT:
//			byte[] shortBytes = new byte[2];
//			size = Integer.parseInt(BufferUtils.decodeIntLittleEndian(shortBytes, 0, 2) + "");
//			break;
//		case INT:
//			byte[] intBytes = new byte[4];
//			size = Integer.parseInt(BufferUtils.decodeIntLittleEndian(intBytes, 0, 4) + "");
//			break;
//		case LONG:
//			byte[] longBytes = new byte[8];
//			size = Integer.parseInt(BufferUtils.decodeIntLittleEndian(longBytes, 0, 8) + "");
//			break;
//		}
//		return size;
//	}
//	
//	/**
//	 * @param key
//	 *            值的长度的数据类型
//	 * @param type
//	 *            返回值类型
//	 * @return	key对应的值
//	 */
//	public <T> T getBodyData(DataType key, Class<T> type) {
//		switch (key) {
//		case BYTE:
//			byte[] bytes = new byte[1];
//			bodyBuffer.get(bytes);
//			T t = type.newInstance();
//			t = (T)BufferUtils.decodeIntLittleEndian(bytes, 0, 1);
//			break;
//		case SHORT:
//			byte[] shortBytes = new byte[2];
//			BufferUtils.encodeIntLittleEndian(shortBytes, value.length, 0, 2);
//			break;
//		case INT:
//			byte[] intBytes = new byte[4];
//			BufferUtils.encodeIntLittleEndian(intBytes, value.length, 0, 4);
//			break;
//		case LONG:
//			byte[] longBytes = new byte[8];
//			BufferUtils.encodeIntLittleEndian(longBytes, value.length, 0, 8);
//			break;
//		}
//		return null;
//	}
//
//}
