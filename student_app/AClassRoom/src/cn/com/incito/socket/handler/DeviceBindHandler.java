package cn.com.incito.socket.handler;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
/**
 * 设备绑定hanlder
 * Created by liushiping on 2014/7/28.
 */
public class DeviceBindHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		if("0".equals(data.getString("code"))) {
			UIHelper uiHelper = UIHelper.getInstance();
			uiHelper.showLoginActivity();
		}
		
	}

}
