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
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.Log;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.utils.ApiClient;
import cn.com.incito.classroom.utils.FileUtils;
import cn.com.incito.common.utils.AndroidUtil;

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
		Log.e("程序挂掉了","");
		// 把错误的堆栈信息 获取出来
		final String errorinfo = getErrorInfo(arg1);
		// 打印到控制台上异常信息
		System.err.println(errorinfo);
		// 获取异常出现的时间
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
		final String currentTimeString = format1.format(new Date(System.currentTimeMillis()));
		// 获取当前应用名称
		final String _ApplicationName = AndroidUtil.getApplicationName(context);
		// 获取当前应用版本号
		String _AppVersion = AndroidUtil.getAppVersion(context);
		// 获取当前应的屏幕大小
		// String _ScreenSize = AndroidUtil.getScreenSize((Activity) context);
		// 获取ChannelId "\nChannelId:" + _ChannelId +
		// int _ChannelId = AndroidUtil.getChannelId(context);
		// 获取当前设备ID
		String _DeviceId = AndroidUtil.getDeviceId(context);
		// 获取当前android版本
		String _Version = AndroidUtil.getVersion();
		// 获取当前手机屏幕密度
		// String _Dpi = AndroidUtil.getDpi((Activity) context);+ "\n屏幕大小:" + _ScreenSize +
		// "\n屏幕密度:" + _Dpi
		String _Model = AndroidUtil.getModel();
		String _UserAgent = AndroidUtil.getUserAgent();
		final String _AppInfo = "应用名称:" + _ApplicationName + "\n应用版本号:" + _AppVersion + "\n设备ID:" + _DeviceId + "\nAndroid版本:" + _Version + "\nModel:" + _Model + "\nUserAgent:" + _UserAgent + "\n";

		if (AndroidUtil.isSDCardAvailable()) {
			FileUtils.writeFileToSDCardFromString(_AppInfo, _ApplicationName + ".txt");
			Log.i("写入SD卡",_ApplicationName+".txt");
		}
		if (AndroidUtil.isNetworkAvailable(context)) {
			ApiClient.uploadErrorLog(errorinfo);
		}

		AppManager.getAppManager().AppExit(context);;
		android.os.Process.killProcess(android.os.Process.myPid());
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