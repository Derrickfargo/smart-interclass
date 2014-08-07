package cn.com.incito.socket.handler;

import cn.com.incito.classroom.ui.activity.WaitingActivity;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

public class StudentLoginHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		UIHelper.getInstance().getWaitingActivity()
		.doResult(data, WaitingActivity.STUDENT_LOGIN);
	}

}
