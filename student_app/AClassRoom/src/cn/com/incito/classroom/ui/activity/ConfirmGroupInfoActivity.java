package cn.com.incito.classroom.ui.activity;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.ui.widget.HorizontalListView;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.classroom.adapter.HorizontalListViewAdapter;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;


public class ConfirmGroupInfoActivity extends BaseActivity implements View.OnClickListener{

	private static final String TAG = "ConfirmGroupInfoActivity";

	private ImageButton mBtnDisagree = null;
	private ImageButton mBtnAgree = null;
	private ImageView mGroupIcon = null;
	private TextView mGroupName = null;
	private TypedArray mGroupIcons = null;
	private TextView mWaitingStudentTip = null;
	private String mGroupID = null;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_group_info);
		initView();
		initData();
	}
	
	private void initView(){
		mBtnAgree = (ImageButton)findViewById(R.id.btn_agree);
		mBtnDisagree = (ImageButton)findViewById(R.id.btn_disagree);
		mGroupIcon = (ImageView)findViewById(R.id.group_icon);
		mGroupName = (TextView)findViewById(R.id.group_name);
		mWaitingStudentTip = (TextView)findViewById(R.id.waiting_other_student);
		mBtnAgree.setOnClickListener(this);
		mBtnDisagree.setOnClickListener(this);
	}
	
	private void initData(){
		Bundle bundle = getIntent().getExtras();
		Map<String,Object> json = (Map<String,Object>) bundle.get("data");
		mGroupID = json.get("id").toString();
		mGroupName.setText(json.get("name").toString());
		String iconName = json.get("logo").toString();
		mGroupIcons = getResources().obtainTypedArray(R.array.groupIcons);
		String[] iconsName = getResources().getStringArray(R.array.groupicons_name);
		Drawable drawable = Utils.getGroupIconByName(mGroupIcons, iconsName, iconName);
		mGroupIcon.setImageDrawable(drawable);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		JSONObject json = new JSONObject();
		json.put("id", mGroupID);
		json.put("imei", tm.getDeviceId());
		if(id == R.id.btn_agree){
			json.put("vote", "0");
		}else if(id == R.id.btn_disagree){
			json.put("vote", "1");
		}
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_VOTE);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json.toJSONString()));
        CoreSocket.getInstance().sendMessage(messagePacking);
        if(id == R.id.btn_agree){
        	mWaitingStudentTip.setText(R.string.waiting_other_confirm_tip);
        }
        mBtnAgree.setVisibility(View.GONE);
    	mBtnDisagree.setVisibility(View.GONE);
	}

	@Override
	protected void onStop(){
		super.onStop();
		mGroupIcons.recycle();
	}
	
}
