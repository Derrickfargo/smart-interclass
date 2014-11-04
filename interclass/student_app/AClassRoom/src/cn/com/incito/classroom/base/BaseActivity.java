package cn.com.incito.classroom.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import cn.com.incito.classroom.ui.widget.NetWorkDialog;

import com.google.code.microlog4android.config.PropertyConfigurator;

/**
 * activity基类 Created by popoy on 2014/8/5.
 */
public class BaseActivity extends FragmentActivity {

	protected int mScreenWidth;

	protected int mScreenHeight;

	protected float mDensity;

	NetWorkReceiver receiver;

	public NetWorkDialog netWorkDialog;

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


	protected void onAfterOnCreate(Bundle savedInstanceState) {
	}

	@Override
	protected void onResume() {
		super.onResume();
//		receiver = new NetWorkReceiver();
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//		registerReceiver(receiver, intentFilter);

	}

	@Override
	protected void onPause() {
		super.onPause();
//		unregisterReceiver(receiver);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}

	class NetWorkReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
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
