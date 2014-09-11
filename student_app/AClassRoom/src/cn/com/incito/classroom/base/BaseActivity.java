package cn.com.incito.classroom.base;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.classroom.ui.activity.SplashActivity;
import cn.com.incito.classroom.ui.widget.NetWorkDialog;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.sdk.log.WLog;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * activity基类 Created by popoy on 2014/8/5.
 */
public class BaseActivity extends FragmentActivity {

	protected int mScreenWidth;

	protected int mScreenHeight;

	protected float mDensity;

	NetWorkReceiver receiver;
	private boolean netDectOpen = true;
	NetWorkDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppManager.getAppManager().addActivity(this);
		onAfterOnCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
	}

	public boolean isNetDectOpen() {
		return netDectOpen;
	}

	public void setNetDectOpen(boolean netDectOpen) {
		this.netDectOpen = netDectOpen;
	}

	protected void onAfterOnCreate(Bundle savedInstanceState) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (netDectOpen) {
			receiver = new NetWorkReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
			registerReceiver(receiver, intentFilter);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (netDectOpen) {
			unregisterReceiver(receiver);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}

	class NetWorkReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					"android.net.conn.CONNECTIVITY_CHANGE")) {
				ConnectivityManager manager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo wifiInfo = manager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				NetworkInfo activeInfo = manager.getActiveNetworkInfo();
				if (activeInfo == null || wifiInfo == null){
					if(dialog==null){
						dialog=new NetWorkDialog(context);
						dialog.show();
					}else{
						if(!dialog.isShowing()){
							dialog.show();
						}
					}
					
					
				}	
			}
		}

	}
}
