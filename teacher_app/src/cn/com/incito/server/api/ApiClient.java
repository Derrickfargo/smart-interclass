package cn.com.incito.server.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.exception.AppException;
import cn.com.incito.server.utils.JSONUtils;
import cn.com.incito.server.utils.Md5Utils;
import cn.com.incito.server.utils.URLs;

/**
 * API客户端接口：用于访问网络数据
 * 
 * @author 刘世平
 * @version 1.0
 * @created 2014-3-21
 */
public class ApiClient {

	public static final String UTF_8 = "UTF-8";

	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 3;


	private static HttpClient getHttpClient() {
		HttpClient httpClient = new HttpClient();
		// 设置 默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		// 设置 连接超时时间
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
		// 设置 读数据超时时间
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}

	private static GetMethod getHttpGet(String url) {
		GetMethod httpGet = null;
		try {
			httpGet = new GetMethod(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Host", URLs.HOST);
		httpGet.setRequestHeader("Connection", "Keep-Alive");
		return httpGet;
	}

	private static PostMethod getHttpPost(String url) {
		PostMethod httpPost = new PostMethod(url);
		// 设置 请求超时时间
//		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpPost.setRequestHeader("Host", URLs.HOST);
		httpPost.setRequestHeader("Connection", "Keep-Alive");
		return httpPost;
	}

	private static String _MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?") < 0)
			url.append('?');

		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
		}

		return url.toString().replace("?&", "?");
	}

	/**
	 * get请求URL
	 * 
	 * @param url
	 * @throws AppException
	 */
	private static String http_get(String url) throws AppException {
		HttpClient httpClient = null;
		GetMethod httpGet = null;

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				}
				responseBody = httpGet.getResponseBodyAsString();
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		// responseBody = responseBody.replaceAll("\\p{Cntrl}", "\r\n");
		return responseBody;
	}

	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	private static String _post(String url,
			Map<String, Object> params, Map<String, File> files)
			throws AppException {
		// System.out.println("post_url==> "+url);
		HttpClient httpClient = null;
		PostMethod httpPost = null;

		// post表单参数处理
		int length = (params == null ? 0 : params.size())
				+ (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
		if (params != null)
			for (String name : params.keySet()) {
				parts[i++] = new StringPart(name, String.valueOf(params
						.get(name)), UTF_8);
				// System.out.println("post_key==> "+name+"    value==>"+String.valueOf(params.get(name)));
			}
		if (files != null)
			for (String file : files.keySet()) {
				try {
					parts[i++] = new FilePart(file, files.get(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				// System.out.println("post_key_file==> "+file);
			}

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpPost = getHttpPost(url);
				httpPost.setRequestEntity(new MultipartRequestEntity(parts,
						httpPost.getParams()));
				int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				}
				responseBody = httpPost.getResponseBodyAsString();
				// System.out.println("XMLDATA=====>"+responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		return responseBody;
	}

	/**
	 * 教师登陆
	 * @param mac
	 * @param uname
	 * @param password
	 * @return
	 * @throws AppException
	 */
	public static String loginForTeacher(String mac, String uname,
			String password) throws AppException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mac", mac);
		params.put("uname", uname);
		params.put("password", Md5Utils.md5(password));
		try {
			return _post(URLs.URL_TEACHER_LOGIN, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	
	/**
	 * 判断设备是否已绑定
	 * @param imei
	 * @param roomId
	 * @return
	 * @throws AppException
	 */
	public static String isDeviceBind(String imei, int roomId) throws AppException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("imei", imei);
		params.put("roomId", roomId);
		try {
			return _post(URLs.URL_DEVICE_HAS_BIND, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	
	/**
	 * 设备绑定
	 * @param imei
	 * @param number
	 * @param roomId
	 * @return
	 * @throws AppException
	 */
	public static String deviceBind(String imei, int number) throws AppException{
		Application app = Application.getInstance();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("imei", imei);
		params.put("number", number);
		params.put("roomId", app.getRoom().getId());
		params.put("teacherId", app.getTeacher().getId());
		params.put("courseId", app.getCourse().getId());
		params.put("classId", app.getClasses().getId());
		try {
			return _post(URLs.URL_DEVICE_BIND, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	
	/**
	 * 获得分组列表
	 * @param schoolId
	 * @param roomId
	 * @param teacherId
	 * @param courseId
	 * @param classId
	 * @param className
	 * @return
	 * @throws AppException
	 */
	public static String getGroupList(int schoolId, int roomId,int teacherId, int courseId, int classId,
			String className) throws AppException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("schoolId", schoolId);
		params.put("roomId", roomId);
		params.put("teacherId", teacherId);
		params.put("courseId", courseId);
		params.put("classId", classId);
		params.put("className", className);
		try {
			return _post(URLs.URL_TEACHER_GROUP, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	
	/**
	 * 注册学生走这个api
	 * @param name
	 * @param sex
	 * @param number
	 * @param imei
	 * @return
	 * @throws AppException
	 */
	public static String loginForStudent(String name,int sex ,String number,String imei) throws AppException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", Application.getInstance().getCourse().getId());
		params.put("classId", Application.getInstance().getClasses().getId());
		params.put("teacherId", Application.getInstance().getTeacher().getId());
		Device device = Application.getInstance().getImeiDevice().get(imei);
		if (device == null) {
			// 系统中无此设备
			return JSONUtils.renderJSONString(1);// 失败
		}
		Table table = Application.getInstance().getDeviceTable().get(device.getId());
		if (table == null) {
			// 此设备未绑定课桌
			return JSONUtils.renderJSONString(2);// 失败
		}
		params.put("tableId", table.getId());
		params.put("name", name);
		params.put("sex", sex);
		params.put("number", number);
		params.put("imei", imei);
		try {
			return _post(URLs.URL_STUDENT_LOGIN, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	
	public static String updateGroup(int id, String name, String logo) throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("name", name);
		params.put("logo", logo);
		try {
			return _post(URLs.URL_UPDATE_GROUP, params, null);
		} catch (Exception e) {
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}
	
	public static void uploadErrorLog(String reason){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", 1);
		params.put("mac", Application.getInstance().getMac());
		params.put("reason", reason);
		Map<String, File> files = new HashMap<String, File>();
		files.put("file", new File("log/teacher.log"));
		try {
			_post(URLs.URL_UPLOAD_LOG, params, files);
		} catch (Exception e) {
			
		}
	}
	
}
