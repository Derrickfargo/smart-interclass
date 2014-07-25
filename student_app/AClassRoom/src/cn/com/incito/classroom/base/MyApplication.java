package cn.com.incito.classroom.base;

import android.content.Intent;
import cn.com.incito.classroom.service.SocketService;

import com.popoy.common.TAApplication;

public class MyApplication extends TAApplication {
	@Override
	public void onCreate() {
		super.onCreate();
		initApplication();
	}

	private void initApplication() {
		Intent service = new Intent(this,SocketService.class);  
		startService(service);
	}
}
