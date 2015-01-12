package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.AndroidUtil;
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
		UIHelper uiHelper = UIHelper.getInstance();

		if (jsonObject.getBoolean("isbind")) {
			uiHelper.showWaitingActivity();
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+":DeviceHasBindHandler已绑定设备,进入等待界面..."+"response:"+jsonObject.toJSONString());
			
		} else {
			uiHelper.showBindDeskActivity();
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+":DeviceHasBindHandler未绑定设备,进入绑定界面..."+"response:"+jsonObject.toJSONString());
		}
	}

}
