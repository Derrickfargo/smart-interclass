package cn.com.incito.classroom.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.wisdom.sdk.log.WLog;

/**
 * socket服务Service，保持通信连接 Created by liushiping on 2014/7/28.
 */
public class SocketService extends Service {

	public static final String NETWORK_RECEIVER = "cn.com.incito.network.RECEIVER";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		CoreSocket.getInstance().start();
		WLog.i(SocketService.class, "socket started");
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("SocketService.onDestroy");
		WLog.i(SocketService.class, "socket disconnected");
	}
}
