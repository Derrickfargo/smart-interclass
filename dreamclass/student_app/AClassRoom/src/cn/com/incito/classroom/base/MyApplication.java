package cn.com.incito.classroom.base;

import android.app.Application;
import android.app.ExecRootCmd;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.exception.AppUncaughtException;
import cn.com.incito.classroom.vo.LoginResVo;
import cn.com.incito.common.utils.LogUtil;

import com.google.code.microlog4android.config.PropertyConfigurator;

/**
 * 应用 appication（缓存各类数据） Created by popoy on 2014/7/28.
 */
@SuppressWarnings("deprecation")
public class MyApplication extends Application {
	public boolean isOnClass;// 是否在上课
	
	public boolean isOver = false;
	
	/**
	 * 作业ID用于学生提交作业或者老师收作业
	 */
	private String quizID;
	private SharedPreferences mPrefs;
	private WakeLock wl;
	private WifiLock mWifiLock;
	private LoginResVo loginResVo;
	public static String deviceId;
	private static MyApplication mInstance = null;
	public static final String strKey = "840FFE132BB1749F265E77000ED4A8E17ECEC190";
	private boolean isSubmitPaper;// 学生是否已提交作业
	private boolean isLockScreen;// 是否锁定屏幕
	private ContentResolver mContentResolver;

	public boolean isOnClass() {
		return isOnClass;
	}

	public void setOnClass(boolean isOnClass) {
		this.isOnClass = isOnClass;
	}
	public boolean isLockScreen() {
		return isLockScreen;
	}
	public void setLockScreen(boolean isLockScreen) {
		this.isLockScreen = isLockScreen;
	}
	public void closeSysScreenLock() {
		mContentResolver = getContentResolver();
		android.provider.Settings.System.putInt(mContentResolver,android.provider.Settings.Secure.LOCK_PATTERN_ENABLED, 0);
	}
	public boolean isSubmitPaper() {
		return isSubmitPaper;
	}
	public void setSubmitPaper(boolean isSubmitPaper) {
		this.isSubmitPaper = isSubmitPaper;
	}
	public SharedPreferences getSharedPreferences() {
		return mPrefs;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		PropertyConfigurator.getConfigurator(this).configure();
		sendBroadcast(new Intent("android.intent.action.HIDE_NAVIGATION_BAR"));
		AppUncaughtException appException = AppUncaughtException.getInstance();
		appException.init(this);
		Thread.setDefaultUncaughtExceptionHandler(appException);
		PowerManager pmManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiLock = manager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF,"cn.com.incito.classroom");
		wl = pmManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
		wl.acquire();
		mWifiLock.acquire();

		mInstance = this;
		mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		initApplication();
	}

	public void release() {
		mWifiLock.release();
		wl.release();
	}

	public static MyApplication getInstance() {
		return mInstance;
	}

	private void initApplication() {
		// 初始化服务端ip
		String ip = mPrefs.getString(Constants.PREFERENCE_IP, "");
		if (ip != null && !ip.trim().equals("")) {
			Constants.setIP(ip);
		}
		// 初始化本机mac地址
		initMacAddress();
	}

	private void initMacAddress() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			// 获取网络连接管理的对象
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				// 判断当前网络是否已经连接
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
					WifiInfo wifiInfo = wifi.getConnectionInfo();
					if (wifiInfo != null) {
						setDeviceId(wifiInfo.getMacAddress().replace(":", "-"));
					}
				}
			}
		}
	}

	public void stopSocketService() {
		Intent service = new Intent("cn.com.incito.classroom.service.SOCKET_SERVICE");
		stopService(service);
	}

	public LoginResVo getLoginResVo() {
		return loginResVo;
	}

	public void setLoginResVo(LoginResVo loginResVo) {
		this.loginResVo = loginResVo;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		MyApplication.deviceId = deviceId;
	}

	public String getQuizID() {
		return quizID;
	}

	public void setQuizID(String quizID) {
		this.quizID = quizID;
	}

	/**
	 * @param isLock
	 *            true是锁频屏，false解屏
	 */
	public void lockScreen(boolean isLock) {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		boolean screenOn = pm.isScreenOn();

		if (Constants.OPEN_LOCK_SCREEN) {
			LogUtil.d("是否收到解锁屏信息：" + isLock);
			ContentResolver mContentResolver = this.getApplicationContext().getContentResolver();
			ExecRootCmd execRootCmd = new ExecRootCmd();
			if (isLock) {
				this.setLockScreen(isLock);
				Settings.Global.putInt(mContentResolver,"disable_powerkey", 1);// 屏蔽电源按钮唤醒功能
				execRootCmd.powerkey();
			} else {
				if (isLockScreen()) {
					if (screenOn) {
						this.setLockScreen(isLock);
						Settings.Global.putInt(mContentResolver,"disable_powerkey", 0); // 打开电源按钮唤醒功能
						execRootCmd.powerkey();
					}
					this.setLockScreen(isLock);
					Settings.Global.putInt(mContentResolver,"disable_powerkey", 0); // 打开电源按钮唤醒功能
					execRootCmd.powerkey();
					KeyguardManager mManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
					KeyguardLock mKeyguardLock = mManager.newKeyguardLock("Lock");
					// 让键盘锁失效
					mKeyguardLock.disableKeyguard();
				}
			}
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		sendBroadcast(new Intent("android.intent.action.SHOW_NAVIGATION_BAR"));
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		sendBroadcast(new Intent("android.intent.action.SHOW_NAVIGATION_BAR"));
	}
}
