package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.ui.activity.SplashActivity;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.wisdom.sdk.log.WLog;

/**
 * 设备绑定hanlder Created by liushiping on 2014/7/28.
 */
public class DeviceBindHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		if ("0".equals(data.getString("code"))) {
			WLog.i(DeviceBindHandler.class, "设备已绑定，进入..." + "code:" + 0);
			UIHelper uiHelper = UIHelper.getInstance();
			uiHelper.showLoginActivity();
		} else {
			WLog.i(DeviceBindHandler.class, "设备绑定失败，一个桌子最多绑定..."
					+ Constants.PAD_PER_DESK + "个pad");
			UIHelper.getInstance().getBindDeskActivity().showErrorDialog();
		
		}

	}

}
