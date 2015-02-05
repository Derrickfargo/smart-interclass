package cn.com.incito.socket.handler;

import android.os.SystemClock;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.ui.activity.ResponderActivity;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 结束抢答
 * @author hm
 *
 */
public class ResponderEndHandler extends MessageHandler {
	
	@Override
	protected void handleMessage() {
		SystemClock.sleep(2000);
		if(ResponderActivity.class.equals(activity.getClass())){
			ResponderActivity responderActivity = (ResponderActivity)activity;
			if(responderActivity.getBeforResponderisLockScreeen()){
				MyApplication.getInstance().lockScreen(true);
			}
			AppManager.getAppManager().finishActivity();
		}
	}
}
