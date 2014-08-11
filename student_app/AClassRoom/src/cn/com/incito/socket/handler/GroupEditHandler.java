package cn.com.incito.socket.handler;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

public class GroupEditHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		int groupID = data.getIntValue("id");
		UIHelper.getInstance().showEditGroupActivity(groupID);
	}

}
