package cn.com.incito.socket.handler;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 分组修改hanlder Created by bianshijian on 2014/7/28.
 */
public class GroupEditHandler extends MessageHandler {
	@Override
	protected void handleMessage() {
		int groupID = data.getIntValue("id");
		UIHelper.getInstance().showEditGroupActivity(groupID);
		finishNotWaitingActivity();
	}
}
