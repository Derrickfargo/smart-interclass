package cn.com.incito.socket.handler;

import cn.com.incito.classroom.ui.activity.SplashActivity;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

import com.alibaba.fastjson.JSONObject;
/**
 * 判定设备是否绑定hanlder
 * Created by liushiping on 2014/7/28.
 */
public class DeviceHasBindHandler extends MessageHandler {
	@Override
	protected void handleMessage() {
		if (data.getIntValue("code") != 0) {
			return;
		}
		
		JSONObject jsonObject = data.getJSONObject("data");
		if (jsonObject.getBoolean("isbind")) {
			UIHelper.getInstance().showWaitingActivity();
			LogUtil.d("已绑定设备,进入等待界面");
			
		} else {
			UIHelper.getInstance().showBindDeskActivity();
			LogUtil.d("未绑定设备,进入绑定界面");
		}
		if(SplashActivity.class.equals(activity.getClass())){
			activity.finish();
		}
	}

}
