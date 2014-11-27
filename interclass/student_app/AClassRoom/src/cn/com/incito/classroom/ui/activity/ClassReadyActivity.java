package cn.com.incito.classroom.ui.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.navisdk.util.common.StringUtils;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.exception.AppException;
import cn.com.incito.classroom.utils.ApiClient;
import cn.com.incito.classroom.utils.UpdateManager;
import cn.com.incito.classroom.vo.Version;
import cn.com.incito.common.utils.AndroidUtil;

public class ClassReadyActivity extends BaseActivity {

	private String url;

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setContentView(R.layout.activity_classready);
		registRecier();
		new Thread(new Runnable() {
			public void run() {
				if(isUpdateApk()){
					mHandler.sendEmptyMessage(1);
				};
			}
		}).start();
	}

	public boolean isUpdateApk() {
		// TODO 升级
		String ip = Constants.getSERVER_IP();
		String port = Constants.getSERVER_PORT();
		if (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(port)) {
			try {
				JSONObject updateResult = JSONObject.parseObject(ApiClient.updateApk(MyApplication.getInstance().getCode()));
				MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ "SplashActivity:" + "版本更新返回信息：" + updateResult);
				Log.i("SplashActivity", "版本更新返回信息：" + updateResult);
				if (updateResult.getInteger("code") == 0) {
					Version version = JSON.parseObject(updateResult.getJSONObject("data").toJSONString(),Version.class);
					url = "http://"+Constants.getSERVER_IP()+":"+Constants.getSERVER_PORT()+"/app/api/version/download?id=" + version.getId();
					MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ "SplashActivity:" + "更新地址：" + url);
					return true;
				}
			} catch (AppException e) {
				ApiClient.uploadErrorLog(e.getMessage());
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		unRegistReciver();
		super.onDestroy();

	}
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
					UpdateManager mUpdateManager = new UpdateManager(ClassReadyActivity.this, url);
					mUpdateManager.checkUpdateInfo();
				break;
			default:
				break;
			}

		}
	};
}