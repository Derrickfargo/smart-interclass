package com.popoy.common;

import com.popoy.common.TAActivity;
//import com.popoy.widget.actionbar.TitleBar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	protected TAActivity mActivity;
	protected View mView;
	/** 标题栏布局. */
//	protected TitleBar titleBar = null;

	protected abstract int getLayoutId();

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.mActivity = (TAActivity) activity;
	}

	public void onActivityCreated(Bundle paramBundle) {
		super.onActivityCreated(paramBundle);

	}

	public View onCreateView(LayoutInflater paramLayoutInflater,
			ViewGroup paramViewGroup, Bundle paramBundle) {
		this.mView = paramLayoutInflater.inflate(getLayoutId(), paramViewGroup,
				false);
//		titleBar = (TitleBar) mView.findViewById(R.id.titlebar);
		return this.mView;
	}
//
//	public TitleBar getTitleBar() {
//		return titleBar;
//	}
}