package cn.com.incito.socket.handler;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

public class DeviceBindHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		if (data.getIntValue("code") != 0) {
			return;
		}
		UIHelper uiHelper = UIHelper.getInstance();
		uiHelper.showLoginActivity();
	}

}
