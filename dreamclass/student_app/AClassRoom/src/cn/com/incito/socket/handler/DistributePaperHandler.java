package cn.com.incito.socket.handler;

import android.app.Dialog;
import android.content.Context;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.ui.widget.FtpReconnectDialog;
import cn.com.incito.classroom.utils.FTPUtils;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 试卷分发 hanlder Created by popoy on 2014/7/28.
 */
public class DistributePaperHandler extends MessageHandler {
	private String isContainsPic;

	@Override
	protected void handleMessage() {
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":DistributePaperHandler收到作业消息");
		
		String uuid = data.getString("uuid");
		MyApplication.getInstance().setQuizID(uuid);
		isContainsPic = data.getString("isContainsPic");
		
		if ("true".equals(isContainsPic)) {// 有图片,利用ftp去下载图片
			FTPUtils.getInstance();
			if (!FTPUtils.downLoadFile(Constants.FILE_PATH, Constants.FILE_NAME)) {// 下载不成功
				Dialog dialog = new FtpReconnectDialog((Context) AppManager.getAppManager().currentActivity());
				dialog.show();
			}
		}

		MyApplication.getInstance().lockScreen(false);
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "收到作业");
		UIHelper.getInstance().showDrawBoxActivity(isContainsPic);
	}
}
