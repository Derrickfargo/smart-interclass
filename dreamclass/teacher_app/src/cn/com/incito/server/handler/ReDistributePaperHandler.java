package cn.com.incito.server.handler;

import cn.com.incito.server.core.MessageHandler;

public class ReDistributePaperHandler extends MessageHandler{

	@Override
	protected void handleMessage() {
		String imei = data.getString("imei");
		service.removeOrderSet(imei);
	}
}
