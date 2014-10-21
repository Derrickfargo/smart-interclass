package cn.com.incito.socket.handler;

import cn.com.incito.classroom.ui.activity.WaitingActivity;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.wisdom.sdk.log.WLog;
/**
 * 获取组成员列表hanlder
 * Created by liushiping on 2014/7/28.
 */
public class GroupListHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		WLog.i(GroupListHandler.class,"收到分组列表");
		UIHelper.getInstance().getWaitingActivity()
				.doResult(data, WaitingActivity.STUDENT_LIST);
	}

}
