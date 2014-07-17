package cn.com.incito.server.utils;

public class URLs {
	
	public final static String HOST = "localhost:8080/InterClass-admin";
	public final static String HTTP = "http://";
	private final static String URL_SPLITTER = "/";
	private final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER;
	
	/**
	 * 老师登陆
	 */
	public final static String URL_TEACHER_LOGIN = URL_API_HOST + "api/teacher_login";
	
	/**
	 * 学生登陆
	 */
	public final static String URL_STUDENT_LOGIN = URL_API_HOST + "api/student_login";
}
