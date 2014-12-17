package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 随机分组handler
 * @author hm
 */
public class RandomGroupHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "::RandomGroupHandler::收到随机分组消息" + data.toJSONString());
		
		String currentActivityName = AppManager.getAppManager().currentActivity().getClass().getSimpleName();
		
		UIHelper.getInstance().getWaitingActivity().refreshStudents(data);
		
		
		//判断当前是否是随机分组界面如果是随机分组界面则只刷新界面数据
		if("RandomGroupActivity".equals(currentActivityName)){
			UIHelper.getInstance().getRandomGroupActivity().refreshData(data);
		}else{	//否则的话显示随机分组界面并且显示数据
			if(!"WaitingActivity".equals(currentActivityName)){
				AppManager.getAppManager().finishActivity();
			}
			UIHelper.getInstance().showRandomGroupActivity(data);
		}
	}
}
