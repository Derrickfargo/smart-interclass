package cn.com.incito.socket.handler;

import cn.com.incito.classroom.ui.activity.RandomGroupActivity;
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
		//判断当前是否是随机分组界面如果是随机分组界面则只刷新界面数据
		if(RandomGroupActivity.class.equals(activity.getClass())){
			UIHelper.getInstance().getRandomGroupActivity().refreshData(data);
		}else{
			UIHelper.getInstance().showRandomGroupActivity(data);
			finishNotWaitingActivity();
		}
		SendMessageUtil.sendGroupList();
	}
}
