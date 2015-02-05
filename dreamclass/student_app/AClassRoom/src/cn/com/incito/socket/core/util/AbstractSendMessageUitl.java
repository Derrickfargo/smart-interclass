package cn.com.incito.socket.core.util;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.socket.core.NCoreSocket;
import cn.com.incito.socket.core.callback.DefaultSendMessageCallback;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

public abstract class AbstractSendMessageUitl {

	protected static NCoreSocket mCoreSocket;
	static{
		mCoreSocket = NCoreSocket.getInstance();
	}
	
	/**
	 * 发送的消息中只携带 设备imei的消息包装类
	 * @param msgId
	 * @return
	 */
	protected static void sendMessagePackingWithImei(byte msgId){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.getInstance().getDeviceId());
		MessagePacking messagePacking = new MessagePacking(msgId);
		messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
		mCoreSocket.sendMessage(messagePacking,new DefaultSendMessageCallback());
	}
	
	/**
	 * 包含其他信息的消息包装类
	 * @param msgId
	 * @param json
	 * @return
	 */
	protected static void sendMessagePackingWithData(byte msgId,String json){
		MessagePacking messagePacking = new MessagePacking(msgId);
		messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
		mCoreSocket.sendMessage(messagePacking,new DefaultSendMessageCallback());
	}
}
