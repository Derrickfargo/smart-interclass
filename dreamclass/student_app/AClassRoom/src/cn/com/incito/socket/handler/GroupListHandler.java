package cn.com.incito.socket.handler;

import android.util.Log;
import cn.com.incito.classroom.ui.activity.WaitingActivity;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
/**
 * 获取组成员列表hanlder
 * Created by liushiping on 2014/7/28.
 */
public class GroupListHandler extends MessageHandler {
	private static final String TAG=GroupListHandler.class.getSimpleName();
	public static final Logger Logger = LoggerFactory.getLogger();
	@Override
	protected void handleMessage() {
		Logger.debug(Utils.getTime()+TAG+":收到分组列表"+data.toJSONString());
		Log.i(TAG,"收到分组列表"+data.toJSONString());
		UIHelper.getInstance().getWaitingActivity()
				.doResult(data, WaitingActivity.STUDENT_LIST);
	}

}
