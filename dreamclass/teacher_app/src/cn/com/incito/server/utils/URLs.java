package cn.com.incito.server.utils;

import java.util.Properties;

import cn.com.incito.server.config.AppConfig;

public class URLs {
	private static String IP = "61.155.215.91";
	private static String PORT = "9090";
	static {
		Properties props = AppConfig.getProperties();
		String ip = props.getProperty(AppConfig.CONF_SERVER_IP);
		if (ip != null && !ip.equals("")) {
			IP = ip;
		}
		String port = props.getProperty(AppConfig.CONF_SERVER_PORT);
		if (port != null && !port.equals("")) {
			PORT = port;
		}
	}

	public final static String HTTP = "http://";
	private final static String URL_SPLITTER = "/";
	public final static String HOST = IP + ":" + PORT + "/app";
	private final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER;
	
	/**
	 * 分数奖励
	 */
	public static String URL_UPDATE_SCORE = URL_API_HOST + "api/student/changepoint";
	
	/**
	 * 勋章奖励
	 */
	 public static String URL_UPDATE_MEDALS = URL_API_HOST + "api/student/medals";
	
	/**
	 * 老师登陆
	 */
	public static String URL_TEACHER_LOGIN = URL_API_HOST + "api/teacher/login";

	/**
	 * 获取分组
	 */
	public static String URL_TEACHER_GROUP = URL_API_HOST + "api/teacher/group";

	/**
	 * 学生注册
	 */
	public static String URL_STUDENT_LOGIN = URL_API_HOST + "api/student/login";

	/**
	 * 判断设备是否已绑定
	 */
	public static String URL_DEVICE_HAS_BIND = URL_API_HOST + "api/table/hasBind";

	/**
	 * 绑定课桌
	 */
	public static String URL_DEVICE_BIND = URL_API_HOST + "api/table/bind";

	/**
	 * 更新组信息
	 */
	public static String URL_UPDATE_GROUP = URL_API_HOST + "api/group/update";

	/**
	 * 随堂作业云同步
	 */
	public static String URL_CLOUD_SYN_ADD = URL_API_HOST + "api/paper/upload";
	
	/**
	 * 上传日志文件
	 */
	public static String URL_UPLOAD_LOG = URL_API_HOST + "api/log/save";
	
	/**
	 * 检查是否存在更新
	 */
	public static String URL_CHECK_UPDATE = URL_API_HOST + "api/version/check";
	
	/**
	 * 下载更新
	 */
	public static String URL_DOWNLOAD_UPDATE = URL_API_HOST + "api/version/download?";
}
