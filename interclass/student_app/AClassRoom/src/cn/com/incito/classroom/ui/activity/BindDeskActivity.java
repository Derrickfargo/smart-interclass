package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.DeskNumberAdapter;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.ui.widget.MyAlertDialog;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.sdk.log.WLog;

import com.alibaba.fastjson.JSONObject;

/**
 * 绑定课桌activity Created by popoy on 2014/7/28.
 */
public class BindDeskActivity extends BaseActivity {

	private GridView gv_desk_number;

	private ImageButton btn_join;

	protected long mExitTime;

	DeskNumberAdapter deskNumberAdapter;

	private int curentDesk;


	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setContentView(R.layout.binddesk);
		UIHelper.getInstance().setBindDeskActivity(this);
		initViews();
		initEvent();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 12; i++) {
			list.add(String.valueOf(i + 1));
		}
		deskNumberAdapter = new DeskNumberAdapter(this, list);
		gv_desk_number.setAdapter(deskNumberAdapter);
		gv_desk_number.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				curentDesk = i + 1;
				deskNumberAdapter.setSelectPos(i);
				deskNumberAdapter.notifyDataSetChanged();
			}
		});

	}

	@Override
	public void onBackPressed() {
		MyAlertDialog dialog=new MyAlertDialog(this);
		dialog.show();
//		if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
//			Toast.makeText(this, R.string.toast_quit_app, Toast.LENGTH_SHORT).show();
//			mExitTime = System.currentTimeMillis();// 更新mExitTime
//		} else {
//			AppManager.getAppManager().AppExit(this);
//		}
	}

	private void initViews() {
		gv_desk_number = (GridView) findViewById(R.id.gv_desk_number);
		btn_join = (ImageButton) findViewById(R.id.btn_bind);
	}

	private void initEvent() {
		btn_join.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (curentDesk < 1) {
					ToastHelper.showCustomToast(BindDeskActivity.this, R.string.toast_choose_atleast_one_desk);
					return;
				}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("imei", MyApplication.deviceId);
				jsonObject.put("number", curentDesk);
				MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_DEVICE_BIND);
				messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
				CoreSocket.getInstance().sendMessage(messagePacking);
				WLog.i(BindDeskActivity.class, "启动课桌绑定..." + "request:" + jsonObject.toJSONString());
			}

		});
	}

	public void showErrorDialog() {
		android.os.Message message = new android.os.Message();
		message.what = 1;
		mHandler.sendMessage(message);
	}
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 登陆
			case 1: {
				ToastHelper.showCustomToast(BindDeskActivity.this, "一个桌子最多绑定" + Constants.PAD_PER_DESK + "个pad");
			}
				break;
			}
		}
	};
}
