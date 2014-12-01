package cn.com.incito.socket.handler;

import java.io.File;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 试卷提交结果处理hanlder Created by liguangming on 2014/7/28.
 */
public class SavePaperResultHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+"--SavePaperResultHandler.class:保存作业完成后返回信息"+data.toJSONString());
		if (data.getIntValue("code") == 0) {
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+"提交作业成功");
			UIHelper.getInstance().getDrawBoxActivity().cancleTask();
			MyApplication.getInstance().setSubmitPaper(true);
			MyApplication.getInstance().lockScreen(true);
			File f = new File("/sdcard/", "temp.png");
			if(f.exists()){
				f.delete();
			}
			AppManager.getAppManager().currentActivity().finish();
			if (UIHelper.getInstance().getClassingActivity() == null) {
				UIHelper.getInstance().showClassingActivity();
			}
		}
	}

}
