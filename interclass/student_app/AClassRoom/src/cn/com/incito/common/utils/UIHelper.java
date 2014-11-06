package cn.com.incito.common.utils;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.ui.activity.ClassReadyActivity;
import cn.com.incito.classroom.ui.activity.DrawBoxActivity;
import cn.com.incito.classroom.ui.activity.SelectGroupActivity;
import cn.com.incito.classroom.ui.activity.WaitForOtherMembersActivity;
import cn.com.incito.classroom.ui.activity.WifiSelectorActivity;
import cn.com.incito.classroom.vo.Group;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;

public class UIHelper {
	private static UIHelper instance;
	private MyApplication app;
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
	
	
	/**
	 * 显示选择分组界面 
	 * @param json  服务器上传来分组数据
	 */
	public  void showGroupSelect(String json){
		Intent intent = new Intent(app,SelectGroupActivity.class);
		intent.putExtra("group", json);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(intent);
		
	}



	/*
	 * public SplashActivity getSplashActivity() { return splashActivity; }
	 */



	public DrawBoxActivity getDrawBoxActivity() {
		return drawBoxActivity;
	}

	public void setDrawBoxActivity(DrawBoxActivity drawBoxActivity) {
		if (this.drawBoxActivity != null) {
			this.drawBoxActivity.finish();
			this.drawBoxActivity = null;
		}
		this.drawBoxActivity = drawBoxActivity;
	}

	/**
	 * 显示选择小组界面
	 */
	public void showGroupActivity() {
		Intent intent = new Intent(app, SelectGroupActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(intent);
	}

	public void showSelectWifiActivity(){
		Intent intent = new Intent(app, WifiSelectorActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(intent);
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
	/**
	 * 显示小组列表
	 */
	public void showGroupListActivity(){
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Constants.ACTION_SHOW_GROUP_LIST);
		app.startActivity(intent);
	}
	

	/**
	 * 启动等待小组其他成员界面
	 * @param data  返回的json数据
	 */
	public void showConfirmGroupActivity(String data) {
		WaitForOtherMembersActivity.startSelf(app, data);

	}

	public void showToast(Activity mActivity, String mes) {
		Toast.makeText(mActivity, mes, Toast.LENGTH_SHORT);
	}

	public void showDrawBoxActivity() {
		Intent intent = new Intent(app.getApplicationContext(),DrawBoxActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Constants.ACTION_SHOW_EDIT_GROUP);
		app.startActivity(intent);
	}

	/**
	 * 显示准备界面
	 */
	public void showClassReadyActivity() {
		Intent intent = new Intent(app.getApplicationContext(),
				ClassReadyActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(intent);
		
	}
}
