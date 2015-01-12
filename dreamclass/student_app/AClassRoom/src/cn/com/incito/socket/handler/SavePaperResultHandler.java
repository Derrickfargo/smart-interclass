package cn.com.incito.socket.handler;

import java.io.File;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 试卷提交结果处理hanlder Created by liguangming on 2014/7/28.
 */
public class SavePaperResultHandler extends MessageHandler {
	@Override
	protected void handleMessage() {
		if (data.getIntValue("code") == 0) {
			File f = new File("/sdcard/", "temp.jpg");
			if(f.exists()){
				f.delete();
			}
			MyApplication.Logger.debug(Utils.getTime()+":SavePaperResultHandler收到服务器发送的作业保存成功信息");
			MyApplication.getInstance().lockScreen(true);
			MyApplication.getInstance().setSubmitPaper(true);
			UIHelper.getInstance().getDrawBoxActivity().closeProgressDialog();
			UIHelper.getInstance().getDrawBoxActivity().cancleTask();
			UIHelper.getInstance().getDrawBoxActivity().finish();
		}
	}
}
