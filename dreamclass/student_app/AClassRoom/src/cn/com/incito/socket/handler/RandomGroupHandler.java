package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.util.SendMessageUtil;

/**
 * 随机分组handler
 * @author hm
 */
public class RandomGroupHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		String currentActivityName = AppManager.getAppManager().currentActivity().getClass().getSimpleName();
		SendMessageUtil.sendGroupList();
		
		//判断当前是否是随机分组界面如果是随机分组界面则只刷新界面数据
		if("RandomGroupActivity".equals(currentActivityName)){
			UIHelper.getInstance().getRandomGroupActivity().refreshData(data);
		}else{
			UIHelper.getInstance().showRandomGroupActivity(data);
			finishNotWaitingActivity();
		}
	}
}
