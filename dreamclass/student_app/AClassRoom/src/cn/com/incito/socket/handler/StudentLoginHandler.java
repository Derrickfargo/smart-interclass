package cn.com.incito.socket.handler;

import android.util.Log;
import cn.com.incito.classroom.ui.activity.WaitingActivity;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
/**
 * 学生登录注册hanlder
 * Created by popoy on 2014/7/28.
 */
public class StudentLoginHandler extends MessageHandler {
	private static final String TAG=DeviceBindHandler.class.getSimpleName();
	public static final Logger Logger = LoggerFactory.getLogger();
	@Override
	protected void handleMessage() {
		Logger.debug(Utils.getTime()+TAG+"：学生登录数据"+data);
		Log.i(TAG, "学生登录数据"+data);
		UIHelper.getInstance().getWaitingActivity()
		.doResult(data, WaitingActivity.STUDENT_LOGIN);
	}

}
