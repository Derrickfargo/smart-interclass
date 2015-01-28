package cn.com.incito.classroom.exception;

/**
 * Copyright (c) 2014 (RJT). All Rights Reserved.
 * FileName : AppException.java
 * Description : Define the exception of this application which recorded in log and shown when debug
 * or trace.
 */


import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.util.Log;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.utils.ApiClient;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.LogUtil;

/**
 * Description : Define the exception of this application which recorded in log
 * and shown when debug or trace.
 * Author : Six.Sama
 * Version : 1.0
 */
public class AppUncaughtException implements UncaughtExceptionHandler {
	private static AppUncaughtException appException;

	private Context context;

	private AppUncaughtException() {

	}

	public static synchronized AppUncaughtException getInstance() {
		if (appException == null) {
			appException = new AppUncaughtException();
		}
		return appException;

	}

	public void init(Context context) {
		this.context = context;
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
	
//		SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences();
//		preferences.edit().putBoolean("isNormalExit", false).commit();
		Log.e("程序挂掉了","");
		// 把错误的堆栈信息 获取出来
		LogUtil.d(getErrorInfo(arg1));
//		MyApplication.Logger.debug(Utils.getTime()+"AppUncaughtException:"+getErrorInfo(arg1));
		String errorInfo=arg1.getMessage();
		// 打印到控制台上异常信息
		if (AndroidUtil.isNetworkAvailable(context)) {
			ApiClient.uploadErrorLog(errorInfo);
		}
		LogUtil.d(errorInfo);
//		MyApplication.Logger.debug("AppUncaughtException:"+errorInfo);
	}

	/**
	 * @Title: getErrorInfo
	 * @Description: TODO
	 * @param arg1
	 * @return 设定文件
	 *         String 返回类型
	 * @throws
	 */
	private String getErrorInfo(Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		String error = writer.toString();
		return error;
	}

}