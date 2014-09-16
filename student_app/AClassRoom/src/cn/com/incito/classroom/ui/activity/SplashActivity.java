package cn.com.incito.classroom.ui.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
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
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.sdk.log.WLog;

import com.alibaba.fastjson.JSONObject;

/**
 * 用户其启动界面时候的一个启动页面完成一些初始化工作 Created by popoy on 2014/7/28.
 */

public class SplashActivity extends BaseActivity {

	private TextView tv_loading_msg;
	private ImageButton ib_setting_ip;
	 private boolean Flag;
	 private IpSettingDialogFragment dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.splash, null);
		setContentView(view);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(Flag){
			Flag=false;
			startMain();
		}
	}

	/**
	 * 启动主界面
	 */

	private void startMain() {
		tv_loading_msg.setText(R.string.loading_msg);
		new Thread() {
			public void run() {
				while (true) {
					if (checkWifi()) {
						MyApplication app = MyApplication.getInstance();
						WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
						WifiInfo info = wifi.getConnectionInfo();
						app.setDeviceId(info.getMacAddress().replace(":", "-"));
						if (!CoreSocket.getInstance().isConnected()) {
							showSetting();
							restartConnector();
						} else {
							startMainAct();
						}
						break;
					}
					SplashActivity.this.sleep(3000);
				}
			}
		}.start();
		
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			ib_setting_ip.setVisibility(View.VISIBLE);
		}
	};
	
	private void showSetting() {
		mHandler.sendEmptyMessage(0);
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
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.deviceId);
		MessagePacking messagePacking = new MessagePacking(
				Message.MESSAGE_DEVICE_HAS_BIND);
		messagePacking.putBodyData(DataType.INT,
				BufferUtils.writeUTFString(jsonObject.toJSONString()));
		CoreSocket.getInstance().sendMessage(messagePacking);
		WLog.i(SplashActivity.class,
				"开始判定设备是否绑定..." + "request:" + jsonObject.toJSONString());
	}


	/**
	 * 重新建立连接
	 */
	public void restartConnector() {
		new Thread() {
			public void run() {
				while (Boolean.TRUE) {
					CoreSocket.getInstance().restartConnection();
					SplashActivity.this.sleep(1000);// 等待1秒后检查连接
					if (!CoreSocket.getInstance().isConnected()) {
						CoreSocket.getInstance().disconnect();
						SplashActivity.this.sleep(3000);
						continue;
					} else {
						if (dialog != null) {
							dialog.dismiss();
						}
						startMainAct();
					}
					break;
				}
			}
		}.start();
	}

	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
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
		Flag=true;
		return false;
	}

}
