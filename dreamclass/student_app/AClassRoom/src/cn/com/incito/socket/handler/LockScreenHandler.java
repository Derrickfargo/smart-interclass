package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.NCoreSocket;

public class LockScreenHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		String isLock = data.getString("isLock");
		if (isLock.equals("true")) {
			MyApplication.getInstance().lockScreen(true);
			finishNotWaitingActivity();
		} else if(isLock.equals("false")){
			MyApplication.getInstance().lockScreen(false);
		}else{
			MyApplication.getInstance().isOver = true;
			MyApplication.getInstance().lockScreen(false);
			AppManager.getAppManager().AppExit(MyApplication.getInstance());
			NCoreSocket.getInstance().closeConnection();
		}
	}
}
