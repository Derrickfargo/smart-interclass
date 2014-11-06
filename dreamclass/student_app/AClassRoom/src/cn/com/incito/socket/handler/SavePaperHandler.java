package cn.com.incito.socket.handler;

import android.util.Log;
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
	private static final String TAG=SavePaperHandler.class.getSimpleName();
	public static final Logger Logger = LoggerFactory.getLogger();
	@Override
	protected void handleMessage() {
		if(MyApplication.getInstance().isSubmitPaper()){
			MyApplication.getInstance().setSubmitPaper(false);
		}else{
			int delay = data.getIntValue("delay");
			Logger.debug(Utils.getTime()+TAG+":作业延迟时间:" + delay);
			Log.i(TAG, "作业延迟时间:" + delay);
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(UIHelper.getInstance().getDrawBoxActivity()!=null){
				UIHelper.getInstance().getDrawBoxActivity().submitPaper();
			}
		}
	}
}
