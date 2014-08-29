package cn.com.incito.socket.handler;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.wisdom.sdk.log.WLog;

/**
 * 分组提交修改hanlder Created by bianshijian on 2014/7/28.
 */
public class GroupSubmitHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		int code = data.getIntValue("code");
		if (code == 0) {
			JSONObject json = data.getJSONObject("data");
			WLog.i(DeviceBindHandler.class, "分组提交成功" + json);
			UIHelper.getInstance().showConfirmGroupActivity(json);
		} else {
			// TODO showMessage
		}

	}

}
