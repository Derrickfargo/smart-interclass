package cn.com.incito.socket.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private ExecutorService executorService = Executors.newCachedThreadPool();
	
	/**
	 * 消息对应的处理器 存放消息对应的处理逻辑，在消息分发时使用
	
	/**
	 * 直接传入jsonObject进行处理
	 * @param jsonObject
	 * @author hm
	 */
	public void handleMessage(JSONObject jsonObject){
		data = jsonObject;
		
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				handleMessage();
			}
		});
	}
	protected abstract void handleMessage();
}
