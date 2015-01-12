package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 分组修改hanlder Created by bianshijian on 2014/7/28.
 */
public class GroupEditHandler extends MessageHandler {
	@Override
	protected void handleMessage() {
		String currentActivityName = AppManager.getAppManager().currentActivity().getClass().getSimpleName();
		int groupID = data.getIntValue("id");
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+":GroupEditHandler收到修改分组...组ID：" + groupID);
		if("RandomGroupActivity".equals(currentActivityName)){
			AppManager.getAppManager().finishActivity();
		}
		UIHelper.getInstance().showEditGroupActivity(groupID);
		MyApplication.getInstance().lockScreen(false);
	}

}
