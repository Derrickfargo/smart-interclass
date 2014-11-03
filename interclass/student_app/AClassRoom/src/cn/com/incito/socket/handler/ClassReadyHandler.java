package cn.com.incito.socket.handler;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

public class ClassReadyHandler  extends MessageHandler{

	@Override
	protected void handleMessage() {
		System.out.println(data.toJSONString());
		UIHelper.getInstance().showClassReadyActivity();
	}

}
