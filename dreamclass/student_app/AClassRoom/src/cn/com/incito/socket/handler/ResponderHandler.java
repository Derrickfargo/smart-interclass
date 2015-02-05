package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 收到抢答消息的处理类
 * 
 * @author hm
 *
 */
public class ResponderHandler extends MessageHandler {


	@Override
	protected void handleMessage() {
		finishNotWaitingActivity();
		UIHelper.getInstance().showResponderActivity(MyApplication.getInstance().isLockScreen());
	}
}
