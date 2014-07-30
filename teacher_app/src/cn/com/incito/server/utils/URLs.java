package cn.com.incito.server.utils;

public class URLs {
	
	public final static String HOST = "192.168.30.45:8080/app";

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
}
