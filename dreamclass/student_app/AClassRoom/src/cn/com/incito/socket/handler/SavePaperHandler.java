package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.util.SendMessageUtil;

/**
 * 试卷提交保存hanlder Created by liguangming on 2014/7/28.
 */
public class SavePaperHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		SendMessageUtil.sendReplyReciveSavePaper();
		if (UIHelper.getInstance().getDrawBoxActivity() != null && AppManager.getAppManager().currentActivity().getClass().getSimpleName().equals("DrawBoxActivity")) {
			LogUtil.d("当前是做作业界面,向服务器上传作业!");
			UIHelper.getInstance().getDrawBoxActivity().sendPaper();
		}
	}
}
