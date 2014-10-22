package cn.com.incito.socket.handler;

import android.content.Intent;
import android.util.Log;
import cn.com.incito.classroom.ui.activity.DrawBoxActivity;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.wisdom.sdk.log.WLog;

import com.alibaba.fastjson.JSONObject;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
/**
 * 判定设备是否绑定hanlder
 * Created by liushiping on 2014/7/28.
 */
public class DeviceHasBindHandler extends MessageHandler {
	private static final String TAG=DeviceHasBindHandler.class.getSimpleName();
	public static final Logger Logger = LoggerFactory.getLogger();
	@Override
	protected void handleMessage() {
		if (data.getIntValue("code") != 0) {
			return;
		}
		JSONObject jsonObject = data.getJSONObject("data");
		UIHelper uiHelper = UIHelper.getInstance();

		if (jsonObject.getBoolean("isbind")) {
			uiHelper.showWaitingActivity();
			Logger.debug(Utils.getTime()+TAG+":已绑定设备，进入等待界面..."+"response:"+jsonObject.toJSONString());
			Log.i(TAG, "已绑定设备，进入等待界面..."+"response:"+jsonObject.toJSONString());
			
		} else {
			uiHelper.showBindDeskActivity();
			Logger.debug(Utils.getTime()+TAG+":已绑定设备，进入等待界面..."+"response:"+jsonObject.toJSONString());
			Log.i(TAG, "未绑定设备，进入绑定界面..."+"response:"+jsonObject.toJSONString());
		}
	}

}
