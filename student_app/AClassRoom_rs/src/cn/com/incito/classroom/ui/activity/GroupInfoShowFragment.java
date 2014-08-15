package cn.com.incito.classroom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.constants.Constant;
import cn.com.incito.classroom.entry.IGroupInfo;

/**
 * 编辑好小组信息之后的小组信息预览页面
 * 
 * @author 陈正
 * 
 */
public class GroupInfoShowFragment extends CommonFragment {

	private TextView textView_name;
	private ImageView imageView_icon;
	private Button button_back, button_ok;
	private IGroupInfo groupInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContainerView = LayoutInflater.from(mActivity).inflate(
				R.layout.group_infoshow, null);
		initData();
		initViews();

		return mContainerView;
	}

	private void initData() {

		groupInfo = getArguments().getParcelable(
				Constant.GROUP_INTENT_BUNDLE_NAME);

	}

	private void initViews() {

		textView_name = (TextView) mContainerView
				.findViewById(R.id.group_infoShow_name);
		imageView_icon = (ImageView) mContainerView
				.findViewById(R.id.group_infoShow_icon);
		button_back = (Button) mContainerView
				.findViewById(R.id.group_infoShow_back);
		button_ok = (Button) mContainerView
				.findViewById(R.id.group_infoShow_ok);

		button_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				backToEditor();
			}
		});

		button_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sure();
			}
		});

		textView_name.setText(groupInfo.getGroupName());
		imageView_icon.setImageResource(groupInfo.getGroupIcon());

	}

	/**
	 * 返回重新编辑小组信息
	 */
	private void backToEditor() {
		if (mActivity instanceof GroupActivity) {
			((GroupActivity) mActivity).skipToGroupEditorPage(groupInfo);
		}

	}

	/**
	 * 填好了,之后进行页面跳转
	 */
	private void sure() {
		Intent intent = new Intent(mActivity, ClassReadyActivity.class);
		startActivity(intent);
		mActivity.finish();
	}
}
