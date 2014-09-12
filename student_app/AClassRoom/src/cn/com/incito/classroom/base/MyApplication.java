package cn.com.incito.classroom.base;

import android.app.Application;
import android.app.ExecRootCmd;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.service.LogService;
import cn.com.incito.classroom.vo.LoginResVo;
import cn.com.incito.socket.handler.LockScreenHandler;
import cn.com.incito.wisdom.sdk.cache.disk.impl.TotalSizeLimitedDiscCache;
import cn.com.incito.wisdom.sdk.cache.disk.naming.Md5FileNameGenerator;
import cn.com.incito.wisdom.sdk.cache.mem.AbstractMemoryCache;
import cn.com.incito.wisdom.sdk.image.loader.ImageLoader;
import cn.com.incito.wisdom.sdk.image.loader.ImageLoaderConfiguration;
import cn.com.incito.wisdom.sdk.image.loader.assist.LRULimitedMemoryCacheBitmapCache;
import cn.com.incito.wisdom.sdk.image.loader.assist.LRUMemoryCacheBitmapCache;
import cn.com.incito.wisdom.sdk.image.loader.assist.QueueProcessingType;
import cn.com.incito.wisdom.sdk.log.WLog;
import cn.com.incito.wisdom.sdk.net.download.BaseImageDownloader;
import cn.com.incito.wisdom.sdk.net.download.SlowNetworkImageDownloader;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;
import cn.com.incito.wisdom.sdk.openudid.OpenUDIDManager;
import cn.com.incito.wisdom.sdk.utils.StorageUtils;

import com.baidu.mapapi.SDKInitializer;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用 appication（缓存各类数据） Created by popoy on 2014/7/28.
 */
public class MyApplication extends Application {

	public boolean isOnClass;// 是否在上课

	public boolean isOnClass() {
		return isOnClass;
	}

	public void setOnClass(boolean isOnClass) {
		this.isOnClass = isOnClass;
	}

	private LoginResVo loginResVo;

	public static String deviceId;

	private static final String TAG = MyApplication.class.getSimpleName();

	private static MyApplication mInstance = null;

	public static final String strKey = "840FFE132BB1749F265E77000ED4A8E17ECEC190";

	private static String IMEI;

	private boolean isSubmitPaper;// 学生是否已提交作业

	private boolean isLockScreen;// 是否锁定屏幕

	ContentResolver mContentResolver;

	public boolean isLockScreen() {
		return isLockScreen;
	}

	public void setLockScreen(boolean isLockScreen) {
		this.isLockScreen = isLockScreen;
	}

	public void closeSysScreenLock() {
		mContentResolver = getContentResolver();
		android.provider.Settings.System.putInt(mContentResolver,
				android.provider.Settings.System.LOCK_PATTERN_ENABLED, 0);
	}

	public boolean isSubmitPaper() {
		return isSubmitPaper;
	}

	public void setSubmitPaper(boolean isSubmitPaper) {
		this.isSubmitPaper = isSubmitPaper;
	}

	/**
	 * 作业ID用于学生提交作业或者老师收作业
	 */
	private String quizID;

	private SharedPreferences mPrefs;

	private WakeLock wl;

	public SharedPreferences getSharedPreferences() {
		return mPrefs;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sendBroadcast(new Intent("android.intent.action.HIDE_NAVIGATION_BAR"));
		// closeSysScreenLock();

		mInstance = this;
		mPrefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		initApplication();
		MobclickAgent.openActivityDurationTrack(false);// 禁止友盟的自动统计功能

		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		// TelephonyManager tm = (TelephonyManager) this
		// .getSystemService(Context.TELEPHONY_SERVICE);
		// IMEI = tm.getDeviceId();
		IMEI = info.getMacAddress();

		OpenUDIDManager.sync(this);
		File cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(),
				Constants.WISDOMCITY_IAMGE_CACHE_SDCARD_PATH);
		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
		AbstractMemoryCache<String, Bitmap> memoryCache;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			memoryCache = new LRUMemoryCacheBitmapCache(memoryCacheSize);
		} else {
			memoryCache = new LRULimitedMemoryCacheBitmapCache(memoryCacheSize);
		}
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCache(memoryCache)
				.denyCacheImageMultipleSizesInMemory()
				.discCache(
						new TotalSizeLimitedDiscCache(cacheDir,
								new Md5FileNameGenerator(), 10 * 1024 * 1024))
				.imageDownloader(
						new SlowNetworkImageDownloader(new BaseImageDownloader(
								this)))
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);

	}

	public static MyApplication getInstance() {
		return mInstance;
	}

	public final String getUDID() {
		if (OpenUDIDManager.isInitialized()) {
			return IMEI + "_" + OpenUDIDManager.getOpenUDID();
		}
		return IMEI;
	}

	private void initApplication() {
		Constants.setIP(mPrefs.getString(Constants.PREFERENCE_IP, ""));
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		deviceId = tm.getDeviceId();
		Intent service = new Intent(
				"cn.com.incito.classroom.service.SOCKET_SERVICE");
		startService(service);
		WLog.i(MyApplication.class, "socket service started");
		if (Constants.LOG_OPEN) {
			Intent logservice = new Intent(
					"cn.com.incito.classroom.service.LOG_SERVICE");
			startService(logservice);
			WLog.i(MyApplication.class, "log service started");
		}

	}

	public void stopSocketService() {
		Intent service = new Intent(
				"cn.com.incito.classroom.service.SOCKET_SERVICE");
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
		this.deviceId = deviceId;
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
		if (Constants.OPEN_LOCK_SCREEN) {
			WLog.i(LockScreenHandler.class, "是否收到解锁屏信息：" + isLock);

			ContentResolver mContentResolver = this.getApplicationContext()
					.getContentResolver();
			ExecRootCmd execRootCmd = new ExecRootCmd();
			if (isLock) {
				MyApplication.getInstance().setLockScreen(isLock);
				PowerManager pmManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
				wl = pmManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
						"My Tag");
				wl.acquire();
				boolean ret = Settings.Global.putInt(mContentResolver,
						"disable_powerkey", 1);// 屏蔽电源按钮唤醒功能
				execRootCmd.powerkey();
			} else {
				if (MyApplication.getInstance().isLockScreen()) {
					MyApplication.getInstance().setLockScreen(isLock);
					boolean ret1 = Settings.Global.putInt(mContentResolver,
							"disable_powerkey", 0); // 打开电源按钮唤醒功能
					execRootCmd.powerkey();
					KeyguardManager mManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
					KeyguardLock mKeyguardLock = mManager
							.newKeyguardLock("Lock");
					// 让键盘锁失效
					mKeyguardLock.disableKeyguard();
					wl.release();
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
		WLog.i(MyApplication.class, "广播发出");

	}
}
