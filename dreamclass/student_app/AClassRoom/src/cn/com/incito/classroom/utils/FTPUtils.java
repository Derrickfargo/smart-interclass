package cn.com.incito.classroom.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;

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

	private static int FTPPort = 21;
	private static String UserName = "admin";
	private static String UserPassword = "admin";

	private FTPUtils() {
		if(ftpClient==null){
			ftpClient = new FTPClient();
		}
	}

	public static FTPUtils getInstance() {
		if (ftpUtilsInstance == null) {
			ftpUtilsInstance = new FTPUtils();
		}
		return ftpUtilsInstance;
	}

	public static boolean initFTPSetting(String FTPUrl, int FTPPort,
			String UserName, String UserPassword) {
		int reply;
		try { // 1.需要连接的Url,Port
			MyApplication.Logger.debug("FTP服务器初始化连接" + "地址：" + FTPUrl + "端口号："
					+ FTPPort);
			ftpClient.connect(FTPUrl, FTPPort);
			// 2.登陆服务端
			MyApplication.Logger.debug("FTP服务器初始化连接" + "用户名：" + UserName);
			ftpClient.login(UserName, UserPassword); //
			ftpClient.setBufferSize(1024*4);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.enterLocalPassiveMode();
			ftpClient.setKeepAlive(true);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // 设置为2进制
			reply = ftpClient.getReplyCode();
			MyApplication.Logger.debug("FTP服务器初始化连接,返回数据："+reply);
			if (!FTPReply.isPositiveCompletion(reply)) { // 连接不成功
				MyApplication.Logger.debug("FTP服务器初始化连接失败");
				ftpClient.logout();
				ftpClient.disconnect();
				return false;
			}
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean uploadFile(String FilePath, String FileName) {
		try {
			if (!ftpClient.isConnected()) {
				MyApplication.Logger.debug("FTP服务器未连接");
				if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {
					MyApplication.Logger.debug("服务器连接失败");
					return false;
				}
			}
			MyApplication.Logger.debug("服务器已连接");
			ftpClient.makeDirectory(FilePath);
			ftpClient.changeWorkingDirectory(FilePath); // 跳转到服务端目录
			FTPFile[] files = ftpClient.listFiles();
			for (FTPFile file : files) {
				if (file.getName().equals(FileName)) {
					if (ftpClient.deleteFile(Constants.FILE_PATH + FilePath
							+ "/" + FileName))
						System.out.println("删除旧文件成功");
				}
			}
			FileInputStream fileInputStream = new FileInputStream(new File("/sdcard/temp.jpg"));
			ftpClient.storeFile(FileName, fileInputStream); //
			fileInputStream.close(); //
			ftpClient.logout();
			ftpClient.disconnect();
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
			return false;
		}finally{  
            try {  
            	ftpClient.logout();
                ftpClient.disconnect();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
		return true;
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
		try {
		if (!ftpClient.isConnected()) {
			if (!initFTPSetting(FTPUrl, FTPPort, UserName, UserPassword)) {
				MyApplication.Logger.debug("FTP服务器初始化连接失败");
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
					System.out.println(localFile.getAbsolutePath());
					OutputStream outputStream = new FileOutputStream(localFile);
					ftpClient.retrieveFile(file.getName(), outputStream); // 耗时操作
					outputStream.close();
				}
			}
			ftpClient.logout();
			ftpClient.disconnect();
		} catch (IOException e) { // TODO Auto-generated
			e.printStackTrace();
		}finally{  
            try {  
            	ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
		return true;
	}

	// public static void main(String[] args) {
	// FTPUtils.getInstance();
	// downLoadFile("ftp_home","temp.png");
	// }
}
