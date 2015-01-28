package cn.com.incito.server.handler;

import cn.com.incito.server.core.MessageHandler;
import cn.com.incito.server.utils.QuizCollector;

public class ReSavePaperHander extends MessageHandler{

	@Override
	protected void handleMessage() {
		QuizCollector.getInstance().orderComplete(ctx);
	}
}
