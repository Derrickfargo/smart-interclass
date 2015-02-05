package cn.com.incito.socket.handler;

import android.os.Looper;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.ui.activity.DrawBoxActivity;
import cn.com.incito.classroom.utils.FTPUtils;
import cn.com.incito.common.utils.LogUtil;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.util.SendMessageUtil;

/**
 * 试卷分发 hanlder Created by popoy on 2014/7/28.
 */
public class DistributePaperHandler extends MessageHandler {
	private String isContainsPic;

	@Override
	protected void handleMessage() {
		SendMessageUtil.sendReplyRecivePaper();
		String uuid = data.getString("uuid");
		MyApplication.getInstance().setQuizID(uuid);
		isContainsPic = data.getString("isContainsPic");
		
		/**
		 * 有图片,利用ftp去下载图片
		 */
		if ("true".equals(isContainsPic)) { 
			LogUtil.d("老师截图发送作业,马上开始下载作业!");
			FTPUtils.getInstance();
			if (!FTPUtils.downLoadFile(Constants.FILE_PATH, Constants.FILE_NAME)) {
				LogUtil.d("老师截图发送作业,下载作业失败!");
				BaseActivity activity = (BaseActivity) AppManager.getAppManager().currentActivity();
				Looper.prepare();
				activity.showRequestTecherSendPaper();
				Looper.loop();
				return;
			}
		}
		if(DrawBoxActivity.class.equals(activity.getClass())){
			LogUtil.d("当前是做作业界面,只更换背景");
			((DrawBoxActivity)activity).replaceBackGround(isContainsPic);
			
		}else{
			finishNotWaitingActivity();
			UIHelper.getInstance().showDrawBoxActivity(isContainsPic);
		}
	}
}
