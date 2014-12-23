package cn.com.incito.classroom.ui.activity;

import android.os.Bundle;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.common.utils.UIHelper;

public class ClassingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UIHelper.getInstance().setClassingActivity(this);
		setContentView(R.layout.activity_classing);
		registRecier();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		unRegistReciver();
		super.onDestroy();
	}
}
