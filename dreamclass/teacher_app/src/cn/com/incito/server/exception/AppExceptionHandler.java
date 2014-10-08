package cn.com.incito.server.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import cn.com.incito.server.api.ApiClient;

public class AppExceptionHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		ApiClient.uploadErrorLog(e.toString());
	}

}
