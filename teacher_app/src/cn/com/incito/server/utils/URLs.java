package cn.com.incito.server.utils;

public class URLs {

//	public final static String HOST = "192.168.1.187:9090/app";
	public final static String HOST = "localhost:8080/app";
	public final static String HTTP = "http://";
	private final static String URL_SPLITTER = "/";
	private final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER;

	
	/**
	 * 分数奖励
	 */
	public final static String URL_UPDATE_SCORE = URL_API_HOST
			+ "api/student/changepoint";
	
	/**
	 * 勋章奖励
	 */
	 public final static String URL_UPDATE_MEDALS = URL_API_HOST
			+ "api/student/medals";
	
	/**
	 * 老师登陆
	 */
	public final static String URL_TEACHER_LOGIN = URL_API_HOST
			+ "api/teacher/login";

	/**
	 * 获取分组
	 */
	public final static String URL_TEACHER_GROUP = URL_API_HOST
			+ "api/teacher/group";

	/**
	 * 学生注册
	 */
	public final static String URL_STUDENT_LOGIN = URL_API_HOST
			+ "api/student/login";

	/**
	 * 判断设备是否已绑定
	 */
	public final static String URL_DEVICE_HAS_BIND = URL_API_HOST
			+ "api/table/hasBind";

	/**
	 * 绑定课桌
	 */
	public final static String URL_DEVICE_BIND = URL_API_HOST
			+ "api/table/bind";

	/**
	 * 更新组信息
	 */
	public final static String URL_UPDATE_GROUP = URL_API_HOST
			+ "api/group/update";

	/**
	 * 随堂作业云同步
	 */
	public final static String URL_CLOUD_SYN_ADD = URL_API_HOST
			+ "api/paper/upload";
}
