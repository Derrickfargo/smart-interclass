package cn.com.incito.socket.handler;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

public class DeleteGroupHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		if(0 == data.getIntValue("code")){
				UIHelper.getInstance().showGroupSelect(data.toString());
		}

	}

}
