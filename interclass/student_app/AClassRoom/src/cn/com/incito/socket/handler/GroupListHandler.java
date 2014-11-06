package cn.com.incito.socket.handler;

//import cn.com.incito.classroom.vo.GroupVo;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.wisdom.sdk.log.WLog;

/**
 * 获取组成员列表hanlder Created by liushiping on 2014/7/28.
 */
public class GroupListHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		WLog.i(GroupListHandler.class, "收到分组列表消息：" + data);
		if(0 == data.getIntValue("code")){
			if(AppManager.getAppManager().currentActivity().getComponentName().getClassName().equals("WaitForOtherMembersActivity")){
				UIHelper.getInstance().showConfirmGroupActivity(data.getString("data"));
			} else {
				UIHelper.getInstance().showGroupSelect(data.getString("data"));
			}
		}
		
	}
}
