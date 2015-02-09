package cn.com.incito.socket.handler;

import android.os.SystemClock;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 结束抢答
 * @author hm
 *
 */
public class ResponderEndHandler extends MessageHandler {
	
	@Override
	protected void handleMessage() {
		SystemClock.sleep(1000);
		if(!MyApplication.getInstance().isLockScreen()){
			MyApplication.getInstance().lockScreen(true);
		}
		finishNotWaitingActivity();
	}
}
