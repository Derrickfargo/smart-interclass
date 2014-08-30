package cn.com.incito.socket.handler;

import android.util.Log;
import cn.com.incito.socket.core.ConnectionManager;
import cn.com.incito.socket.core.MessageHandler;
/**
 * 心跳处理hanlder
 * Created by liushiping on 2014/7/28.
 */
public class HeartbeatHandler extends MessageHandler {

	@Override
	protected void handleMessage() {
		//收到心跳回复
		Log.d("HeartbeatHandler", "收到心跳回复!");
		ConnectionManager.getInstance(message.getChannel()).heartbeat();
	}

}
