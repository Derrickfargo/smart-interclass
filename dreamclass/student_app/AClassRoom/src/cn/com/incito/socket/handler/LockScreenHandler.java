package cn.com.incito.socket.handler;

import java.nio.ByteBuffer;

import android.util.Log;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.utils.BufferUtils;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

public class LockScreenHandler extends MessageHandler {

	public final static String TAG = LockScreenHandler.class.getSimpleName();

	private String isLock;
	public static final Logger Logger = LoggerFactory.getLogger();
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
		Logger.debug(Utils.getTime()+TAG+":收到锁屏信息:"+isLock);
		Log.i(TAG,"收到锁屏信息:"+isLock);
		String currentActivityName = AppManager.getAppManager().currentActivity().getClass().getSimpleName();
		if (isLock.equals("true")) {
				if("RandomGroupActivity".equals(currentActivityName)){
					AppManager.getAppManager().finishActivity();
				}
				MyApplication.getInstance().setOnClass(true);
				MyApplication.getInstance().lockScreen(true);
		} else if(isLock.equals("false")){
			MyApplication.getInstance().lockScreen(false);
		}else{
			MyApplication.getInstance().lockScreen(false);
			MyApplication.getInstance().setOnClass(false);
			AppManager.getAppManager().AppExit(MyApplication.getInstance());
		}
	}

}
