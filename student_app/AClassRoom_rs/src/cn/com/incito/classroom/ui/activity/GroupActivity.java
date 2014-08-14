package cn.com.incito.classroom.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;

/**
 * 小组暗号输入页面
 * 
 * @author 陈正
 * 
 */
public class GroupActivity extends BaseActivity {

	private int fragmentId;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		fragmentManager = getSupportFragmentManager();
		fragmentId = R.id.group_fragmentId;
		skipToPasswordPage();
	}

	/**
	 * 跳转到输入暗号的页面
	 */
	void skipToPasswordPage() {

		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		GroupPasswordFragment passwordFragment = new GroupPasswordFragment();
		fragmentTransaction.replace(fragmentId, passwordFragment);
		fragmentTransaction.commitAllowingStateLoss();

	}
	
	/**
	 * 跳转到小组信息编辑页面
	 */
	void skipToGroupEditorPage(){
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		GroupEditorFragment editorFragment = new GroupEditorFragment();
		fragmentTransaction.replace(fragmentId, editorFragment);
		fragmentTransaction.commitAllowingStateLoss();
		
	}
	
	/**
	 * 跳转到小组信息展示页面
	 */
	void skipToGroupInfoShowPage(){
		
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		GroupInfoShowFragment infoShowFragment = new GroupInfoShowFragment();
		fragmentTransaction.replace(fragmentId, infoShowFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}

}
