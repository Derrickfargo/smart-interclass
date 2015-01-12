package cn.com.incito.socket.message;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.MessageHandlerResource;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

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
	
	private JSONObject jsonObject;

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
		this.jsonObject = JSONObject.parseObject(BufferUtils.readUTFString(value));
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
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
	
	public MessageHandler getHandler(){
		return MessageHandlerResource.getHandlerResources().getMessageHandler(msgId);
	}
}
