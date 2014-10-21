package cn.com.incito.socket.handler;

import cn.com.incito.classroom.ui.activity.WaitingActivity;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.wisdom.sdk.log.WLog;
/**
 * 学生登录注册hanlder
 * Created by popoy on 2014/7/28.
 */
public class StudentLoginHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		WLog.i(StudentLoginHandler.class, "学生登录数据"+data);
		UIHelper.getInstance().getWaitingActivity()
		.doResult(data, WaitingActivity.STUDENT_LOGIN);
	}

}
