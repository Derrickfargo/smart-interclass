package cn.com.incito.socket.handler;

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
		} else {
			uiHelper.showBindDeskActivity();
		}
	}

}
