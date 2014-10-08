package cn.com.incito.classroom.utils;


import android.net.wifi.WifiInfo;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;


/**
 * Created by popoy on 2014/7/8.
 */

public abstract class WifiAdmin {

    private static final String TAG = "WifiAdmin";

    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    private List<WifiConfiguration> mWifiConfiguration;

    private WifiLock mWifiLock;

    private String mPasswd = "";
    private String mSSID = "";

    private Context mContext = null;

    public WifiAdmin(Context context) {

        mContext = context;

        // 取得WifiManager对象
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();

        Log.v(TAG, "getIpAddress = " + mWifiInfo.getIpAddress());
    }

    // 打开WIFI
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    // 关闭WIFI
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    public abstract Intent myRegisterReceiver(BroadcastReceiver receiver, IntentFilter filter);

    public abstract void myUnregisterReceiver(BroadcastReceiver receiver);

    public abstract void onNotifyWifiConnected();

    public abstract void onNotifyWifiConnectFailed();

    // 添加一个网络并连接
    public void addNetwork(WifiConfiguration wcg) {

        register();

//        WifiApAdmin.closeWifiAp(mContext);

        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b = mWifiManager.enableNetwork(wcgID, true);
    }

    public static final int TYPE_NO_PASSWD = 0x11;
    public static final int TYPE_WEP = 0x12;
    public static final int TYPE_WPA = 0x13;

    /**
     * 添加并连接到热点
     *
     * @param ssid
     * @param passwd
     * @param type
     */
    public void addNetwork(String ssid, String passwd, int type) {
        if (ssid == null || passwd == null || ssid.equals("")) {
            Log.e(TAG, "addNetwork() ## nullpointer error!");
            return;
        }

        if (type != TYPE_NO_PASSWD && type != TYPE_WEP && type != TYPE_WPA) {
            Log.e(TAG, "addNetwork() ## unknown type = " + type);
        }

        stopTimer();
        unRegister();

        addNetwork(createWifiInfo(ssid, passwd, type));
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
                Log.d(TAG, "RSSI changed");

                //有可能是正在获取，或者已经获取了
                Log.d(TAG, " intent is " + WifiManager.RSSI_CHANGED_ACTION);

                if (isWifiContected(mContext) == WIFI_CONNECTED) {
                    stopTimer();
                    onNotifyWifiConnected();
                    unRegister();
                } else if (isWifiContected(mContext) == WIFI_CONNECT_FAILED) {
                    stopTimer();
                    closeWifi();
                    onNotifyWifiConnectFailed();
                    unRegister();
                } else if (isWifiContected(mContext) == WIFI_CONNECTING) {

                }
            }
        }
    };

    private final int STATE_REGISTRING = 0x01;
    private final int STATE_REGISTERED = 0x02;
    private final int STATE_UNREGISTERING = 0x03;
    private final int STATE_UNREGISTERED = 0x04;

    private int mHaveRegister = STATE_UNREGISTERED;

    private synchronized void register() {
        Log.v(TAG, "register() ##mHaveRegister = " + mHaveRegister);

        if (mHaveRegister == STATE_REGISTRING
                || mHaveRegister == STATE_REGISTERED) {
            return;
        }

        mHaveRegister = STATE_REGISTRING;
        myRegisterReceiver(mBroadcastReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        mHaveRegister = STATE_REGISTERED;

        startTimer();
    }

    private synchronized void unRegister() {
        Log.v(TAG, "unRegister() ##mHaveRegister = " + mHaveRegister);

        if (mHaveRegister == STATE_UNREGISTERED
                || mHaveRegister == STATE_UNREGISTERING) {
            return;
        }

        mHaveRegister = STATE_UNREGISTERING;
        myUnregisterReceiver(mBroadcastReceiver);
        mHaveRegister = STATE_UNREGISTERED;
    }

    private Timer mTimer = null;

    private void startTimer() {
        if (mTimer != null) {
            stopTimer();
        }

        mTimer = new Timer(true);
//		mTimer.schedule(mTimerTask, 0, 20 * 1000);// 20s
        mTimer.schedule(mTimerTask, 30 * 1000);
    }

    private TimerTask mTimerTask = new TimerTask() {

        @Override
        public void run() {
            Log.e(TAG, "timer out!");
            onNotifyWifiConnectFailed();
            unRegister();
        }
    };

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void finalize() {
        try {
            super.finalize();
            unRegister();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public WifiConfiguration createWifiInfo(String SSID, String password, int type) {

        Log.v(TAG, "SSID = " + SSID + "## Password = " + password + "## Type = " + type);

        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        // 分为三种情况：1没有密码2用wep加密3用wpa加密
        if (type == TYPE_NO_PASSWD) {// WIFICIPHER_NOPASS
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;

        } else if (type == TYPE_WEP) {  //  WIFICIPHER_WEP
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (type == TYPE_WPA) {   // WIFICIPHER_WPA
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        return config;
    }

    public static final int WIFI_CONNECTED = 0x01;
    public static final int WIFI_CONNECT_FAILED = 0x02;
    public static final int WIFI_CONNECTING = 0x03;

    /**
     * 判断wifi是否连接成功,不是network
     *
     * @param context
     * @return
     */
    public int isWifiContected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        Log.v(TAG, "isConnectedOrConnecting = " + wifiNetworkInfo.isConnectedOrConnecting());
        Log.d(TAG, "wifiNetworkInfo.getDetailedState() = " + wifiNetworkInfo.getDetailedState());
        if (wifiNetworkInfo.getDetailedState() == DetailedState.OBTAINING_IPADDR
                || wifiNetworkInfo.getDetailedState() == DetailedState.CONNECTING) {
            return WIFI_CONNECTING;
        } else if (wifiNetworkInfo.getDetailedState() == DetailedState.CONNECTED) {
            return WIFI_CONNECTED;
        } else {
            Log.d(TAG, "getDetailedState() == " + wifiNetworkInfo.getDetailedState());
            return WIFI_CONNECT_FAILED;
        }
    }

    private WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"") /*&& existingConfig.preSharedKey.equals("\"" + password + "\"")*/) {
                return existingConfig;
            }
        }
        return null;
    }


    // 断开指定ID的网络
    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    // 检查当前WIFI状态
    public int checkState() {
        return mWifiManager.getWifiState();
    }

    // 锁定WifiLock
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    // 解锁WifiLock
    public void releaseWifiLock() {
        // 判断时候锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    // 创建一个WifiLock
    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    // 得到配置好的网络
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfiguration;
    }

    public void startScan() {
        mWifiManager.startScan();
        mWifiList = mWifiManager.getScanResults();
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
    }

    // 得到网络列表
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    // 查看扫描结果
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    // 得到MAC地址
    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    // 得到接入点的BSSID
    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    // 得到IP地址
    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    // 得到连接的ID
    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    // 得到WifiInfo的所有信息包
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    /**
     * Description 指定WIFI是否可以被扫描到，即是否在可用范围内
     *
     * @return true表示可用，否则不可用
     */
    private boolean targetWifiCanScan(String ssid) {
        List<ScanResult> scanResultList = mWifiManager.getScanResults();
        if (scanResultList != null && scanResultList.size() > 0) {
            for (int i = 0; i < scanResultList.size(); i++) {
                ScanResult scanResult = scanResultList.get(i);

                StringBuffer str = new StringBuffer()
                        .append("SSID: " + scanResult.SSID).append("\n")
                        .append("BSSID: " + scanResult.BSSID).append("\n")
                        .append("capabilities: " + scanResult.capabilities).append("\n")
                        .append("level: " + scanResult.level).append("\n")
                        .append("frequency: " + scanResult.frequency);
                Log.i("", str.toString());

                if (scanResult.SSID.equals(ssid)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Description 指定热点是否已配置(注册)
     *
     * @return true表示已注册，否则未注册
     */
    public boolean targetWifiIsConfig(String ssid) {
        for (int i = 0; i < mWifiConfiguration.size(); i++) {
            WifiConfiguration wifiConfiguration = mWifiConfiguration.get(i);

            if (wifiConfiguration.SSID.substring(1, wifiConfiguration.SSID.length() - 1).equals(ssid.trim())) {
//                networkId = wifiConfiguration.networkId;
                return true;
            }
        }
        return false;
    }

    /**
     * Description 连接指定热点
     *
     * @return
     */
    private boolean connectionTargetWifi(int networkId) {
        if (mWifiManager.enableNetwork(networkId, true)) {
            Log.i(TAG, "热点连接成功");
            return true;
        } else {
            Log.d(TAG, "热点连接失败");
            return false;
        }
    }

    /**
     * Description 连接指定热点
     *
     * @return
     */
    public boolean connectionTargetWifiByIndex(int index) {
        // 索引大于配置好的网络索引返回
        if (index > mWifiConfiguration.size()) {
            return false;
        }
        // 连接配置好的指定ID的网络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
        return true;
    }

    public boolean connectionTargetWifiBySSID(String ssid) {
        List<WifiConfiguration> configurations = getConfiguration();
        for (int j = 0; j < configurations.size(); j++) {
            if (ssid.equals(configurations.get(j).SSID.substring(1, configurations.get(j).SSID.length() - 1))) {
                return connectionTargetWifiByIndex(j);
            }
        }
        return false;
    }
}

