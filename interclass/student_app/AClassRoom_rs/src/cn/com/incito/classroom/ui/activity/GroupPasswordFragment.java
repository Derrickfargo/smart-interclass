package cn.com.incito.classroom.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import cn.com.incito.classroom.R;

/**
 * 小组暗号输入页面
 * 
 * @author 陈正
 * 
 */
public class GroupPasswordFragment extends CommonFragment {
	private EditText editText_password;
	private Button button_ok;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContainerView = LayoutInflater.from(mActivity).inflate(
				R.layout.grouppaasword_main, null);
		initViews();
		return mContainerView;
	}

	private void initViews() {

		editText_password = (EditText) mContainerView
				.findViewById(R.id.groupPassword_password);
		button_ok = (Button) mContainerView.findViewById(R.id.groupPassword_ok);

		button_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sureToNext();
			}
		});
	}

	/**
	 * 填好了,进入下一步
	 */
	private void sureToNext() {
		String password = editText_password.getText().toString().trim();
		// TODO 此处可对学生填写的暗号进行正则验证
		
		if(mActivity instanceof GroupActivity){
			((GroupActivity)mActivity).skipToGroupEditorPage(null);
			
		}

	}
}
