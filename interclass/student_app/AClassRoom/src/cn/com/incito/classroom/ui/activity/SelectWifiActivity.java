package cn.com.incito.classroom.ui.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.WifiSelectAdapter;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.common.utils.WifiAdmin;
import cn.com.incito.common.utils.WifiAdmin.WifiCipherType;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.uicomp.widget.dialog.ProgressiveDialog;

import com.alibaba.fastjson.JSONObject;

public class SelectWifiActivity extends BaseActivity {

	private IpSettingDialogFragment ipSettingDialogFragment;
	private ListView wifi_list;
	private ImageButton ib_setting_ip;
	private WifiSelectAdapter wifiselectAdapter;
	private WifiAdmin wifiAdmin;
	private ProgressiveDialog progressDialog;
	private TextView wifiText;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

	private Timer connectWifiTimer;
	private TimerTask connectWifiTimerTask;

	private Timer updateWifiTimer;
	private TimerTask updateWifiTimerTask;;

	private Timer connectServerTimer;
	private TimerTask connectServerTimerTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifiselector);
		UIHelper.getInstance().setSelectorWifiActivity(this);
		sharedPreferences = getPreferences(MODE_PRIVATE);
		editor = sharedPreferences.edit();
		initView();
	}

	// 初始化组件
	private void initView() {
		wifi_list = (ListView) findViewById(R.id.wifiselector_main_listview);
		ib_setting_ip = (ImageButton) findViewById(R.id.ib_setting_ip);
		wifiAdmin = WifiAdmin.getWifiAdmin(this);
		wifiText = new TextView(this);
		wifiText.setText("正在扫描请稍后...");

		wifi_list.setEmptyView(wifiText);
		wifiselectAdapter = new WifiSelectAdapter(this,
				wifiAdmin.getScanresult());
		wifi_list.setAdapter(wifiselectAdapter);

		progressDialog = new ProgressiveDialog(this);

		progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						if (connectServerTimer != null) {
							connectServerTimer.cancel();
						}
						if (connectWifiTimer != null) {
							connectWifiTimer.cancel();
						}
						CoreSocket.getInstance().stopConnection();
					}
				});

		// 更新wifi列表
		updateWifi();

		// 设置ip按钮
		ib_setting_ip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (connectServerTimer != null) {
					connectServerTimer.cancel();
				}
				if (connectWifiTimer != null) {
					connectWifiTimer.cancel();
				}
				if (ipSettingDialogFragment == null) {
					ipSettingDialogFragment = new IpSettingDialogFragment();
					ipSettingDialogFragment.setStyle(
							DialogFragment.STYLE_NO_TITLE, 0);
				}
				ipSettingDialogFragment.show(getSupportFragmentManager(), "");
			}
		});

		// 点击wifi
		wifi_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (connectServerTimer != null) {
					connectServerTimer.cancel();
				}
				if (connectWifiTimer != null) {
					connectWifiTimer.cancel();
				}

				final ScanResult scanResult = (ScanResult) wifiselectAdapter
						.getItem(position);
				WifiInfo wifiInfo = wifiAdmin.getWifiInfo();

				if (isWifiNetConnected()&& wifiInfo.getSSID().substring(1, wifiInfo.getSSID().length() - 1).equals(scanResult.SSID)) {
					connectServer();
				} else {
					if (sharedPreferences.contains(scanResult.BSSID)) {
						connecteWifi(scanResult, sharedPreferences.getString(
								scanResult.BSSID, ""));
					} else {
						final EditText passwordEdit = new EditText(
								SelectWifiActivity.this);

						passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());

						// 显示输入密码对话框
						new AlertDialog.Builder(SelectWifiActivity.this)
								.setTitle("请输入密码")
								.setIcon(android.R.drawable.ic_dialog_info)
								.setView(passwordEdit)
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,int which) {
												dialog.dismiss();
											}
										})
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,int which) {
												connecteWifi(scanResult,passwordEdit.getText().toString());
												dialog.dismiss();
											}
										}).show();
					}
				}
			}
		});
	}

	// 连接教师端
	private void connectServer() {

		if (connectWifiTimer != null) {
			connectWifiTimer.cancel();
		}

		if (connectServerTimer != null) {
			connectServerTimer.cancel();
		}

		if (progressDialog == null) {
			progressDialog = new ProgressiveDialog(this);
		}

		progressDialog.setMessage(R.string.going_classroom);
		progressDialog.show();

		if (isWifiNetConnected()) {
			MyApplication app = MyApplication.getInstance();
			WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			app.setDeviceId(info.getMacAddress().replace(":", "-"));
			Log.i("SelectWifiActivity", "WiFi已连接，检查Socket是否连接 ");

			new Thread(CoreSocket.getInstance()).start();// 连接socket

			connectServerTimer = new Timer();
			connectServerTimerTask = new TimerTask() {
				@Override
				public void run() {
					if (!CoreSocket.getInstance().isConnected()) {
						Log.i("SelectWifiActivity", "Socket无连接,startMain退出 ");
						handler.sendEmptyMessage(3);// 重连socket
					} else {
						handler.sendEmptyMessage(4);// 开始登录
					}
				}
			};
			connectServerTimer.schedule(connectServerTimerTask, 3 * 1000);
		}
	}

	// 连接wifi
	private void connecteWifi(final ScanResult scanResult, final String password) {

		if (progressDialog == null) {
			progressDialog = new ProgressiveDialog(this);
		}
		progressDialog.setMessage(R.string.connect_wifi);

		wifiAdmin.releasWifiLock();
		connectWifiTimer = new Timer();
		connectWifiTimerTask = new TimerTask() {

			@Override
			public void run() {
				if (isWifiNetConnected()) {
					editor.putString(scanResult.BSSID, password);
					editor.commit();
					handler.sendEmptyMessage(1);// wifi已经连接s
				} else {
					editor.remove(scanResult.BSSID);
					editor.commit();
					handler.sendEmptyMessage(2);// wifi连接错误
				}
			}
		};

		if (isWifiNetConnected()) {
			wifiAdmin.disconnectWifi();
			if (wifiAdmin.connectWifi(scanResult.SSID, password,WifiCipherType.WIFICIPHER_WPA)) {
				progressDialog.show();
				connectWifiTimer.schedule(connectWifiTimerTask, 10 * 1000);
			} else {
				ToastHelper.showCustomToast(this, "网络错误...");
			}
		}else{
			if (wifiAdmin.connectWifi(scanResult.SSID, password,WifiCipherType.WIFICIPHER_WPA)) {
				progressDialog.show();
				connectWifiTimer.schedule(connectWifiTimerTask, 8 * 1000);
			} else {
				ToastHelper.showCustomToast(this, "网络错误...");
			}
		}
		

	}

	// 5s钟刷新一次wifi列表
	private void updateWifi() {
		updateWifiTimer = new Timer();
		updateWifiTimerTask = new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		};
		updateWifiTimer.schedule(updateWifiTimerTask, 0, 5 * 1000);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				wifiselectAdapter.setScanresult(wifiAdmin.getScanresult());
				break;
			case 1:
				progressDialog.setMessage(R.string.going_classroom);
				connectServer();
				break;
			case 2:
				progressDialog.dismiss();
				ToastHelper.showCustomToast(SelectWifiActivity.this,"wifi连接失败...");
				break;
			case 3:
				progressDialog.dismiss();
				CoreSocket.getInstance().disconnect();
				ToastHelper.showCustomToast(getBaseContext(), "教室连接失败请重新连接...");
				// connectServer();
				break;
			case 4:
				progressDialog.setMessage(R.string.login);
				MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ "socket建立成功开始进行登录");
				startMainAct();
				break;
			case 11:
				progressDialog.dismiss();
				CoreSocket.getInstance().stopConnection();
				ToastHelper.showCustomToast(SelectWifiActivity.this,"当前设备没有注册学生,请联系老师注册!");
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 发送登陆请求
	 */
	public void startMainAct() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.deviceId);
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
		messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(jsonObject.toJSONString()));
		CoreSocket.getInstance().sendMessage(messagePacking);
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":SelectWifiActivity:开始判定设备是否绑定...request：" + jsonObject.toJSONString());
	}

	public void showToast() {
		android.os.Message message = new android.os.Message();
		message.what = 11;
		handler.sendMessage(message);
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
		return isConnected;
	}

	@Override
	protected void onDestroy() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		if(connectServerTimer != null){
			connectServerTimer.cancel();
		}
		if(connectWifiTimer != null){
			connectWifiTimer.cancel();
		}
		if(updateWifiTimer != null){
			updateWifiTimer.cancel();
		}
		super.onDestroy();
	}
}
