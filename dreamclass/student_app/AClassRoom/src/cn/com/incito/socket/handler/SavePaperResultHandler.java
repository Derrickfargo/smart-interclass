package cn.com.incito.socket.handler;

import android.util.Log;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 试卷提交结果处理hanlder Created by liguangming on 2014/7/28.
 */
public class SavePaperResultHandler extends MessageHandler {
	private static final String TAG=SavePaperResultHandler.class.getSimpleName();
	public static final Logger Logger = LoggerFactory.getLogger();
	@Override
	protected void handleMessage() {
		if (data.getIntValue("code") == 0) {
			Logger.debug(Utils.getTime()+TAG+":作业保存成功");
			Log.i(TAG, "paper保存成功");
			MyApplication.getInstance().lockScreen(true);
			MyApplication.getInstance().setSubmitPaper(true);
		}
	}

}
