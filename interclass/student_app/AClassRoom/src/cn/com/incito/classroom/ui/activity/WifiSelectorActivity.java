package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.common.utils.ToastHelper;

/**
 * 无线WIFI选择页面.
 * 
 * @author lgm
 */
public class WifiSelectorActivity extends BaseActivity {

	private ListView gridView_wifi;
	private ArrayList<IWifiItem> mWifiItems = new ArrayList<IWifiItem>();
	private IWifiListAdapter mWifiListAdapter;
	private String TAG = "WifiSelectorActivity";
	private WifiInfo mWifiInfo;
	private IWifiStatusChangedReceiver mWifiIntentReceiver;
	private INetStatusChangedReceiver mNetStatusChangedReceiver;
	private WifiManager mWifiManager;
	private Handler mHandler;
	private boolean isAutoInvalidate = false;
	
	// 自动刷新Wifi列表的时长
	private long TIMEINMILLS_REFLUSHLIST = 10 * 1000;
	private IWifiItem mCurrentWifiItem;
	private static final int message_checkNetStatus = 0x11;
	
	// 是否允许出现相同的wifi名称
	private boolean isAllowRepeat = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifiselector);
		initWifi();
		initViews();
	}

	/**
	 * 初始化UI
	 */
	private void initViews() {
		gridView_wifi = (ListView) findViewById(R.id.wifiselector_main_listview);
		mWifiListAdapter = new IWifiListAdapter();
		
		
		gridView_wifi.setAdapter(mWifiListAdapter);


		gridView_wifi.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mCurrentWifiItem = mWifiItems.get(arg2);
				switch (mCurrentWifiItem.wifiType) {
				case WIFITYPE_NORMAL:
					mWifiInfo = mWifiManager.getConnectionInfo();
					final EditText passwordEdit = new EditText(
							WifiSelectorActivity.this);
					// 显示输入密码对话框
					new AlertDialog.Builder(WifiSelectorActivity.this)
							.setTitle("请输入密码")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setView(passwordEdit)
							.setPositiveButton("确定", new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									connectToWifi(mCurrentWifiItem,
											passwordEdit.getText().toString(),
											IWifiSecurityType.SECURITY_TYPE_WPA);
									dialog.dismiss();
								}
							}).setNegativeButton("取消", new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();

					break;
				case WIFITYPE_3G:
					if (isMobileNetConnected()) {
					} else {
						Toast.makeText(getBaseContext(), "当前手机网络不可用",
								Toast.LENGTH_LONG).show();
					}
					break;

				default:
					break;
				}
			}
		});

		isAutoInvalidate = true;
		mHandler = new IFlushHander();
		mHandler.sendMessageDelayed(mHandler.obtainMessage(), 500);

	}

	/**
	 * 刷新页面
	 * 
	 */
	@SuppressLint("HandlerLeak")
	private class IFlushHander extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			default:
				invalidateNetListData();
				mWifiListAdapter.notifyDataSetChanged();
				if (isAutoInvalidate) {
					mHandler.sendMessageDelayed(mHandler.obtainMessage(),
							TIMEINMILLS_REFLUSHLIST);
				}
				break;
			}

		}

	}

	/**
	 * 初始化WIFI
	 */
	private void initWifi() {
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		// 监听wifi状态变化
		IntentFilter mWifiIntentFilter = new IntentFilter();
		mWifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		mWifiIntentReceiver = new IWifiStatusChangedReceiver();
		registerReceiver(mWifiIntentReceiver, mWifiIntentFilter);

		/*
		 * //监听网络连接状态的变化 IntentFilter intentFilter_netConnect = new
		 * IntentFilter();
		 * intentFilter_netConnect.addAction(ConnectivityManager.
		 * CONNECTIVITY_ACTION); mNetStatusChangedReceiver = new
		 * INetStatusChangedReceiver();
		 * registerReceiver(mNetStatusChangedReceiver, intentFilter_netConnect);
		 */
	}

	/**
	 * 刷新wifi列表数据
	 */
	private void invalidateNetListData() {
		mWifiManager.startScan();
		mWifiItems.clear();
		if (mWifiManager.isWifiEnabled()) {
			ArrayList<ScanResult> scanResults = (ArrayList<ScanResult>) mWifiManager
					.getScanResults();
			// TODO 是否应该根据SSID进行过滤
			IWifiItem wifiItem;
			ScanResult scanResult;
			Collections.sort(scanResults, new Comparator<ScanResult>() {

				@Override
				public int compare(ScanResult arg0, ScanResult arg1) {
					return Math.abs(arg0.level) - Math.abs(arg1.level);
				}

			});

			for (int i = 0; i < scanResults.size(); i++) {
				wifiItem = new IWifiItem();
				scanResult = scanResults.get(i);
				wifiItem.setScanResult(scanResult);
				wifiItem.setWifiName(scanResult.SSID);
				wifiItem.setWifiType(IWifiType.WIFITYPE_NORMAL);
				if (isAllowRepeat)
					mWifiItems.add(wifiItem);
				else {
					if (!mWifiItems.contains(wifiItem))
						mWifiItems.add(wifiItem);
				}
			}

		} else {
			mWifiItems.clear();
		}

		// 手机本机网络
		IWifiItem wifiItem = new IWifiItem();
		wifiItem.wifiName = "本机3G/2G网络";
		wifiItem.wifiType = IWifiType.WIFITYPE_3G;
		mWifiItems.add(wifiItem);
	}

	/**
	 * 当wifi状态发生变化时触发
	 * 
	 * @author lgm
	 * 
	 */
	private class IWifiStatusChangedReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {

			switch (intent.getIntExtra("wifi_state", 0)) {
			case WifiManager.WIFI_STATE_DISABLING:
				break;
			case WifiManager.WIFI_STATE_DISABLED:
				Log.e(TAG, "关闭了wifi");
				break;
			case WifiManager.WIFI_STATE_ENABLING:
				break;
			case WifiManager.WIFI_STATE_ENABLED:
				Log.e(TAG, "打开了wifi");
				break;
			case WifiManager.WIFI_STATE_UNKNOWN:
				break;
			}

			mHandler.sendMessageDelayed(mHandler.obtainMessage(), 1000);
		}

	}

	/**
	 * 监听网络状态发生变化
	 * 
	 * @author lgm
	 * 
	 */
	private class INetStatusChangedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectMgr
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectMgr
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (wifiNetInfo.isConnected()) {
				Toast.makeText(getBaseContext(), "已经连接", Toast.LENGTH_LONG)
						.show();
			}
			if (!wifiNetInfo.isConnectedOrConnecting()) {
				Toast.makeText(getBaseContext(), "WIFI已经断了", Toast.LENGTH_LONG)
						.show();
			}

		}

	}

	private class IWifiListAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return mWifiItems.size();
		}

		@Override
		public Object getItem(int arg0) {

			return mWifiItems.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup viewGroup) {
			IWifiItem wifiItem = mWifiItems.get(position);
			IWifiHolder mWifiHolder;
			if (contentView == null) {
				mWifiHolder = new IWifiHolder();
				contentView = LayoutInflater.from(WifiSelectorActivity.this)
						.inflate(R.layout.wifiselector_gridview_item, null);
				mWifiHolder.textView_name = (TextView) contentView
						.findViewById(R.id.wifiselector_gridview_item_name);
				contentView.setTag(mWifiHolder);
			} else {
				mWifiHolder = (IWifiHolder) contentView.getTag();
			}

			switch (wifiItem.getWifiType()) {
			case WIFITYPE_CLASS:

				break;
			case WIFITYPE_3G:
				mWifiHolder.textView_name.setText(wifiItem.getWifiName());
				mWifiHolder.textView_name.setTextColor(Color.BLACK);

				break;

			case WIFITYPE_NORMAL:
				mWifiHolder.textView_name
						.setText(wifiItem.getScanResult().SSID);
				mWifiHolder.textView_name.setTextColor(Color.BLACK);
				break;

			default:
				break;
			}

			return contentView;
		}

	}

	private class IWifiHolder {

		TextView textView_name;

	}

	/**
	 * WIFI显示时的对象
	 * 
	 * @author lgm
	 * 
	 */
	private class IWifiItem {

		// wifi名称
		private String wifiName = "";
		// wifi类型
		private IWifiType wifiType = IWifiType.WIFITYPE_NORMAL;
		// wifi所对应的对象
		private ScanResult scanResult;

		public String getWifiName() {
			return wifiName;
		}

		public void setWifiName(String wifiName) {
			this.wifiName = wifiName;
		}

		public IWifiType getWifiType() {
			return wifiType;
		}

		public void setWifiType(IWifiType wifiType) {
			this.wifiType = wifiType;
		}

		public ScanResult getScanResult() {
			return scanResult;
		}

		public void setScanResult(ScanResult scanResult) {
			this.scanResult = scanResult;
		}

		@Override
		public boolean equals(Object object) {
			if (object instanceof IWifiItem) {
				return ((IWifiItem) object).getScanResult().SSID.equals(this
						.getScanResult().SSID);
			} else
				return false;
		}

	}

	/**
	 * WIFI的分类
	 * 
	 * @author lgm
	 * 
	 */
	private enum IWifiType {
		/**
		 * 课堂类型
		 */
		WIFITYPE_CLASS,
		/**
		 * 3G网络
		 */
		WIFITYPE_3G,
		/**
		 * 家庭或者其他的网络类型
		 */
		WIFITYPE_NORMAL,
		/**
		 * 未知类型
		 */
		WIFITYPE_NONE;

	}

	/**
	 * wifi安全加密方式
	 * 
	 * @author lgm
	 * 
	 */
	private enum IWifiSecurityType {
		/**
		 * 无加密
		 */
		SECURITY_TYPE_NOPASS,
		/**
		 * wep加密方式
		 */
		SECURITY_TYPE_WEP,
		/**
		 * wap加密方式
		 */
		SECURITY_TYPE_WPA;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mWifiIntentReceiver);
	}

	@Override
	protected void onStart() {
		isAutoInvalidate = true;

		// 强制隐藏键盘
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		isAutoInvalidate = false;
	}

	/**
	 * 连接到指定的Wifi
	 * 
	 * @param wifiItem
	 */
	private void connectToWifi(IWifiItem wifiItem, String password,
			IWifiSecurityType securityType) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + wifiItem.getScanResult().SSID + "\"";

		WifiConfiguration tempConfig = isExistWifi(wifiItem.getScanResult().SSID);
		if (tempConfig != null) {
			mWifiManager.removeNetwork(tempConfig.networkId);
		}

		switch (securityType) {
		case SECURITY_TYPE_NOPASS:
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
			break;
		case SECURITY_TYPE_WEP:
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
			break;

		case SECURITY_TYPE_WPA:
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
			break;
		default:
			break;
		}
		int wcgID = mWifiManager.addNetwork(config);
		if (mWifiManager.enableNetwork(wcgID, true)) {
			Intent intent = new Intent(this, SelectGroupActivity.class);
			startActivity(intent);
		} else {
			ToastHelper.showCustomToast(this, R.string.wifi_password_error);
		}

	}

	/**
	 * 检测WIFI网络是否已经连接
	 * 
	 * @return
	 */
	private boolean isWifiNetConnected() {
		boolean isConnected = false;
		ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetInfo = connectMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		isConnected = wifiNetInfo.isConnected();
		/*
		 * if(wifiNetInfo.isConnected()){ Toast.makeText(getBaseContext(),
		 * "已经连接", Toast.LENGTH_LONG).show(); }
		 * if(!wifiNetInfo.isConnectedOrConnecting()){
		 * Toast.makeText(getBaseContext(), "WIFI已经断了",
		 * Toast.LENGTH_LONG).show(); }
		 */
		return isConnected;
	}

	/**
	 * 检测手机网络是否已经连接
	 * 
	 * @return
	 */
	private boolean isMobileNetConnected() {
		boolean isConnected = false;
		ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		isConnected = mobNetInfo.isConnected();
		/*
		 * if(mobNetInfo.isConnected()){ Toast.makeText(getBaseContext(),
		 * "手机网络已经连接", Toast.LENGTH_LONG).show(); }
		 * if(!mobNetInfo.isConnectedOrConnecting()){
		 * Toast.makeText(getBaseContext(), "手机网络已经断了",
		 * Toast.LENGTH_LONG).show(); }
		 */
		return isConnected;

	}

	/**
	 * 是否已经存在指定的WIFI
	 * 
	 * @param SSID
	 * @return
	 */
	private WifiConfiguration isExistWifi(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager
				.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	// /**
	// * 跳转到模块选择页面
	// */
	// private void skipToMoudle(){
	// Intent intent = new Intent(this,MoudleSelectorActivity.class);
	// startActivity(intent);
	// finish();
	//
	// }
}
	