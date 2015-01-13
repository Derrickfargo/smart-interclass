package cn.com.incito.socket.core;

import java.nio.ByteBuffer;

import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * 消息对应的处理器接口
 * 存放消息对应的处理逻辑提供共有的抽象接口方法
 *
 * @author 刘世平
 */
public abstract class MessageHandler {
	protected Message message;
	protected JSONObject data;
	
	/**
	 * 消息对应的处理器 存放消息对应的处理逻辑，在消息分发时使用
	
	/**
	 * 直接传入jsonObject进行处理
	 * @param jsonObject
	 * @author hm
	 */
	public void handleMessage(JSONObject jsonObject){
		data = jsonObject;
		handleMessage();
	}

	protected abstract void handleMessage();
}
