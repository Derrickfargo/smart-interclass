package cn.com.incito.socket.handler;

import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

public class SavePaperHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		UIHelper.getInstance().getDrawBoxActivity().submitPaper();
//		UIHelper.getInstance().getDrawBoxActivity().doResult(data, DrawBoxActivity.RESULT_CODE);
	}

}
