package cn.com.incito.classroom.base;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.ui.activity.SplashActivity;
import cn.com.incito.classroom.ui.widget.MyAlertDialog;
import cn.com.incito.classroom.ui.widget.NetWorkDialog;
import cn.com.incito.classroom.ui.widget.ProgressiveDialog;
import cn.com.incito.classroom.utils.UpdateManager;
import cn.com.incito.common.utils.ToastHelper;

/**
 * activity基类 Created by popoy on 2014/8/5.
 */
public class BaseActivity extends FragmentActivity {
	
	public static final int RESPONDER_SUCCESS = 4;
	public static final int INSTALL_UPDATE = 1;

	protected int mScreenWidth;

	protected int mScreenHeight;
	private BaseHandler handler;

	protected float mDensity;
	protected NetWorkReceiver receiver;
	private ProgressiveDialog mProgressDialog;

	public NetWorkDialog netWorkDialog;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		handler = new BaseHandler(this);
		onAfterOnCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
		mProgressDialog = new ProgressiveDialog(this);
		mProgressDialog.setMessage(R.string.load_dialog_default_text);
		
	}

	protected void onAfterOnCreate(Bundle savedInstanceState) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		receiver = new NetWorkReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(receiver, intentFilter);

	}
	
	/**
	 * 在等待界面按下返回键提示是否退出
	 */
	@Override
	public void onBackPressed() {
		if("WaitingActivity".equals(AppManager.getAppManager().currentActivity().getClass().getSimpleName())){
			new MyAlertDialog(this).show();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}
	
	/**
	 * 显示不能发送消息的提示
	 * 只在屏幕没有锁的情况下提示
	 */
	protected void showToast(String info){
		if(!MyApplication.getInstance().isLockScreen()){
			ToastHelper.showCustomToast(this,info);
		}
	}
	
	/**
	 * 显示进度对话框
	 * @param id
	 */
	protected void showProgress(int id){
		if(mProgressDialog == null){
			mProgressDialog = new ProgressiveDialog(this);
			mProgressDialog.setMessage(id);
		}
		if(!mProgressDialog.isShowing()){
			mProgressDialog.show();
		}
	}
	
	/**
	 * 关闭进度框
	 */
	public void closeProgress(){
		if(mProgressDialog != null && mProgressDialog.isShowing()){
			mProgressDialog.dismiss();
		}
	}

	class NetWorkReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
				ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				NetworkInfo activeInfo = manager.getActiveNetworkInfo();
				if (activeInfo == null || wifiInfo == null) {
					Activity activity = AppManager.getAppManager().currentActivity();
					String activityName = activity.getClass().getSimpleName();
					if("SplashActivity".equals(activityName)){
						SplashActivity splashActivity = (SplashActivity) activity;
						UpdateManager updateManager = splashActivity.getUpdateManager();
						if(updateManager != null){
							updateManager.dimissDialog();
						}
					}
					if (netWorkDialog == null) {
						netWorkDialog = new NetWorkDialog(context);
						netWorkDialog.setCancelable(false);
						netWorkDialog.show();
					} else {
						if (!netWorkDialog.isShowing()) {
							netWorkDialog.show();
						}
					}
				}else{
					WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
					WifiInfo info = wifi.getConnectionInfo();
					MyApplication.getInstance().setDeviceId(info.getMacAddress().replace(":", "-"));
					if(netWorkDialog != null){
						netWorkDialog.dismiss();
					}
				}
			}
		}
	}
	
	private static class BaseHandler extends Handler{
		
		WeakReference<BaseActivity> reference;
		
		public BaseHandler(BaseActivity baseActivity) {
			this.reference = new WeakReference<BaseActivity>(baseActivity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			BaseActivity activity = reference.get();
			
			switch (msg.what) {
			case 0:
				if(activity != null){
					activity.closeProgress();
					activity.showToast("网络拥堵请稍后重试!");
				}
				break;
			case RESPONDER_SUCCESS:
				ToastHelper.showResponderSuccessToast(activity.getApplicationContext());
				break;
			case INSTALL_UPDATE:
				ToastHelper.showCustomToast(activity.getBaseContext(), "正在更新,更新成功后请点击重新进入");
			default:
				break;
			}
		}
	}

	/**
	 * 要求老师重新发送作业
	 */
	public void showRequestTecherSendPaper() {
		if(MyApplication.getInstance().isLockScreen()){
			MyApplication.getInstance().lockScreen(false);
		}
		ToastHelper.showCustomToast(this, "作业下载失败,请告知老师让老师重新发送!");
	}
	
	public void showError(){
		handler.sendEmptyMessage(0);
	}
	
	/**
	 * 显示抢答成功对话框
	* @author hm
	* @date 2015年2月5日 上午11:37:55 
	* @Title: showResponderSuccess 
	* @return void    返回类型 
	 */
	public void showResponderSuccess() {
		handler.sendEmptyMessage(RESPONDER_SUCCESS);
	}

	/**
	 * 提示正在更新
	 * @author hm
	 * @date 2015年2月6日 下午5:30:14 
	 * @return void
	 */
	public void showUpdate() {
		handler.sendEmptyMessage(INSTALL_UPDATE);
	}
}
