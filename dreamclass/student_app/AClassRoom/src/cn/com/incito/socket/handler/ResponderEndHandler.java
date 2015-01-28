package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.ui.activity.ResponderActivity;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 结束抢答
 * @author hm
 *
 */
public class ResponderEndHandler extends MessageHandler {
	
	@Override
	protected void handleMessage() {
		LogUtil.d("收到抢答结束命令");
		String currentActiivty = AppManager.getAppManager().currentActivity().getClass().getSimpleName();
		if("ResponderActivity".equals(currentActiivty)){
			ResponderActivity activity = (ResponderActivity) AppManager.getAppManager().currentActivity();
			AppManager.getAppManager().finishActivity();
			if(activity.getBeforResponderisLockScreeen()){
				MyApplication.getInstance().lockScreen(true);
			}
		}
	}
}
