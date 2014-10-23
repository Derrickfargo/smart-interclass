package cn.com.incito.classroom.base;

import java.io.File;

import android.app.Application;
import android.app.ExecRootCmd;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Build;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.exception.AppUncaughtException;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.classroom.vo.LoginResVo;
import cn.com.incito.wisdom.sdk.cache.disk.impl.TotalSizeLimitedDiscCache;
import cn.com.incito.wisdom.sdk.cache.disk.naming.Md5FileNameGenerator;
import cn.com.incito.wisdom.sdk.cache.mem.AbstractMemoryCache;
import cn.com.incito.wisdom.sdk.image.loader.ImageLoader;
import cn.com.incito.wisdom.sdk.image.loader.ImageLoaderConfiguration;
import cn.com.incito.wisdom.sdk.image.loader.assist.LRULimitedMemoryCacheBitmapCache;
import cn.com.incito.wisdom.sdk.image.loader.assist.LRUMemoryCacheBitmapCache;
import cn.com.incito.wisdom.sdk.image.loader.assist.QueueProcessingType;
import cn.com.incito.wisdom.sdk.net.download.BaseImageDownloader;
import cn.com.incito.wisdom.sdk.net.download.SlowNetworkImageDownloader;
import cn.com.incito.wisdom.sdk.openudid.OpenUDIDManager;
import cn.com.incito.wisdom.sdk.utils.StorageUtils;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.umeng.analytics.MobclickAgent;

/**
 * 应用 appication（缓存各类数据） Created by popoy on 2014/7/28.
 */
public class MyApplication extends Application {
	public static final Logger Logger = LoggerFactory.getLogger();
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

	private WifiLock mWifiLock;

	public SharedPreferences getSharedPreferences() {
		return mPrefs;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		PropertyConfigurator.getConfigurator(this).configure();
		sendBroadcast(new Intent("android.intent.action.HIDE_NAVIGATION_BAR"));
		// closeSysScreenLock();
		AppUncaughtException appException = AppUncaughtException.getInstance();
		appException.init(this);
		Thread.setDefaultUncaughtExceptionHandler(appException);
		PowerManager pmManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiLock = manager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "cn.com.incito.classroom");
		wl = pmManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"My Tag");
		wl.acquire();
		mWifiLock.acquire();
		
		mInstance = this;
		mPrefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		initApplication();
		MobclickAgent.openActivityDurationTrack(false);// 禁止友盟的自动统计功能

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

	public void release(){
		mWifiLock.release();
		wl.release();
	}
	public static MyApplication getInstance() {
		return mInstance;
	}

	private void initApplication() {
		//初始化服务端ip
		String ip = mPrefs.getString(Constants.PREFERENCE_IP, "");
		if (ip != null && !ip.trim().equals("")) {
			Constants.setIP(ip);
		}
		
		//初始化本机mac地址
		initMacAddress();
		
		//启动socket和日志服务
		Intent service = new Intent("cn.com.incito.classroom.service.SOCKET_SERVICE");
		startService(service);
		Logger.debug(Utils.getTime()+"MyApplication:"+"socket service started");
		Log.i("MyApplication", "socket service started");

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
			Logger.debug(Utils.getTime()+"LockScreenHandler:"+"是否收到解锁屏信息：" + isLock);
			Log.i("LockScreenHandler", "是否收到解锁屏信息：" + isLock);

			ContentResolver mContentResolver = this.getApplicationContext()
					.getContentResolver();
			ExecRootCmd execRootCmd = new ExecRootCmd();
			if (isLock) {
				MyApplication.getInstance().setLockScreen(isLock);
				boolean ret = Settings.Global.putInt(mContentResolver,
						"disable_powerkey", 1);// 屏蔽电源按钮唤醒功能
				execRootCmd.powerkey();
			} else {
				if (MyApplication.getInstance().isLockScreen()) {
					if(screenOn){
						MyApplication.getInstance().setLockScreen(isLock);
						boolean ret1 = Settings.Global.putInt(mContentResolver,
								"disable_powerkey", 0); // 打开电源按钮唤醒功能
						execRootCmd.powerkey();
					}
					MyApplication.getInstance().setLockScreen(isLock);
					boolean ret1 = Settings.Global.putInt(mContentResolver,
							"disable_powerkey", 0); // 打开电源按钮唤醒功能
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
		Log.i("MyApplication", "广播发出");

	}
}
