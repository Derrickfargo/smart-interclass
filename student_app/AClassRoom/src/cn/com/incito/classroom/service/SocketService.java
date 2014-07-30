package cn.com.incito.classroom.service;

import cn.com.incito.classroom.transition.SocketMinaClient;
import cn.com.incito.socket.core.CoreSocket;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SocketService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		CoreSocket.getInstance().start();
//        SocketMinaClient.getInstance().start();
	}

	
}
