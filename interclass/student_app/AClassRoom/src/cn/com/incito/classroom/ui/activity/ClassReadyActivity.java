package cn.com.incito.classroom.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;

public class ClassReadyActivity extends BaseActivity{
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setContentView(R.layout.activity_classready);

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}
	
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		return true;
	}
}