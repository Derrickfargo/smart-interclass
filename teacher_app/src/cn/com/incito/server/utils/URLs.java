package cn.com.incito.server.utils;

public class URLs {
	
	public final static String HOST = "192.168.30.8:8080/app";

	public final static String HTTP = "http://";
	private final static String URL_SPLITTER = "/";
	private final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER;
	
	/**
	 * 老师登陆
	 */
	public final static String URL_TEACHER_LOGIN = URL_API_HOST + "api/teacher/login";
	
	/**
	 * 获取分组
	 */
	public final static String URL_TEACHER_GROUP = URL_API_HOST + "api/teacher/group";
	
	/**
	 * 学生注册
	 */
	public final static String URL_STUDENT_LOGIN = URL_API_HOST + "api/student/login";
	
	/**
	 * 判断设备是否已绑定
	 */
	public final static String URL_DEVICE_HAS_BIND = URL_API_HOST + "api/table/hasBind";
	
	/**
	 * 绑定课桌
	 */
	public final static String URL_DEVICE_BIND = URL_API_HOST + "api/table/bind";
}
