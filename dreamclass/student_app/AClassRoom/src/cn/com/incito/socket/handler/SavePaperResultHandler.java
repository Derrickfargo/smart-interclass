package cn.com.incito.socket.handler;

import java.io.File;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 试卷提交结果处理hanlder Created by liguangming on 2014/7/28.
 */
public class SavePaperResultHandler extends MessageHandler {
	@Override
	protected void handleMessage() {
		if (data.getIntValue("code") == 0) {
			UIHelper.getInstance().getDrawBoxActivity().cancleTask();
			UIHelper.getInstance().getDrawBoxActivity().closeProgress();
			MyApplication.getInstance().lockScreen(true);
			UIHelper.getInstance().getDrawBoxActivity().finish();
			File f = new File("/sdcard/", "temp.jpg");
			if(f.exists()){
				f.delete();
				LogUtil.d("删除本地保存的作业");
			}
		}
	}
}
