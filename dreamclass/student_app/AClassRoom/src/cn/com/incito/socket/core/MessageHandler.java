package cn.com.incito.socket.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Looper;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.ui.activity.DrawBoxActivity;
import cn.com.incito.classroom.ui.activity.SplashActivity;
import cn.com.incito.classroom.ui.activity.WaitingActivity;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.util.SendMessageUtil;

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
	
	/**
	 * 销毁不是等待界面的activity
	 * @author hm
	 * @date 2015年2月9日 上午10:56:44 
	 * @return void
	 */
	protected final void finishNotWaitingActivity(){
		if(!WaitingActivity.class.equals(activity.getClass())){
			LogUtil.d("销毁不是等待界面的界面：" + activity.getClass());
			activity.finish();
		}
	}
	
	/**
	 * 老版本重连时的数据刷新
	 * @author hm
	 * @date 2015年2月9日 上午10:52:51 
	 * @return void
	 */
	protected void reconnectOld() {
		/**
		 * 重连操作刷新学生小小组界面
		 */
		if (null != UIHelper.getInstance().getWaitingActivity()) {
			LogUtil.d("当前界面不是动画界面,发送获取小组学生信息,更新学生状态!");
			SendMessageUtil.sendGroupList();
		}
		/**
		 * 如果是做作业界面 发送提交作业命令
		 */
		if (DrawBoxActivity.class.equals(activity.getClass())) {
			DrawBoxActivity boxActivity = (DrawBoxActivity) activity;
			if (boxActivity != null) {
				boxActivity.sendPaperRequest();
			}
		}
	}
	
	/**
	 * 非静默安装升级方式
	 * @author hm
	 * @date 2015年2月9日 上午10:52:01 
	 * @return void
	 */
	protected void oldUpdate() {
		if (SplashActivity.class.equals(activity.getClass())) {
			final SplashActivity splashActivity = (SplashActivity) activity;
			boolean isUpdateAp = splashActivity.isUpdateApk();
			LogUtil.d("当前界面是开始动画界面检查apk是否需要更新:isUpdateAp:" + isUpdateAp);
			if (!isUpdateAp) {
				LogUtil.d("当前界面是开始动画界面检查apk不需要更新,发送设备是否绑定消息");
				SendMessageUtil.sendDeviceHasBind();
			} else {
				 Looper.prepare();
				 splashActivity.UpdateAap();
				 Looper.loop();
			}
		} else {
			reconnectOld();
		}
	}
	
	/**
	 * 静默安装的重连判断
	 * @author hm
	 * @date 2015年2月9日 上午10:54:38 
	 * @return void
	 */
	protected void reconnectNew() {
		/**
		 * 判断是否是重连
		 */
		if(UIHelper.getInstance().getWaitingActivity() != null){
			LogUtil.d("是重连操作,请求小组成员数据");
			SendMessageUtil.sendGroupList();
			 /**
			  *  如果是做作业界面 发送提交作业命令
			  */
			if ("DrawBoxActivity".equals(activity.getClass().getSimpleName())) {
				DrawBoxActivity boxActivity =(DrawBoxActivity) activity;
				boxActivity.sendPaperRequest();
			}
		}else{
			LogUtil.d("不是重连操作,判断设备是否绑定");
			SendMessageUtil.sendDeviceHasBind();
		}
	}
	
	protected abstract void handleMessage();
}
