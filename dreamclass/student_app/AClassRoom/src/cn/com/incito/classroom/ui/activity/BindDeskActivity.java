package cn.com.incito.classroom.ui.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.DeskNumberAdapter;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.util.SendMessageUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * 绑定课桌activity Created by popoy on 2014/7/28.
 */
public class BindDeskActivity extends BaseActivity {

	private GridView gv_desk_number;
	private ImageButton btn_join;
	protected long mExitTime;
	private DeskNumberAdapter deskNumberAdapter;
	private int curentDesk;
	private BindDeskActivityHandler mHandler;

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setContentView(R.layout.binddesk);
		mHandler = new BindDeskActivityHandler(this);
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
				jsonObject.put("imei", MyApplication.getInstance().getDeviceId());
				jsonObject.put("number", curentDesk);
				SendMessageUtil.sendBindDesk(jsonObject.toJSONString());
			}

		});
	}

	public void showErrorDialog() {
		android.os.Message message = new android.os.Message();
		message.what = 1;
		mHandler.sendMessage(message);
	}
	
	private static class BindDeskActivityHandler extends Handler{
		
		private WeakReference<BindDeskActivity> weakReference;
		
		public BindDeskActivityHandler(BindDeskActivity bindDeskActivity) {
			this.weakReference = new WeakReference<BindDeskActivity>(bindDeskActivity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			BindDeskActivity bindDeskActivity = weakReference.get();
			switch (msg.what){
			case 1: 
				bindDeskActivity.showToast("一个桌子最多绑定" + Constants.PAD_PER_DESK + "个pad");
				break;
			}
		}
	}
}
