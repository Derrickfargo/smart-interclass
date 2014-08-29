package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.wisdom.sdk.log.WLog;

/**
 * 试卷提交结果处理hanlder Created by liguangming on 2014/7/28.
 */
public class SavePaperResultHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		if (data.getIntValue("code") == 0) {
			WLog.i(SavePaperResultHandler.class, "paper保存成功");
			MyApplication.getInstance().setSubmitPaper(true);
		}
	}

}
