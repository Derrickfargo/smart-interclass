package cn.com.incito.classroom.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.baidu.navisdk.util.common.StringUtils;

import android.os.Environment;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.exception.AppException;

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
			ApiClient.uploadErrorLog(e.getMessage());
			e.printStackTrace();
		}
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Host", Constants.HTTP + Constants.HOST);
		httpGet.setRequestHeader("Connection", "Keep-Alive");
		return httpGet;
	}

	private static PostMethod getHttpPost(String url) {
		PostMethod httpPost = new PostMethod(url);
		// 设置 请求超时时间
		// httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpPost.setRequestHeader("Host", Constants.HTTP + Constants.HOST);
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
						ApiClient.uploadErrorLog(e1.getMessage());
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				ApiClient.uploadErrorLog(e.getMessage());
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						ApiClient.uploadErrorLog(e1.getMessage());
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
	private static String _post(String url, Map<String, Object> params, Map<String, File> files) throws AppException {
		// System.out.println("post_url==> "+url);
		HttpClient httpClient = null;
		PostMethod httpPost = null;

		// post表单参数处理
		int length = (params == null ? 0 : params.size()) + (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
		if (params != null)
			for (String name : params.keySet()) {
				parts[i++] = new StringPart(name, String.valueOf(params.get(name)), UTF_8);
				// System.out.println("post_key==> "+name+"    value==>"+String.valueOf(params.get(name)));
			}
		if (files != null)
			for (String file : files.keySet()) {
				try {
					parts[i++] = new FilePart(file, files.get(file));
				} catch (FileNotFoundException e) {
					// ApiClient.uploadErrorLog(e.getMessage());
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
				httpPost.setRequestEntity(new MultipartRequestEntity(parts, httpPost.getParams()));
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
	 * APK更新
	 * 
	 * @return
	 * @throws AppException
	 */
	public static String updateApk(int code) throws AppException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", 2);
		params.put("code", code);
		try {
			return _post(Constants.URL_UPDATE_APK, params, null);
		} catch (Exception e) {
			ApiClient.uploadErrorLog(e.getMessage());
			if (e instanceof AppException)
				throw (AppException) e;
			throw AppException.network(e);
		}
	}

	/**
	 * 日志上传
	 * 
	 * @param reason
	 * @throws IOException 
	 */
	public static void uploadErrorLog(String reason)  {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 日志名称格式
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", 2);
		params.put("mac", MyApplication.deviceId == null ? "" : MyApplication.deviceId);
		params.put("reason", reason);
		Map<String, File> files = new HashMap<String, File>();
		File logFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyApp" + File.separator + "log" + File.separator + sdf.format(new Date()) + ".log");
		 	try {
				copyFile(logFile, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyApp" + File.separator + "log" + File.separator + "2014-1-1" + ".log"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		 	File mlogFile=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyApp" + File.separator + "log" + File.separator + "2014-1-1" + ".log");
			files.put("file", mlogFile);

		try {
			if(!StringUtils.isEmpty(Constants.SERVER_IP)&&!StringUtils.isEmpty(Constants.SERVER_PORT))
			_post(Constants.URL_UPLOAD_LOG, params, files);
			System.out.println(Constants.URL_UPLOAD_LOG);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * copy 文件到另一个文件
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);
		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();

		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}

}
