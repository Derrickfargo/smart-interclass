package cn.com.incito.socket.handler;

import com.alibaba.fastjson.JSONObject;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.ui.activity.ResponderActivity;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 结束抢答
 * @author hm
 *
 */
public class ResponderEndHandler extends MessageHandler {
	
	@Override
	public void handleMessage(Message msg) {
		handleMessage();
	}
	
	@Override
	public void handleMessage(JSONObject jsonObject) {
		handleMessage();
	}

	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "ResponderEndHandler:收到抢答结束命令" );
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
