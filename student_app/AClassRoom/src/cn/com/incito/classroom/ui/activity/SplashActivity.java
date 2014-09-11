package cn.com.incito.classroom.ui.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.Settings;
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
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.service.SocketService;
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

	private ServiceConnectReceiver serviceConnectReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		serviceConnectReceiver = new ServiceConnectReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SocketService.NETWORK_RECEIVER);
		registerReceiver(serviceConnectReceiver, intentFilter);
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
		// 注销广播
		unregisterReceiver(serviceConnectReceiver);
		super.onDestroy();
	}

	/**
	 * service连接广播接收器
	 */
	public class ServiceConnectReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() == SocketService.NETWORK_RECEIVER) {
				// 拿到进度，更新UI
				String exception = intent.getStringExtra("exception");
				// tv_loading_msg.setText(R.string.loading_network_disconnect);
				showErrorNetDialog();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Flag) {
			Flag = false;
			CoreSocket.getInstance().restartConnection();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			startMainAct();
		}

	}

	/**
	 * 启动主界面
	 */

	private void startMain() {
		tv_loading_msg.setText(R.string.loading_msg);

		if ("".equals(MyApplication.getInstance().getSharedPreferences()
				.getString(Constants.PREFERENCE_IP, ""))) {
			showSettingDialog();
		}
		if (!CoreSocket.getInstance().isConnected()) {
			ib_setting_ip.setVisibility(View.VISIBLE);
			// showErrorNetDialog();
		} else {
			startMainAct();
		}
	}

	@Override
	public void onBackPressed() {
		AppManager.getAppManager().AppExit(this);
	}

	private void showSettingDialog() {
		IpSettingDialogFragment dialog = new IpSettingDialogFragment();
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

	public void showErrorNetDialog() {
		new AlertDialog.Builder(this).setCancelable(false).setTitle("网络设置")
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Settings.ACTION_SETTINGS);
						startActivity(intent);
						Flag = true;
						dialog.dismiss();
					}
				})
				.setNegativeButton("重试", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						CoreSocket.getInstance().restartConnection();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						startMainAct();
						dialog.dismiss();
					}
				}).show();
	}

}
