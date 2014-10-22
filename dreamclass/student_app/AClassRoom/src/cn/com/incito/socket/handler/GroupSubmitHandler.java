package cn.com.incito.socket.handler;

import android.util.Log;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

import com.alibaba.fastjson.JSONObject;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 分组提交修改hanlder Created by bianshijian on 2014/7/28.
 */
public class GroupSubmitHandler extends MessageHandler {
	private static final String TAG=GroupSubmitHandler.class.getSimpleName();
	public static final Logger Logger = LoggerFactory.getLogger();
	@Override
	protected void handleMessage() {
		int code = data.getIntValue("code");
		if (code == 0) {
			JSONObject json = data.getJSONObject("data");
			Logger.debug(Utils.getTime()+TAG+":分组提交成功" + "response:"+json);
			Log.i(TAG, "分组提交成功" + "response:"+json);
			UIHelper.getInstance().showConfirmGroupActivity(json);
		} else {
		}

	}

}
