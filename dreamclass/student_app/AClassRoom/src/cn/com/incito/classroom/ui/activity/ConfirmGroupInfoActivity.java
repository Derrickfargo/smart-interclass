package cn.com.incito.classroom.ui.activity;

import java.util.Map;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 绑定课桌activity Created by bianshijian on 2014/7/28.
 */
public class ConfirmGroupInfoActivity extends BaseActivity implements
		View.OnClickListener {
	public static final String TAG=ConfirmGroupInfoActivity.class.getSimpleName();
	private ImageButton mBtnDisagree;
	private ImageButton mBtnAgree;
	private ImageView mGroupIcon;
	private TextView mGroupName;
	private TypedArray mGroupIcons;
	private TextView mWaitingStudentTip;
	private String mGroupID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_group_info);
		initView();
		initData();
	}

	private void initView() {
		mBtnAgree = (ImageButton) findViewById(R.id.btn_agree);
		mBtnDisagree = (ImageButton) findViewById(R.id.btn_disagree);
		mGroupIcon = (ImageView) findViewById(R.id.group_icon);
		mGroupName = (TextView) findViewById(R.id.group_name);
		mWaitingStudentTip = (TextView) findViewById(R.id.waiting_other_student);
		mBtnAgree.setOnClickListener(this);
		mBtnDisagree.setOnClickListener(this);
	}

	private void initData() {
		Bundle bundle = getIntent().getExtras();
		Map<String, Object> json = (Map<String, Object>) bundle.get("data");
		mGroupID = json.get("id").toString();
		mGroupName.setText(json.get("name").toString());
		String iconName = json.get("logo").toString();
		mGroupIcons = getResources().obtainTypedArray(R.array.groupIcons);
		String[] iconsName = getResources().getStringArray(
				R.array.groupicons_name);
		Drawable drawable = Utils.getGroupIconByName(mGroupIcons, iconsName,
				iconName);
		mGroupIcon.setImageDrawable(drawable);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		JSONObject json = new JSONObject();
		json.put("id", mGroupID);
		json.put("imei",MyApplication.getInstance().getDeviceId());
		if (id == R.id.btn_agree) {
			json.put("vote", "0");
		} else if (id == R.id.btn_disagree) {
			json.put("vote", "1");
		}
		MessagePacking messagePacking = new MessagePacking(
				Message.MESSAGE_GROUP_VOTE);
		messagePacking.putBodyData(DataType.INT,
				BufferUtils.writeUTFString(json.toJSONString()));
		CoreSocket.getInstance().sendMessage(messagePacking);
		Logger.debug(Utils.getTime()+TAG+":启动分组确认..." + "request:" + json.toJSONString());
		Log.i(TAG,"启动分组确认..." + "request:" + json.toJSONString());
		if (id == R.id.btn_agree) {
			mWaitingStudentTip.setText(R.string.waiting_other_confirm_tip);
		}
		mBtnAgree.setVisibility(View.GONE);
		mBtnDisagree.setVisibility(View.GONE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mGroupIcons.recycle();
	}
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
            return true;  
        } else  
            return super.onKeyDown(keyCode, event);  
    }  
}
