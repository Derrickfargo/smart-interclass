package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 收到抢答消息的处理类
 * @author hm
 *
 */
public class ResponderHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "::ResponderHandler::" + "收到抢答消息::" + data.toJSONString());
		
		int code = data.getIntValue("code");
		
		if(0 == code){
			//屏幕解锁
			MyApplication.getInstance().setLockScreen(false);
			//进入抢答界面
			UIHelper.getInstance().showResponderActivity();
		}
	}
}
