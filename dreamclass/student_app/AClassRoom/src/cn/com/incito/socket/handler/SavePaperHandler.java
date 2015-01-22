package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 试卷提交保存hanlder
 * Created by liguangming on 2014/7/28.
 */
public class SavePaperHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		if (MyApplication.getInstance().isSubmitPaper()) {
			MyApplication.getInstance().setSubmitPaper(false);
		} else {
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":SavePaperHandler:收到老师提交作业命令");
			if (UIHelper.getInstance().getDrawBoxActivity() != null && AppManager.getAppManager().currentActivity().getClass().getSimpleName().equals("DrawBoxActivity")) {
				UIHelper.getInstance().getDrawBoxActivity().sendPaper();
			}
		}
	}
}
