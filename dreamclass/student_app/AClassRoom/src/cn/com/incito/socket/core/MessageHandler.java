package cn.com.incito.socket.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.ui.activity.WaitingActivity;
import cn.com.incito.common.utils.LogUtil;

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
	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	protected BaseActivity activity;

	
	/**
	 * 直接传入jsonObject进行处理
	 * @param jsonObject
	 * @author hm
	 */
	public final void handleMessage(JSONObject jsonObject){
		data = jsonObject;
		activity = (BaseActivity) AppManager.getAppManager().currentActivity();
		
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				handleMessage();
			}
		});
	}
	
	protected final void finishNotWaitingActivity(){
		if(!WaitingActivity.class.equals(activity.getClass())){
			LogUtil.d("销毁不是等待界面的界面!");
			activity.finish();
		}
	}
	protected abstract void handleMessage();
}
