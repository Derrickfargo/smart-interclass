package cn.com.incito.classroom.base;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.classroom.ui.activity.SplashActivity;
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
import android.provider.Settings;
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
		receiver = new NetWorkReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(receiver, intentFilter);

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
				if (activeInfo == null || wifiInfo == null) {
					showErrorNetDialog(context);
				}
			}
		}

		public void showErrorNetDialog(final Context context) {
			new AlertDialog.Builder(context)
					.setTitle("网络异常，请进行网络设置或重试哦！！")
					.setPositiveButton("设置",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											Settings.ACTION_SETTINGS);
									context.startActivity(intent);
									dialog.dismiss();
								}
							})
					.setNegativeButton("重试",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									CoreSocket.getInstance()
											.restartConnection();
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e1) {
										e1.printStackTrace();
									}
									startMainAct();
									dialog.dismiss();
								}
							}).show();
		}

		/**
		 * 发送连接请求
		 */
		public void startMainAct() {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("imei", MyApplication.deviceId);
			MessagePacking messagePacking = new MessagePacking(
					Message.MESSAGE_DEVICE_HAS_BIND);
			messagePacking.putBodyData(DataType.INT,
					BufferUtils.writeUTFString(jsonObject.toJSONString()));
			CoreSocket.getInstance().sendMessage(messagePacking);
			WLog.i(SplashActivity.class, "开始判定设备是否绑定..." + "request:"
					+ jsonObject.toJSONString());
		}
	}
}
