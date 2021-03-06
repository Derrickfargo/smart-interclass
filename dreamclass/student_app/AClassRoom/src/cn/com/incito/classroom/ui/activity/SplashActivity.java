package cn.com.incito.classroom.ui.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.exception.AppException;
import cn.com.incito.classroom.ui.widget.NetWorkDialog;
import cn.com.incito.classroom.utils.ApiClient;
import cn.com.incito.classroom.utils.UpdateManager;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.classroom.vo.Version;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 用户其启动界面时候的一个启动页面完成一些初始化工作 Created by popoy on 2014/7/28.
 */

public class SplashActivity extends BaseActivity {

	private TextView tv_loading_msg;

	private ImageButton ib_setting_ip;

	private IpSettingDialogFragment dialog;

	private int code;
	private String url;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.splash, null);
		setContentView(view);
		
		if(!isAddShortCut()){
			addShortCut();
		}
		
		try {
			PackageManager pm = getPackageManager();
			PackageInfo info = pm.getPackageInfo("cn.com.incito.classroom", 0);
			code = info.versionCode;
		} catch (NameNotFoundException e) {
			ApiClient.uploadErrorLog(e.getMessage());
		}
		ib_setting_ip = (ImageButton) view.findViewById(R.id.ib_setting_ip);
		ib_setting_ip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showSettingDialog();
			}
		});
		tv_loading_msg = (TextView) view.findViewById(R.id.tv_loading_msg);

		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				startMain();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {

			}
		});
	}

	/**
	 * 启动主界面
	 */

	private void startMain() {
		tv_loading_msg.setText(R.string.loading_msg);
		Log.i("SplashActivity", "startMain");
		new Thread() {

			public void run() {
				while (true) {
					Log.i("SplashActivity", "检查WiFi是否连接 ");
					if (checkWifi()) {
						android.os.Message message = new android.os.Message();
						message.what = 2;
						mHandler.sendMessage(message);
						MyApplication app = MyApplication.getInstance();
						WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
						WifiInfo info = wifi.getConnectionInfo();
						app.setDeviceId(info.getMacAddress().replace(":", "-"));
						Logger.debug(Utils.getTime() + "SplashActivity:"
								+ "WiFi已连接，检查Socket是否连接 ");
						Log.i("SplashActivity", "WiFi已连接，检查Socket是否连接 ");
						if (!CoreSocket.getInstance().isConnected()) {
							Logger.debug("SplashActivity:"
									+ "Socket无连接，开始Socket重连，startMain退出");
							Log.i("SplashActivity",
									"Socket无连接，开始Socket重连，startMain退出 ");
							CoreSocket.getInstance().disconnect();
							showSetting();
							restartConnector();
							break;
						} else {
							Logger.debug(Utils.getTime() + "SplashActivity:"
									+ "Socket已连接，开始登陆，startMain退出");
							Log.i("SplashActivity",
									"Socket已连接，开始登陆，startMain退出 ");
							startMainAct();
						}
						break;
					}
					Logger.debug(Utils.getTime() + "SplashActivity:"
							+ "WiFi未连接");
					Log.i("SplashActivity", "WiFi未连接 ");
					android.os.Message message1 = new android.os.Message();
					message1.what = 1;
					mHandler.sendMessage(message1);
					SplashActivity.this.sleep(3000);
				}
			}
		}.start();

	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (netWorkDialog == null) {
					netWorkDialog = new NetWorkDialog(SplashActivity.this);
					netWorkDialog.show();
				} else if (netWorkDialog != null && !netWorkDialog.isShowing()) {
					netWorkDialog.show();
				}
				break;
			case 2:
				if (netWorkDialog != null && netWorkDialog.isShowing()) {
					netWorkDialog.dismiss();
				}
				break;
			case 0:
				ib_setting_ip.setVisibility(View.VISIBLE);
				break;
			case 1000:
				UpdateManager mUpdateManager = new UpdateManager(
						SplashActivity.this, url);
				mUpdateManager.checkUpdateInfo();
				break;
			default:
				break;
			}

		}
	};

	private void showSetting() {
		android.os.Message message = new android.os.Message();
		message.what = 0;
		mHandler.sendMessage(message);
	}

	@Override
	public void onBackPressed() {
		AppManager.getAppManager().AppExit(this);
	}

	private void showSettingDialog() {
		dialog = new IpSettingDialogFragment();
		dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		dialog.show(getSupportFragmentManager(), "");
	}

	/**
	 * 发送连接请求
	 */
	public void startMainAct() {
		if (!isUpdateApk()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("imei", MyApplication.deviceId);
			MessagePacking messagePacking = new MessagePacking(
					Message.MESSAGE_DEVICE_HAS_BIND);
			messagePacking.putBodyData(DataType.INT,
					BufferUtils.writeUTFString(jsonObject.toJSONString()));
			CoreSocket.getInstance().sendMessage(messagePacking);
			Logger.debug(Utils.getTime() + "SplashActivity:" + "开始判定设备是否绑定..."
					+ "request:" + jsonObject.toJSONString());
			Log.i("SplashActivity",
					"开始判定设备是否绑定..." + "request:" + jsonObject.toJSONString());
		}
	}

	/**
	 * 重新建立连接
	 */
	public void restartConnector() {
		new Thread() {
			public void run() {
				while (Boolean.TRUE) {
					SplashActivity.this.sleep(3000);
					Logger.debug(Utils.getTime() + "SplashActivity:"
							+ "Socket开始重连 ");
					Log.i("SplashActivity", "Socket开始重连 ");
					CoreSocket.getInstance().restartConnection();
					SplashActivity.this.sleep(1000);// 等待1秒后检查连接
					if (!CoreSocket.getInstance().isConnected()) {
						Logger.debug(Utils.getTime() + "SplashActivity:"
								+ "Socket未连接 ");
						Log.i("SplashActivity", "Socket未连接 ");
						CoreSocket.getInstance().disconnect();
						continue;
					}
					Logger.debug(Utils.getTime() + "SplashActivity:"
							+ "Socket已连接 ");
					Log.i("SplashActivity", "Socket已连接 ");
					if (dialog != null) {
						dialog.dismiss();
					}
					startMainAct();
					break;
				}
			}
		}.start();
	}

	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			ApiClient.uploadErrorLog(e.getMessage());
			e.printStackTrace();
		}
	}

	public boolean checkWifi() {
		ConnectivityManager connectivity = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			// 获取网络连接管理的对象
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				// 判断当前网络是否已经连接
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isUpdateApk() {
		// TODO 升级
		String ip = MyApplication.getInstance().getSharedPreferences()
				.getString("server_ip", "");
		String port = MyApplication.getInstance().getSharedPreferences()
				.getString("server_port", "");
		if (!Utils.isEmpty(ip) && !Utils.isEmpty(port)) {
			try {
				JSONObject updateResult = JSONObject.parseObject(ApiClient
						.updateApk(code));
				Logger.debug(Utils.getTime() + "SplashActivity:" + "版本更新返回信息："
						+ updateResult);
				Log.i("SplashActivity", "版本更新返回信息：" + updateResult);
				if (updateResult.getInteger("code") == 0) {
					Version version = JSON.parseObject(updateResult
							.getJSONObject("data").toJSONString(),
							Version.class);
					url = Constants.HTTP + ip + ":" + port
							+ Constants.URL_DOWNLOAD_APK + version.getId();
					Logger.debug(Utils.getTime() + "SplashActivity:" + "更新地址："
							+ url);
					mHandler.sendEmptyMessage(1000);
					return true;
				}
			} catch (AppException e) {
				ApiClient.uploadErrorLog(e.getMessage());
				e.printStackTrace();
			}
		}
		return false;
	}

	// 判断是否已经创建快捷方式
	private boolean isAddShortCut() {
		ContentResolver contentResolver = this.getContentResolver();

		int versionLevel = android.os.Build.VERSION.SDK_INT;// 系统版本

		String systemFileName = "";
		if (versionLevel > 8) {
			systemFileName = "com.android.launcher2.settings";
		} else {
			systemFileName = "com.android.launcher.settings";
		}

		final Uri CONTENT_URI = Uri.parse("content://" + systemFileName
				+ "/favorites?notify=true");

		Cursor cursor = contentResolver.query(CONTENT_URI, new String[] {
				"title", "iconResource" }, "title=?",
				new String[] { getString(R.string.app_name) }, null);

		if (cursor != null && cursor.getCount() > 0) {
			return true;
		}
		return false;
	}

	private void addShortCut() {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 设置属性
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getResources().getString(R.string.app_name));

		// 是否允许重复创建
		shortcut.putExtra("duplicate", false);

		// 设置桌面快捷方式的图标
		Parcelable icon = Intent.ShortcutIconResource.fromContext(this,
				R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

		// 点击快捷方式的操作
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(SplashActivity.this, SplashActivity.class);

		// 设置启动程序
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		// 广播通知桌面去创建
		this.sendBroadcast(shortcut);

	}

}
