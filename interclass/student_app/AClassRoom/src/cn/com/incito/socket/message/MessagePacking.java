package cn.com.incito.socket.message;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.utils.BufferUtils;

/**
 * 消息包装器
 * Created by liushiping on 2014/7/28.
 *
 */
public class MessagePacking {

	/**
	 * 消息头的长度
	 */
	private static final int HEADER_LENGTH = 7;
	
	/**
	 * 消息识别码
	 */
	public static final int MESSAGE_FAKE_ID = 0xFAFB;

	public byte msgId;
	private int bodySize = 0;
	private List<byte[]> bodyDataList = new ArrayList<byte[]>();

	public MessagePacking(byte msgId) {
		this.msgId = msgId;
	}
	
	/**
	 * @param key 值的长度的数据类型
	 * @param value 值
	 */
	public void putBodyData(DataType key, byte[] value) {
		switch (key) {
		case BYTE:
			byte[] bytes = new byte[1];
			BufferUtils.encodeIntLittleEndian(bytes, value.length, 0, 1);
			bodyDataList.add(bytes);
			bodySize += 1;
			break;
		case SHORT:
			byte[] shortBytes = new byte[2];
			BufferUtils.encodeIntLittleEndian(shortBytes, value.length, 0, 2);
			bodyDataList.add(shortBytes);
			bodySize += 2;
			break;
		case INT:
			byte[] intBytes = new byte[4];
			BufferUtils.encodeIntLittleEndian(intBytes, value.length, 0, 4);
			bodyDataList.add(intBytes);
			bodySize += 4;
			break;
		case LONG:
			byte[] longBytes = new byte[8];
			BufferUtils.encodeIntLittleEndian(longBytes, value.length, 0, 8);
			bodyDataList.add(longBytes);
			bodySize += 8;
			break;
		}
		bodyDataList.add(value);
		bodySize += value.length;
	}

	public ByteBuffer pack() {
		ByteBuffer messageBuffer = BufferUtils.prepareToReadOrPut(HEADER_LENGTH + bodySize);
		
		//打包消息头:由fakeId,msgId,bodySize组成,共7个字节
		//fake id
        byte[] indentifierByte = new byte[2];
        BufferUtils.encodeIntLittleEndian(indentifierByte, Message.MESSAGE_FAKE_ID, 0, 2);//short
        messageBuffer.put(indentifierByte);
        //消息ID
        messageBuffer.put(msgId);//byte
        //消息body的长度
        byte[] allLengthByte = new byte[4];//int
        BufferUtils.encodeIntLittleEndian(allLengthByte, bodySize, 0, 4);
        messageBuffer.put(allLengthByte);
        
		//打包消息体
		for (byte[] data : bodyDataList) {
			messageBuffer.put(data);
		}
		return messageBuffer;
	}
}
