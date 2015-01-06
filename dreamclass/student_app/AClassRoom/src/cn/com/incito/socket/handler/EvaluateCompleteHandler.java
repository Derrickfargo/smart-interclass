package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;


public class EvaluateCompleteHandler extends MessageHandler{

	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "收到结束互评的消息");
		if(UIHelper.getInstance().getEvaluateActivity()!=null){
			UIHelper.getInstance().getEvaluateActivity().finish();
			MyApplication.getInstance().setLockScreen(true);
		}
		MyApplication.getInstance().lockScreen(true);
	}

}
