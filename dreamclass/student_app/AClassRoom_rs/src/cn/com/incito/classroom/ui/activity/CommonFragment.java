package cn.com.incito.classroom.ui.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
/**
 * 公共的Fragment
 * @author 陈正
 *
 */
public class CommonFragment extends Fragment {
	
	protected Activity mActivity ;
	
	protected View mContainerView ;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}

	

}
