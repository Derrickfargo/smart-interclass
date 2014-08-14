package cn.com.incito.socket.handler;

import cn.com.incito.socket.core.MessageHandler;

public class SavePaperResultHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		if(data.getIntValue("code")==0){
			System.out.println("保存成功了");
		}
	}

}
