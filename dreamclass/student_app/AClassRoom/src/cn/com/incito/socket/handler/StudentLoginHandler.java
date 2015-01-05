package cn.com.incito.socket.handler;

import android.util.Log;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.ui.activity.WaitingActivity;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
/**
 * 学生登录注册hanlder
 * Created by popoy on 2014/7/28.
 */
public class StudentLoginHandler extends MessageHandler {
	private static final String TAG=DeviceBindHandler.class.getSimpleName();
	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(Utils.getTime()+TAG+"：学生登录数据"+data);
		Log.i(TAG, "学生登录数据"+data);
		MyApplication.Logger.debug(TAG+"学生登录数据"+data);
		if(UIHelper.getInstance().getWaitingActivity()!=null){
			UIHelper.getInstance().getWaitingActivity().doResult(data, WaitingActivity.STUDENT_LOGIN);
		}
	}

}
