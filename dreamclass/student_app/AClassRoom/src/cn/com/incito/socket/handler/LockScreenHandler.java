package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.NCoreSocket;

public class LockScreenHandler extends MessageHandler {

	private String isLock;

	@Override
	protected void handleMessage() {
		isLock = data.getString("isLock");
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":LockScreenHandler收到锁屏消息:" + isLock);
		String currentActivityName = AppManager.getAppManager().currentActivity().getClass().getSimpleName();
		if (isLock.equals("true")) {
				if("RandomGroupActivity".equals(currentActivityName)){
					AppManager.getAppManager().finishActivity();
				}
				MyApplication.getInstance().setOnClass(true);
				MyApplication.getInstance().lockScreen(true);
		} else if(isLock.equals("false")){
			MyApplication.getInstance().lockScreen(false);
		}else{
			MyApplication.getInstance().lockScreen(false);
			MyApplication.getInstance().setOnClass(false);
			NCoreSocket.getInstance().stopConnection();
			AppManager.getAppManager().AppExit(MyApplication.getInstance());
		}
	}
}
