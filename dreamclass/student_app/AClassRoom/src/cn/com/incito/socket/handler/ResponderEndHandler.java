package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 结束抢答
 * @author hm
 *
 */
public class ResponderEndHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		String currentActiivty = AppManager.getAppManager().currentActivity().getClass().getSimpleName();
		
		if("ResponderActivity".equals(currentActiivty)){
			AppManager.getAppManager().finishActivity();
		}
	}
}
