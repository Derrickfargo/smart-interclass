package cn.com.incito.classroom.ui.widget;

import java.net.Socket;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.ui.activity.SplashActivity;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.sdk.log.WLog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NetWorkDialog extends AlertDialog {
	private Button button_entrue;
	private Button button_cancle;
	private Context context;
	private TextView title;

	public NetWorkDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public NetWorkDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network_dialog);
		title = (TextView) findViewById(R.id.title);
		title.setText(R.string.msg_dialog_network_nowork);
		button_entrue = (Button) findViewById(R.id.entrue);
		button_entrue.setText(R.string.btn_setting);
		button_entrue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Settings.ACTION_SETTINGS);
				context.startActivity(intent);
				if (CoreSocket.getInstance().isConnected()) {
					dismiss();
				}

			}

		});
		button_cancle = (Button) findViewById(R.id.dismiss);
		button_cancle.setText(R.string.btn_retry);
		button_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CoreSocket.getInstance().restartConnection();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				startMainAct();
				if (CoreSocket.getInstance().isConnected()) {
					dismiss();
				}
			}
		});
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
}
