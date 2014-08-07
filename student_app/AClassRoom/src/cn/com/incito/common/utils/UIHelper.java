package cn.com.incito.common.utils;

import android.content.Intent;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.ui.activity.BindDeskActivity;
import cn.com.incito.classroom.ui.activity.SplashActivity;
import cn.com.incito.classroom.ui.activity.WaitingActivity;

public class UIHelper {
	private static UIHelper instance;
	private MyApplication app;
	private SplashActivity splashActivity;
	private WaitingActivity waitingActivity;
	private BindDeskActivity bindDeskActivity;
	
	private UIHelper() {
		app = MyApplication.getInstance();
	}

	public static UIHelper getInstance() {
		if(instance == null){
			instance = new UIHelper();
		}
		return instance;
	}

	public void setSplashActivity(SplashActivity splashActivity) {
		this.splashActivity = splashActivity;
	}

	public void setWaitingActivity(WaitingActivity waitingActivity) {
		this.waitingActivity = waitingActivity;
	}

	public void setBindDeskActivity(BindDeskActivity bindDeskActivity) {
		this.bindDeskActivity = bindDeskActivity;
	}

	public SplashActivity getSplashActivity() {
		return splashActivity;
	}

	public WaitingActivity getWaitingActivity() {
		return waitingActivity;
	}

	public BindDeskActivity getBindDeskActivity() {
		return bindDeskActivity;
	}

	/**
	 * 显示登录界面
	 */
	public void showWaitingActivity(){
		if (splashActivity == null) {
			return;
		}
		Intent intent = new Intent(splashActivity, WaitingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(intent);
		splashActivity.finish();
		splashActivity = null;
	}
	
	/**
	 * 显示课桌绑定界面
	 */
	public void showBindDeskActivity(){
		if (splashActivity == null) {
			return;
		}
		Intent intent = new Intent(splashActivity, BindDeskActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(intent);
		splashActivity.finish();
		splashActivity = null;
	}
	
	/**
	 * 显示登录界面
	 */
	public void showLoginActivity(){
		if (bindDeskActivity == null) {
			return;
		}
		Intent intent = new Intent(bindDeskActivity, WaitingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(intent);
		bindDeskActivity.finish();
		bindDeskActivity = null;
	}
}
