package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 收到抢答消息的处理类
 * 
 * @author hm
 *
 */
public class ResponderHandler extends MessageHandler {

	@Override
	public void handleMessage(Message msg) {
		handleMessage();
	}

	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()
				+ "::ResponderHandler::" + "收到抢答消息");
		boolean isLockScreen = MyApplication.getInstance().isLockScreen();

		/**
		 * 判断当前PAD是否是锁屏 如果是锁屏则解锁 并且向抢答界面传送此时的状态
		 */
		if (isLockScreen) {
			MyApplication.getInstance().lockScreen(false);
		}
		// 进入抢答界面
		UIHelper.getInstance().showResponderActivity(isLockScreen);
	}
}
