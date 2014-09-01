package cn.com.incito.socket.handler;

import java.nio.ByteBuffer;

import android.provider.Settings;
import android.app.ExecRootCmd;
import android.content.ContentResolver;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.sdk.log.WLog;

public class LockScreenHandler extends MessageHandler {
	public final static String TAG=LockScreenHandler.class.getSimpleName(); 
	private String isLock;

	@Override
	public void handleMessage(Message msg) {
		ByteBuffer buffer = msg.getBodyBuffer();
		buffer.flip();
		byte[] intSize = new byte[4];// int
		buffer.get(intSize);
		long idLength = BufferUtils.decodeIntLittleEndian(intSize, 0,
				intSize.length);
		byte[] idByte = new byte[(int) idLength];
		buffer.get(idByte);
		isLock=BufferUtils.readUTFString(idByte);
		handleMessage();
	}

	@Override
	protected void handleMessage() {
		if(Constants.OPEN_LOCK_SCREEN){
			MyApplication.getInstance().setLockScreen(true);
			WLog.i(LockScreenHandler.class, "是否收到解锁屏信息：" + isLock);
			ContentResolver mContentResolver = UIHelper.getInstance().getWaitingActivity().getApplicationContext().getContentResolver();
			ExecRootCmd execRootCmd = new ExecRootCmd();
			if (isLock.equals("true")) {
				execRootCmd.powerkey();
				boolean ret = Settings.Global.putInt(mContentResolver, "disable_powerkey", 1);// 屏蔽电源按钮唤醒功能
				execRootCmd.powerkey();
			} else {
				boolean ret1 = Settings.Global.putInt(mContentResolver, "disable_powerkey", 0); // 打开电源按钮唤醒功能
				execRootCmd.powerkey();

			}
		}
	}

}
