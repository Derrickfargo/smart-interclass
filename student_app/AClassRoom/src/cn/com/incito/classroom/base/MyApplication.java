package cn.com.incito.classroom.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.service.SocketService;
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
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient;
import cn.com.incito.wisdom.sdk.openudid.OpenUDIDManager;
import cn.com.incito.wisdom.sdk.utils.StorageUtils;

import com.baidu.mapapi.SDKInitializer;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

public class MyApplication extends Application {
    private LoginResVo loginResVo;
    public static String deviceId;
    private static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance = null;
    public static final String strKey = "840FFE132BB1749F265E77000ED4A8E17ECEC190";

    private static String IMEI;

    private SharedPreferences mPrefs;

    public SharedPreferences getSharedPreferences() {
        return mPrefs;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
        // 百度地图、定位、导航
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());

        MobclickAgent.openActivityDurationTrack(false);// 禁止友盟的自动统计功能
        mInstance = this;
        mPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        TelephonyManager tm = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = tm.getDeviceId();
//        WisdomCityRestClient.init(getApplicationContext(),
//                new WisdomCityRestClientParameterImpl(getApplicationContext()
//                        .getResources()), null,
//                Constants.REST_CLIENT_LOG_SENABLE_LOGGING
//        );
//        GlobalModel.init(getApplicationContext());
        OpenUDIDManager.sync(this);
        // initEngineManager(this);
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
                                new Md5FileNameGenerator(), 10 * 1024 * 1024)
                )
                .imageDownloader(
                        new SlowNetworkImageDownloader(new BaseImageDownloader(
                                this))
                )
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // .enableLogging() // Not necessary in common
                .build();
        ImageLoader.getInstance().init(config);

        // Log.v("CityCode", GlobalModel.getInst().getCityCode("北京"));
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
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = tm.getDeviceId();
        Intent service = new Intent(this, SocketService.class);
        startService(service);
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

}