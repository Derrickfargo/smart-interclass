package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.socket.core.MessageHandler;
/**
 * 试卷提交结果处理hanlder
 * Created by liguangming on 2014/7/28.
 */
public class SavePaperResultHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		if(data.getIntValue("code")==0){
			System.out.println("保存成功了");
			MyApplication.getInstance().setSubmitPaper(true);
		}
	}

}
