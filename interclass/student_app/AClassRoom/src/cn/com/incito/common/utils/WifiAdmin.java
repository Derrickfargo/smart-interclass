package cn.com.incito.common.utils;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiAdmin {

	private WifiManager wifiManager;
	private List<ScanResult> scanResults;
	private WifiInfo wifiInfo;
	private static WifiAdmin wifiAdmin;

	public enum WifiCipherType {
		WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
	}
	
	public static WifiAdmin getWifiAdmin(Context context){
		if(wifiAdmin == null){
			return new WifiAdmin(context);
		}
		return wifiAdmin;
	}

	private WifiAdmin(Context context) {
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiInfo = wifiManager.getConnectionInfo();
	}
	
	
	public void disconnectWifi(){
		wifiInfo = wifiManager.getConnectionInfo();
		if(wifiInfo != null){
			wifiManager.disableNetwork(wifiInfo.getNetworkId());  
			wifiManager.disconnect();  
		}
	}
	
	
	/**
	 * @param ssid
	 * @param passWord
	 * @param wifiCipherType
	 * @return
	 *  开始连接wifi并且使其他的wifi都失效
	 */
	public boolean connectWifi(String ssid, String passWord,
			WifiCipherType wifiCipherType) {
		WifiConfiguration wifiConfig = this.CreateWifiInfo(ssid, passWord,wifiCipherType);
		if (wifiConfig == null) {
			return false;
		}
		int netId = wifiManager.addNetwork(wifiConfig);
		return wifiManager.enableNetwork(netId, true);
	}

	// 得到连接的ID
	public int getNetWordId() {
		return (wifiInfo == null) ? 0 : wifiInfo.getNetworkId();
	}

	// 断开指定ID的网络
	public void disConnectionWifi(int netId) {
		wifiManager.disableNetwork(netId);
		wifiManager.disconnect();
	}

	private WifiConfiguration CreateWifiInfo(String ssid, String passWord,
			WifiCipherType wifiCipherType) {

		WifiConfiguration wifiConfiguration = new WifiConfiguration();
		wifiConfiguration.allowedAuthAlgorithms.clear();
		wifiConfiguration.allowedGroupCiphers.clear();
		wifiConfiguration.allowedKeyManagement.clear();
		wifiConfiguration.allowedPairwiseCiphers.clear();
		wifiConfiguration.allowedProtocols.clear();
		wifiConfiguration.SSID = "\"" + ssid + "\"";
		
		if (wifiCipherType == WifiCipherType.WIFICIPHER_NOPASS) {
			wifiConfiguration.wepKeys[0] = "";
			wifiConfiguration.allowedKeyManagement
					.set(WifiConfiguration.KeyMgmt.NONE);
			wifiConfiguration.wepTxKeyIndex = 0;
		}else if (wifiCipherType == WifiCipherType.WIFICIPHER_WEP) {
			wifiConfiguration.hiddenSSID = true;
			wifiConfiguration.wepKeys[0] = "\"" + passWord + "\"";
			wifiConfiguration.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);
			wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			wifiConfiguration.allowedGroupCiphers
					.set(WifiConfiguration.GroupCipher.WEP104);
			wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			wifiConfiguration.wepTxKeyIndex = 0;
		}else if (wifiCipherType == WifiCipherType.WIFICIPHER_WPA) {
			wifiConfiguration.preSharedKey = "\"" + passWord + "\"";
			wifiConfiguration.hiddenSSID = true;
			wifiConfiguration.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.OPEN);
			wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			wifiConfiguration.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			wifiConfiguration.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
		} else {
			return null;
		}
		return wifiConfiguration;
	}

	public WifiInfo getWifiInfo() {
		return wifiManager.getConnectionInfo();
	}

	private void openWifi() {
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
	}

	public void closeWifi() {
		if (wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(false);
		}
	}

	private void startScan() {
		openWifi();
		wifiManager.startScan();
		// 得到扫描结果
		scanResults = wifiManager.getScanResults();
	}

	public List<ScanResult> getScanresult() {
		startScan();
		return this.scanResults;
	}

}
