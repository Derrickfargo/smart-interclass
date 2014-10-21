package cn.com.incito.socket.handler;

import java.nio.ByteBuffer;

import android.provider.Settings;
import android.app.ExecRootCmd;
import android.content.ContentResolver;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.sdk.log.WLog;

public class LockScreenHandler extends MessageHandler {

	public final static String TAG = LockScreenHandler.class.getSimpleName();

	private String isLock;

	@Override
	public void handleMessage(Message msg) {
		ByteBuffer buffer = msg.getBodyBuffer();
		buffer.flip();
		byte[] intSize = new byte[4];// int
		buffer.get(intSize);
		long idLength = BufferUtils.decodeIntLittleEndian(intSize, 0, intSize.length);
		byte[] idByte = new byte[(int) idLength];
		buffer.get(idByte);
		isLock = BufferUtils.readUTFString(idByte);
		handleMessage();
	}

	@Override
	protected void handleMessage() {
		WLog.i(LockScreenHandler.class,"收到锁屏信息");
		if (isLock.equals("true")) {
				MyApplication.getInstance().setOnClass(true);
				MyApplication.getInstance().lockScreen(true);
		} else if(isLock.equals("false")){
			MyApplication.getInstance().lockScreen(false);
		}else{
			MyApplication.getInstance().setOnClass(false);
			MyApplication.getInstance().lockScreen(false);
			AppManager.getAppManager().AppExit(MyApplication.getInstance());
		}
	}

}
