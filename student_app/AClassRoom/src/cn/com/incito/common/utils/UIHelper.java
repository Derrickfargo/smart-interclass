package cn.com.incito.common.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.ui.activity.BindDeskActivity;
import cn.com.incito.classroom.ui.activity.DrawBoxActivity;
import cn.com.incito.classroom.ui.activity.WaitingActivity;

import com.alibaba.fastjson.JSONObject;

public class UIHelper {
	private static UIHelper instance;
	private MyApplication app;
	private WaitingActivity waitingActivity;
	private BindDeskActivity bindDeskActivity;
	private DrawBoxActivity drawBoxActivity;

	private UIHelper() {
		app = MyApplication.getInstance();
	}

	public static UIHelper getInstance() {
		if (instance == null) {
			instance = new UIHelper();
		}
		return instance;
	}

	public void setWaitingActivity(WaitingActivity waitingActivity) {
		this.waitingActivity = waitingActivity;
	}

	public void setBindDeskActivity(BindDeskActivity bindDeskActivity) {
		this.bindDeskActivity = bindDeskActivity;
	}

	/*
	 * public SplashActivity getSplashActivity() { return splashActivity; }
	 */

	public WaitingActivity getWaitingActivity() {
		return waitingActivity;
	}

	public BindDeskActivity getBindDeskActivity() {
		return bindDeskActivity;
	}

	public DrawBoxActivity getDrawBoxActivity() {
		return drawBoxActivity;
	}

	public void setDrawBoxActivity(DrawBoxActivity drawBoxActivity) {
		this.drawBoxActivity = drawBoxActivity;
	}

	/**
	 * 显示登录界面
	 */
	public void showWaitingActivity() {
		/*
		 * if (splashActivity == null) { return; }
		 */
		Intent intent = new Intent(app, WaitingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(intent);
		/*
		 * splashActivity.finish(); splashActivity = null;
		 */
	}

	/**
	 * 显示课桌绑定界面
	 */
	public void showBindDeskActivity() {
		/*
		 * if (splashActivity == null) { return; }
		 */
		Intent intent = new Intent(app, BindDeskActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(intent);
		/*
		 * splashActivity.finish(); splashActivity = null;
		 */
	}

	/**
	 * 显示登录界面
	 */
	public void showLoginActivity() {
		if (bindDeskActivity == null) {
			return;
		}
		Intent intent = new Intent(bindDeskActivity, WaitingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(intent);
		bindDeskActivity.finish();
		bindDeskActivity = null;
	}

	/**
	 * 显示电子绘画板
	 */
	public void showDrawBoxActivity(byte[] paper) {
		Intent intent = new Intent();
		MyApplication.getInstance().setSubmitPaper(false);
		if (paper != null) {
			Bundle mBundle = new Bundle();
			mBundle.putByteArray("paper", paper);
			intent.putExtras(mBundle);
		}
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Constants.ACTION_SHOW_DRAWBOX);
		app.startActivity(intent);
	}

	public void showEditGroupActivity(int groupID) {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("id", groupID);
		intent.setAction(Constants.ACTION_SHOW_EDIT_GROUP);
		app.startActivity(intent);
	}

	public void showConfirmGroupActivity(JSONObject data) {
		/*
		 * Intent intent = new Intent(editGroupInfoActivity,
		 * ConfirmGroupInfoActivity.class);
		 */
		Intent intent = new Intent(Constants.ACTION_SHOW_CONFIRM_GROUP);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("data", data);
		app.startActivity(intent);

	}

	public void showToast(Activity mActivity, String mes) {
		Toast.makeText(mActivity, mes, Toast.LENGTH_SHORT);
	}

	public void showDrawBoxActivity() {
		Intent intent = new Intent(app.getApplicationContext(),
				DrawBoxActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Constants.ACTION_SHOW_EDIT_GROUP);
		app.startActivity(intent);
	}
}
