package cn.com.incito.server.message;

import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.incito.server.core.Message;
import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.utils.BufferUtils;

/**
 * 消息打包工具
 * @author 刘世平
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
	private JSONObject jsonObject;
	private MessageHandler messageHandler;

	public MessagePacking(byte msgId) {
		this.msgId = msgId;
	}
	
	public MessagePacking(){
		
	}
	
	/**
	 * @param key 值的长度的数据类型
	 * @param value 值
	 */
	public void putBodyData(DataType key, byte[] value) {
		String data = BufferUtils.readUTFString(value);
		this.setJsonObject(JSONObject.parseObject(data));
//		switch (key) {
//		case BYTE:
//			byte[] bytes = new byte[1];
//			BufferUtils.encodeIntLittleEndian(bytes, value.length, 0, 1);
//			bodyDataList.add(bytes);
//			bodySize += 1;
//			break;
//		case SHORT:
//			byte[] shortBytes = new byte[2];
//			BufferUtils.encodeIntLittleEndian(shortBytes, value.length, 0, 2);
//			bodyDataList.add(shortBytes);
//			bodySize += 2;
//			break;
//		case INT:
//			byte[] intBytes = new byte[4];
//			BufferUtils.encodeIntLittleEndian(intBytes, value.length, 0, 4);
//			bodyDataList.add(intBytes);
//			bodySize += 4;
//			break;
//		case LONG:
//			byte[] longBytes = new byte[8];
//			BufferUtils.encodeIntLittleEndian(longBytes, value.length, 0, 8);
//			bodyDataList.add(longBytes);
//			bodySize += 8;
//			break;
//		}
//		bodyDataList.add(value);
//		bodySize += value.length;
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

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public MessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

}
