package cn.com.incito.classroom.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.content.Context;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.common.utils.LogUtil;

/**
 * ftp工具类
 * 
 * @author john
 *
 */
public class FTPUtils {
	private static FTPClient ftpClient = null;
	private static FTPUtils ftpUtilsInstance = null;
	private static String FTPUrl = Constants.getIP();

	public static String getFTPUrl() {
		return FTPUrl;
	}

	public static void setFTPUrl(String fTPUrl) {
		FTPUrl = fTPUrl;
	}

	private static int FTPPort = 9000;
	private static String UserName = "admin";
	private static String UserPassword = "admin";

	private FTPUtils() {
		if (ftpClient == null) {
			ftpClient = new FTPClient();
		}
	}

	public static FTPUtils getInstance() {
		if (ftpUtilsInstance == null) {
			synchronized (FTPUtils.class) {
				if (ftpUtilsInstance == null) {
					ftpUtilsInstance = new FTPUtils();
				}
			}
		}
		return ftpUtilsInstance;
	}

	public static boolean initFTPSetting(String FTPUrl, int FTPPort,String UserName, String UserPassword) {
		int reply;
		try { // 1.需要连接的Url,Port
			LogUtil.d("FTP服务器初始化连接" + "地址：" + FTPUrl + "端口号："+ FTPPort);
			ftpClient.connect(FTPUrl, FTPPort);
			// 2.登陆服务端
			LogUtil.d("FTP服务器初始化连接" + "用户名：" + UserName);
			ftpClient.login(UserName, UserPassword); //
			ftpClient.setBufferSize(1024 * 4);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.enterLocalPassiveMode();
			ftpClient.setKeepAlive(true);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // 设置为2进制
			reply = ftpClient.getReplyCode();
			LogUtil.d("FTP服务器初始化连接,返回数据：" + reply);
			if (!FTPReply.isPositiveCompletion(reply)) { // 连接不成功
				LogUtil.d("FTP服务器初始化连接失败");
				ftpClient.logout();
				ftpClient.disconnect();
				return false;
			}
		} catch (Exception e) {
			LogUtil.e("初始化ftp服务器出现异常:", e.getCause());
			return false;
		}
		return true;
	}

	public boolean uploadFile(String FilePath, String FileName) {
		boolean isSucces = false;
		try {
			if (!ftpClient.isConnected()) {
				LogUtil.d("FTP服务器未连接");
				if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {
					LogUtil.d("ftp服务器连接失败");
					return false;
				}
			}
			LogUtil.d("ftp服务器已连接");
			ftpClient.makeDirectory(FilePath);
			ftpClient.changeWorkingDirectory(FilePath); // 跳转到服务端目录
			FTPFile[] files = ftpClient.listFiles();
			for (FTPFile file : files) {
				if (file.getName().equals(FileName)) {
					if (ftpClient.deleteFile(Constants.FILE_PATH + FilePath+ "/" + FileName))
						LogUtil.d("ftp服务器上删除旧文件成功");
				}
			}
			FileInputStream fileInputStream = new FileInputStream(new File("/sdcard/temp.jpg"));
			isSucces = ftpClient.storeFile(FileName, fileInputStream);
			fileInputStream.close();
			ftpClient.logout();
			ftpClient.disconnect();
		} catch (Exception e) { 
			LogUtil.e("出现异常", e.getCause());
			isSucces = false;
		} finally {
			try {
				ftpClient.disconnect();
			} catch (Exception e) {
				LogUtil.e("上传文件时出现异常", e.getCause());
			}
		}
		LogUtil.d("作业上传结果：isSuccess:" + isSucces);
		return isSucces;
	}

	/**
	 * 下载文件
	 * 
	 * @param FilePath
	 *            文件路径,不包含名字
	 * @param FileName
	 *            文件名称
	 * @return
	 */
	public static boolean downLoadFile(String FilePath, String FileName) {
		boolean isSuccess = false;
		try {
			if (!ftpClient.isConnected()) {
				if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {
					LogUtil.d("FTP服务器初始化连接失败");
					return false;
				}
			}
			FTPFile[] files = ftpClient.listFiles();
			for (FTPFile file : files) {
				if (file.getName().equals(FileName)) {
					File localFile = new File("/sdcard/" + FileName);
					if (localFile.exists()) {
						localFile.delete();
					}
					OutputStream outputStream = new FileOutputStream(localFile);
					isSuccess = ftpClient.retrieveFile(file.getName(), outputStream); // 耗时操作
					outputStream.close();
				}
			}
			ftpClient.logout();
			ftpClient.disconnect();
		} catch (Exception e) { 
			LogUtil.e("下载文件出现异常", e.getCause());
			isSuccess = false;
		} finally {
			try {
//				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				LogUtil.e("出现异常", e.getCause());
			}
		}
		LogUtil.d("作业下载结果：isSuccess:" + isSuccess);
		return isSuccess;
	}
	
	/**
	 * apk下载
	* @author hm
	* @date 2015年2月1日 下午2:17:56 
	* @Title: downLoadApk 
	* @Description:  
	* @param context
	* @param fileName
	* @return
	* @return boolean    返回类型 
	* @throws
	 */
	public static boolean downLoadApk(Context context,String fileName){
		boolean downLoadResult = false;
		try {
		if (!ftpClient.isConnected()) {
			LogUtil.d("FTP服务器未连接,开始连接fpt服务器");
			if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {
				return downLoadResult;
			}
		}
		LogUtil.d("ftp服务器已连接");
		ftpClient.changeWorkingDirectory("/update");
		FTPFile[] files = ftpClient.listFiles();
		for (FTPFile file : files) {
			if (file.getName().equals(fileName)) {
				File localFile = new File(FileUtils.getSDCardPath() + "/" + fileName);
				
				if (localFile.exists()) {
					localFile.delete();
				}
				OutputStream outputStream = new FileOutputStream(localFile);
				downLoadResult = ftpClient.retrieveFile(file.getName(), outputStream); // 耗时操作
				outputStream.close();
			}
		}
		    ftpClient.logout();
			ftpClient.disconnect();
		} catch (IOException e) { 
			LogUtil.e("下载更新包出现异常",e.getCause());
			downLoadResult = false;
		}finally{  
            try {  
                ftpClient.disconnect();
            } catch (IOException e) {  
            	LogUtil.e("ftp断开连接时出现异常",e.getCause());
            }  
        }  
		LogUtil.d("apk下载结果:" + downLoadResult);
		return downLoadResult;
	}
}
