package cn.com.incito.classroom.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import cn.com.incito.socket.core.CoreSocket;

import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

/**
 * socket服务Service，保持通信连接 Created by liushiping on 2014/7/28.
 */
public class SocketService extends Service {
	public static final Logger Logger = LoggerFactory.getLogger();
	public static final String NETWORK_RECEIVER = "cn.com.incito.network.RECEIVER";
	private ExecutorService exec;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		exec = Executors.newCachedThreadPool();
		exec.execute(CoreSocket.getInstance());
		Logger.debug("socket started");
		Log.i("SocketService", "socket started");
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		Logger.debug("socket disconnected");
		Log.i("SocketService", "socket disconnected");
	}
}
