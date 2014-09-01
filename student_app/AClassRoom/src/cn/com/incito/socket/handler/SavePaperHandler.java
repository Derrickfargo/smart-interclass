package cn.com.incito.socket.handler;

import android.app.ExecRootCmd;
import android.content.ContentResolver;
import android.provider.Settings;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.wisdom.sdk.log.WLog;
/**
 * 试卷提交保存hanlder
 * Created by liguangming on 2014/7/28.
 */
public class SavePaperHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		if(MyApplication.getInstance().isSubmitPaper()){
			MyApplication.getInstance().setSubmitPaper(false);
		}else{
			if(UIHelper.getInstance().getDrawBoxActivity()!=null){
				UIHelper.getInstance().getDrawBoxActivity().submitPaper();
				if (Constants.OPEN_LOCK_SCREEN) {
					if (!MyApplication.getInstance().isLockScreen()) {
						WLog.i(DistributePaperHandler.class, "提交作业后锁定屏幕" );
						ContentResolver mContentResolver = UIHelper.getInstance().getWaitingActivity().getApplicationContext().getContentResolver();
						ExecRootCmd execRootCmd = new ExecRootCmd();
						boolean ret1 = Settings.Global.putInt(mContentResolver, "disable_powerkey", 1); // 锁定屏幕
						execRootCmd.powerkey();
						MyApplication.getInstance().setLockScreen(true);
					}
				}
			}
		}
	}
}
