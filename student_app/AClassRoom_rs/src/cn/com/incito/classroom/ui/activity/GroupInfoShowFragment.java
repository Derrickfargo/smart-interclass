package cn.com.incito.classroom.ui.activity;

import cn.com.incito.classroom.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 编辑好小组信息之后的小组信息预览页面
 * @author 陈正
 *
 */
public class GroupInfoShowFragment extends CommonFragment {
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContainerView = LayoutInflater.from(mActivity).inflate(R.layout.group_infoshow, null);
		
		initViews();
		
		return mContainerView;
	}
	
	
	private void initViews(){
		
		
	}

}
