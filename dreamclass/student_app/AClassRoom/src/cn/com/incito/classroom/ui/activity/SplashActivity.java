package cn.com.incito.classroom.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.exception.AppException;
import cn.com.incito.classroom.utils.ApiClient;
import cn.com.incito.classroom.utils.UpdateManager;
import cn.com.incito.classroom.vo.Version;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.common.utils.ShortCutUtil;
import cn.com.incito.socket.core.NCoreSocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 用户其启动界面时候的一个启动页面完成一些初始化工作 Created by popoy on 2014/7/28.
 */

public class SplashActivity extends BaseActivity {

	private TextView tv_loading_msg;
	private ImageButton ib_setting_ip;
	private UpdateManager mUpdateManager;
	private IpSettingDialogFragment dialog;
	private int code;
	private String url;
	
	public UpdateManager getUpdateManager(){
		return mUpdateManager;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.splash, null);
		setContentView(view);
		/**
		 * 添加快捷方式
		 */
		ShortCutUtil.addShortCut(this, this.getClass(), R.drawable.ic_launcher, getResources().getString(R.string.app_name));

		tv_loading_msg = (TextView) view.findViewById(R.id.tv_loading_msg);
		ib_setting_ip = (ImageButton) view.findViewById(R.id.ib_setting_ip);
		ib_setting_ip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showSettingDialog();
			}
		});
		
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				tv_loading_msg.setText(R.string.loading_msg);
				ib_setting_ip.setVisibility(View.VISIBLE);
				NCoreSocket.getInstance().connection();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
	}

	@Override
	public void onBackPressed() {
		AppManager.getAppManager().AppExit(this);
	}

	private void showSettingDialog() {
		dialog = new IpSettingDialogFragment();
		dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		dialog.show(getSupportFragmentManager(), "");
	}

	public boolean isUpdateApk() {
		try {
			PackageManager pm = getPackageManager();
			PackageInfo info = pm.getPackageInfo("cn.com.incito.classroom", 0);
			code = info.versionCode;
		} catch (NameNotFoundException e) {
			ApiClient.uploadErrorLog(e.getMessage());
		}
		String ip = MyApplication.getInstance().getSharedPreferences().getString("server_ip", "");
		String port = MyApplication.getInstance().getSharedPreferences().getString("server_port", "");
		if (!TextUtils.isEmpty(ip) && !TextUtils.isEmpty(port)) {
			try {
				JSONObject updateResult = JSONObject.parseObject(ApiClient.updateApk(code));
				LogUtil.d("版本更新返回信息：" + updateResult);
				if (updateResult.getInteger("code") == 0) {
					Version version = JSON.parseObject(updateResult.getJSONObject("data").toJSONString(),Version.class);
					url = Constants.HTTP + ip + ":" + port + Constants.URL_DOWNLOAD_APK + version.getId();
					LogUtil.d("更新地址：" + url);
					return true;
				}
			} catch (AppException e) {
				ApiClient.uploadErrorLog(e.getMessage());
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public void UpdateAap(){
		mUpdateManager = new UpdateManager(SplashActivity.this, url);
		mUpdateManager.checkUpdateInfo();
	}
}
