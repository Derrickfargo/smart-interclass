package cn.com.incito.classroom.ui.activity;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import android.os.Bundle;
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
import cn.com.incito.classroom.adapter.HorizontalListViewAdapter;


public class ConfirmGroupInfoActivity extends BaseActivity {

	private static final String TAG = "ConfirmGroupInfoActivity";

	private ImageButton mBtnCancel = null;
	private ImageButton mBtnOk = null;
	private ImageView mGroupIcon = null;
	private TextView mGroupName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_group_info);
		initView();
		initData();
	}
	
	private void initView(){
		mBtnOk = (ImageButton)findViewById(R.id.btn_ok);
		mBtnCancel = (ImageButton)findViewById(R.id.btn_cancel);
		mGroupIcon = (ImageView)findViewById(R.id.group_icon);
		mGroupName = (TextView)findViewById(R.id.group_name);
	}
	
	private void initData(){
		Bundle bundle = getIntent().getExtras();
		Map<String,String> json = (Map<String,String>) bundle.get("data");
		mGroupName.setText(json.get("name"));
//		mGroupIcon.setImageDrawable(drawable);
	}
	
}
