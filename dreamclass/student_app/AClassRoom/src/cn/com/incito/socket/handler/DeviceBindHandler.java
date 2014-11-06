package cn.com.incito.socket.handler;

import android.util.Log;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 设备绑定hanlder Created by liushiping on 2014/7/28.
 */
public class DeviceBindHandler extends MessageHandler {
	private static final String TAG=DeviceBindHandler.class.getSimpleName();
	public static final Logger Logger = LoggerFactory.getLogger();
	@Override
	protected void handleMessage() {
		if ("0".equals(data.getString("code"))) {
			Logger.debug(Utils.getTime()+TAG+"设备已绑定，进入..." + "code:" + 0);
			Log.i(TAG, "设备已绑定，进入..." + "code:" + 0);
			UIHelper uiHelper = UIHelper.getInstance();
			uiHelper.showLoginActivity();
		} else {
			Logger.debug(Utils.getTime()+TAG+":设备绑定失败，一个桌子最多绑定..."+Constants.PAD_PER_DESK + "个pad");
			Log.i(TAG, "设备绑定失败，一个桌子最多绑定..."+ Constants.PAD_PER_DESK + "个pad");
			UIHelper.getInstance().getBindDeskActivity().showErrorDialog();
		
		}

	}

}
