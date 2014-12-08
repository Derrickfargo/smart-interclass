package cn.com.incito.socket.handler;

import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * 试卷提交保存hanlder
 * Created by liguangming on 2014/7/28.
 */
public class SavePaperHandler extends MessageHandler {

	private static final String TAG = SavePaperHandler.class.getSimpleName();

	public static final Logger Logger = LoggerFactory.getLogger();

	@Override
	protected void handleMessage() {
		if (MyApplication.getInstance().isSubmitPaper()) {
			MyApplication.getInstance().setSubmitPaper(false);
		} else {
			Logger.debug(Utils.getTime() + TAG + "发送作业开始:");
			if (UIHelper.getInstance().getDrawBoxActivity() != null && AppManager.getAppManager().currentActivity().getClass().getSimpleName().equals("DrawBoxActivity")) {
				UIHelper.getInstance().getDrawBoxActivity().sendPaper();
				;
			}
		}
	}
}
