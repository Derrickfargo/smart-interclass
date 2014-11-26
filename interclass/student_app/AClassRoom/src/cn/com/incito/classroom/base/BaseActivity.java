package cn.com.incito.classroom.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;
import cn.com.incito.classroom.ui.widget.NetWorkDialog;

/**
 * activity基类 Created by popoy on 2014/8/5.
 */
public class BaseActivity extends FragmentActivity {

	protected int mScreenWidth;

	protected int mScreenHeight;

	protected float mDensity;


	public NetWorkDialog netWorkDialog;
	private NetWorkReceiver netWorkReciver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
		onAfterOnCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
		
		
	}
	
	public void registRecier(){
		if(netWorkReciver == null){
			netWorkReciver = new NetWorkReceiver();
		}
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		
		this.registerReceiver(netWorkReciver, intentFilter);
	}
	
	public void unRegistReciver(){
		unregisterReceiver(netWorkReciver);
	}


	protected void onAfterOnCreate(Bundle savedInstanceState) {
	}

	@Override
	protected void onResume() {
		super.onResume();

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if("WifiSelectorActivity".equals(AppManager.getAppManager().currentActivity().getClass().getSimpleName())){
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}

	class NetWorkReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			String currentActivity = AppManager.getAppManager().currentActivity().getClass().getSimpleName();
			if(!"SplashActivity".equals(currentActivity) && !"SelectWifiActivity".equals(currentActivity)){
				if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
					ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
					NetworkInfo activeInfo = manager.getActiveNetworkInfo();
					if (activeInfo == null || wifiInfo == null) {
						if (netWorkDialog == null) {
							netWorkDialog = new NetWorkDialog(context);
							netWorkDialog.setCancelable(false);
							netWorkDialog.show();
						} else {
							if (!netWorkDialog.isShowing()) {
								netWorkDialog.show();
							}
						}
					}
				}
			}
		}
	}
}
