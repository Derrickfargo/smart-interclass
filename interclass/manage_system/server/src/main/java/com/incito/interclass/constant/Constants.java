package com.incito.interclass.constant;

import java.io.File;

/**
 * Created with IntelliJ IDEA. User: popoy Date: 14-8-15 Time: 下午3:12 To change
 * this template use File | Settings | File Templates.
 */
public class Constants {

	/**
	 * 作业存放目录
	 */
	public static final String PAPER_DIR = File.separator + "mnt"
			+ File.separator + "dreamclass" + File.separator + "paper";
	
	/**
	 * 课件存放目录
	 */
	public static final String COURSEWARE_DIR = File.separator + "mnt"
			+ File.separator + "adream" + File.separator + "courseware";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 服务端日志存放目录
	 */
	public static final String LOG_DIR = File.separator + "mnt"
			+ File.separator + "dreamclass" + File.separator + "log";
	
	public static final String STUDENT_DIR = File.separator + "mnt"
			+ File.separator + "dreamclass" + File.separator + "student";
	
	/**
	 * 服务端版本存放目录
	 */
	public static final String VERSION_DIR = File.separator + "mnt"
			+ File.separator + "dreamclass" + File.separator + "version";
}