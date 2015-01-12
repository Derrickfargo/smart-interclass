package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 设备绑定hanlder Created by liushiping on 2014/7/28.
 */
public class DeviceBindHandler extends MessageHandler {
	@Override
	protected void handleMessage() {
		if ("0".equals(data.getString("code"))) {
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":DeviceBindHandler:设备已经绑定");
			UIHelper uiHelper = UIHelper.getInstance();
			uiHelper.showLoginActivity();
		} else {
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":DeviceBindHandler:设备绑定失败，一个桌子最多绑定..."+ Constants.PAD_PER_DESK + "个pad"+ + Constants.PAD_PER_DESK + "个pad");
			UIHelper.getInstance().getBindDeskActivity().showErrorDialog();
		}
	}
}
