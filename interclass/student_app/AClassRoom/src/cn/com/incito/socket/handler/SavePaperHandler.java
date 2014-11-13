package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.utils.ApiClient;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.wisdom.sdk.log.WLog;
/**
 * 试卷提交保存hanlder
 * Created by liguangming on 2014/7/28.
 */
public class SavePaperHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		if(MyApplication.getInstance().isSubmitPaper()){
			MyApplication.getInstance().setSubmitPaper(false);
		}else{
			int delay = data.getIntValue("delay");
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+"SavePaperHandler.class:"+"作业延迟时间:" + delay);
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				ApiClient.uploadErrorLog(e.getMessage());
				e.printStackTrace();
			}
			if(UIHelper.getInstance().getDrawBoxActivity()!=null){
				UIHelper.getInstance().getDrawBoxActivity().submitPaper();
				MyApplication.getInstance().setSubmitPaper(true);
			}
			UIHelper.getInstance().showClassingActivity();
		}
	}
}
