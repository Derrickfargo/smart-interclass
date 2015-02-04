package cn.com.incito.socket.handler;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Looper;
import android.os.SystemClock;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.ui.activity.DrawBoxActivity;
import cn.com.incito.classroom.ui.activity.SplashActivity;
import cn.com.incito.classroom.ui.activity.WaitingActivity;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.util.SendMessageUtil;

/**
 * 登陆处理hanlder Created by liushiping on 2014/7/28.
 * 
 * 修改的地方 取消了调用心跳检测方法
 */
public class DeviceLoginHandler extends MessageHandler {
	@Override
	protected void handleMessage() {
		String ip = (String) data.get("server_ip");
		String port = (String) data.get("server_port");
		Editor editor = MyApplication.getInstance().getSharedPreferences().edit();
		editor.putString("server_ip", ip);
		editor.putString("server_port", port);
		editor.commit();
		editor.apply();
		
		/**
		 * 发送判断apk是否需要更新
		 */
//		MyApplication myApplication = MyApplication.getInstance();
//		int versionCode = AndroidUtil.currentVersionCode(myApplication.getApplicationContext());//当前运行的app版本号
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("currentVersionCode", versionCode);
//		SendMessageUtil.isUpdateApk(jsonObject.toJSONString());
		
		Activity activity = AppManager.getAppManager().currentActivity();
		String activityName = activity.getClass().getSimpleName();
		if ("SplashActivity".equals(activityName)) {
			final SplashActivity splashActivity = (SplashActivity) activity;
			boolean isUpdateAp = splashActivity.isUpdateApk();
			LogUtil.d("当前界面是开始动画界面检查apk是否需要更新:isUpdateAp:" + isUpdateAp);
			if (!isUpdateAp) {
				SystemClock.sleep(1000);
				LogUtil.d("当前界面是开始动画界面检查apk不需要更新,发送设备是否绑定消息");
				SendMessageUtil.sendDeviceHasBind();
			} else {
				 Looper.prepare();
				 splashActivity.UpdateAap();
				 Looper.loop();
			}
		} else {
			/**
			 * 重连操作刷新学生小小组界面
			 */
			WaitingActivity waitingActivity = UIHelper.getInstance().getWaitingActivity();
			if (waitingActivity != null) {
				LogUtil.d("当前界面不是动画界面,发送获取小组学生信息,更新学生状态!");
				SendMessageUtil.sendGroupList();
			}
			/**
			 * 如果是做作业界面 发送提交作业命令
			 */
			if ("DrawBoxActivity".equals(activityName)) {
				DrawBoxActivity boxActivity = UIHelper.getInstance().getDrawBoxActivity();
				if (boxActivity != null) {
					boxActivity.sendPaperRequest();
				}
			}
		}
	}
}
