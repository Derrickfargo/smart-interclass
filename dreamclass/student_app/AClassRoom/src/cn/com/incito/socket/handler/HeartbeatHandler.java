package cn.com.incito.socket.handler;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

import android.util.Log;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.socket.core.ConnectionManager;
import cn.com.incito.socket.core.MessageHandler;

/**
 * 心跳处理hanlder Created by liushiping on 2014/7/28.
 */
public class HeartbeatHandler extends MessageHandler {
	private static final String TAG=HeartbeatHandler.class.getSimpleName();
	public static final Logger Logger = LoggerFactory.getLogger();
	@Override
	protected void handleMessage() {
		// 收到心跳回复
		Logger.debug(Utils.getTime()+TAG+":收到服务端心跳包!");
		Log.i(TAG, "收到服务端心跳包!");
		ConnectionManager.getInstance(message.getChannel()).heartbeat();
	}

}
