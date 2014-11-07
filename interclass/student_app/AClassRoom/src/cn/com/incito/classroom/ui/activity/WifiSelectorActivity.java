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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.utils.ApiClient;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.sdk.log.WLog;

import com.alibaba.fastjson.JSONObject;

/**
 * 无线WIFI选择页面.
 * 
 * @author lgm
 */
public class WifiSelectorActivity extends BaseActivity  {
	private ImageButton ib_setting_ip;
	private IpSettingDialogFragment dialog;
	private ListView listView_wifi;
	private ArrayList<IWifiItem> mWifiItems = new ArrayList<IWifiItem>();
	private IWifiListAdapter mWifiListAdapter;
	private String TAG = "WifiSelectorActivity";
	private WifiInfo mWifiInfo;
	private IWifiStatusChangedReceiver mWifiIntentReceiver;
	private INetStatusChangedReceiver mNetStatusChangedReceiver;
	private WifiManager mWifiManager;
	private boolean isAutoInvalidate = false;
	
	// 自动刷新Wifi列表的时长
	private long TIMEINMILLS_REFLUSHLIST = 10 * 1000;
	private IWifiItem mCurrentWifiItem;
	private static final int message_checkNetStatus = 0x11;
	
	// 是否允许出现相同的wifi名称
	private boolean isAllowRepeat = true;
	private int code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifiselector);
		initWifi();
		initViews();
		try {
			PackageManager pm = getPackageManager();
			PackageInfo info = pm.getPackageInfo("cn.com.incito.classroom", 0);
			code = info.versionCode;
		} catch (NameNotFoundException e) {
			ApiClient.uploadErrorLog(e.getMessage());
		}
	}

	/**
	 * 初始化UI
	 */
	private void initViews() {
		ib_setting_ip = (ImageButton) findViewById(R.id.ib_setting_ip);
		ib_setting_ip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showSettingDialog();
			}
		});
		listView_wifi = (ListView) findViewById(R.id.wifiselector_main_listview);
		mWifiListAdapter = new IWifiListAdapter();
		listView_wifi.setAdapter(mWifiListAdapter);
		listView_wifi.setOnItemClickListener(new OnItemClickListener() {
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
		public void handleMessage(android.os.Message msg) {
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
		mWifiManager.setWifiEnabled(true);
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
			mWifiManager.setWifiEnabled(true);
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
			startMain();
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
		NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
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
	/**
	 * 启动主界面
	 */

	private void startMain() {
		Log.i("WifiSelectorActivity", "startMain");
		new Thread() {
			public void run() {
				while (true) {
					Log.i("WifiSelectorActivity", "检查WiFi是否连接 ");
					if (isWifiNetConnected()) {
						MyApplication app = MyApplication.getInstance();
						WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
						WifiInfo info = wifi.getConnectionInfo();
						app.setDeviceId(info.getMacAddress().replace(":", "-"));
						Log.i("WifiSelectorActivity", "WiFi已连接，检查Socket是否连接 ");
						new Thread(CoreSocket.getInstance()).start();//连接socket
						WifiSelectorActivity.this.sleep(1000);
						// //TODO 升级
//						try {
//							JSONObject updateResult = JSONObject
//									.parseObject(ApiClient.updateApk(code));
//							WLog.i(WifiSelectorActivity.class, "版本更新返回信息："
//									+ updateResult);
//							if (updateResult.getInteger("code") == 0) {
//								Version version = JSON.parseObject(updateResult.getJSONObject("data").toJSONString(),Version.class);
//								String url = "http://localhost:8080/api/version/download?id="+ version.getId();
//								UpdateManager mUpdateManager = new UpdateManager(
//										WifiSelectorActivity.this, url);
//								mUpdateManager.checkUpdateInfo();
//							} else {
//							}
//						} catch (AppException e) {
//							ApiClient.uploadErrorLog(e.getMessage());
//							e.printStackTrace();
//						}
						if (!CoreSocket.getInstance().isConnected()) {
							Log.i("WifiSelectorActivity","Socket无连接，开始Socket重连，startMain退出 ");
							CoreSocket.getInstance().disconnect();
							showSetting();
							restartConnector();
							break;
						} else {
							Log.i("WifiSelectorActivity","Socket已连接，开始登陆，startMain退出 ");
							startMainAct();
						}
						break;
					}
					WifiSelectorActivity.this.sleep(3000);
				}
			}
		}.start();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				ib_setting_ip.setVisibility(View.VISIBLE);
			default:
				break;
			}
		}
	};
	private void showSettingDialog() {
		dialog = new IpSettingDialogFragment();
		dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		dialog.show(getSupportFragmentManager(), "");
	}
	/**
	 * 发送登陆请求
	 */
	public void startMainAct() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.deviceId);
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
		messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(jsonObject.toJSONString()));
		CoreSocket.getInstance().sendMessage(messagePacking);
		WLog.i(WifiSelectorActivity.class, "开始判定设备是否绑定..." + "request:" + jsonObject.toJSONString());
	}

	/**
	 * 重新建立连接
	 */
	public void restartConnector() {
		new Thread() {
			public void run() {
				while (Boolean.TRUE) {
					WifiSelectorActivity.this.sleep(3000);
					Log.i("WifiSelectorActivity", "Socket开始重连 ");
					CoreSocket.getInstance().restartConnection();
					WifiSelectorActivity.this.sleep(3000);// 等待1秒后检查连接
					if (!CoreSocket.getInstance().isConnected()) {
						Log.i("WifiSelectorActivity", "Socket未连接 ");
						CoreSocket.getInstance().disconnect();
						continue;
					}
					Log.i("WifiSelectorActivity", "Socket已连接 ");
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
	private void showSetting() {
		android.os.Message message = new android.os.Message();
		message.what = 0;
		mHandler.sendMessage(message);
		// mHandler.sendEmptyMessage(0);
	}

	@Override
	public void onBackPressed() {
		AppManager.getAppManager().AppExit(this);
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
	