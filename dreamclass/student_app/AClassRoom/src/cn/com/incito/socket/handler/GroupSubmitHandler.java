package cn.com.incito.socket.handler;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

import com.alibaba.fastjson.JSONObject;

/**
 * 分组提交修改hanlder Created by bianshijian on 2014/7/28.
 */
public class GroupSubmitHandler extends MessageHandler {
	@Override
	protected void handleMessage() {
		int code = data.getIntValue("code");
		if (code == 0) {
			JSONObject json = data.getJSONObject("data");
			UIHelper.getInstance().showConfirmGroupActivity(json);
			finishNotWaitingActivity();
		}
	}
}
