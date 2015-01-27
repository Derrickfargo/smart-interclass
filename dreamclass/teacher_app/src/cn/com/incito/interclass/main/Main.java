package cn.com.incito.interclass.main;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import cn.com.incito.http.AsyncHttpConnection;
import cn.com.incito.http.StringResponseHandler;
import cn.com.incito.http.support.ParamsWrapper;
import cn.com.incito.interclass.po.Version;
import cn.com.incito.interclass.ui.Login;
import cn.com.incito.interclass.ui.RoomLogin1;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.exception.AppExceptionHandler;
import cn.com.incito.server.utils.URLs;
import cn.com.incito.server.utils.VersionUpdater;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 应用程序入口
 * 
 * @author 刘世平
 * 
 */
public class Main {
	public static final int VERSION_CODE = 38;
	private static final long FREE_SIZE = 1024 * 1024 * 100;// 100M
	private static Logger log = Logger.getLogger(Main.class.getName());
	public static String updatePath = "update";

	public static void main(String args[]) {
		// 注册异常处理器
		registerExceptionHandler();
		// 设置观感
		setLookAndFeel();
		// 设置字体
		initGlobalFontSetting();
		// 检查升级
		checkUpdate();
	}

	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void checkUpdate() {
		AsyncHttpConnection http = AsyncHttpConnection.getInstance();
		ParamsWrapper params = new ParamsWrapper();
		params.put("type", 1);
		params.put("code", VERSION_CODE);
		http.post(URLs.URL_CHECK_UPDATE, params, new StringResponseHandler() {
			@Override
			protected void onResponse(String content, URL url) {
				if (content != null && !content.equals("")) {
					JSONObject jsonObject = JSON.parseObject(content);
					if (jsonObject.getIntValue("code") == 1) {// 没有升级
						// 初始化应用程序
						checkMac();
						checkPadVersion(VERSION_CODE);// 检查pad文件是否最新版本
						return;
					}
					File file = new File("update.exe");
					if (!file.exists()) {
						JOptionPane.showMessageDialog(null, "检测到程序需要更新，但缺少必要的升级程序!");
						return;
					}
					long freeSize = file.getFreeSpace();
					if (freeSize < FREE_SIZE) {
						JOptionPane.showMessageDialog(null, "检测到程序需要更新，但磁盘空间不足，无法完成更新，请确保磁盘空余空间100M以上!");
						System.exit(0);
						return;
					}
					// 获得带升级的版本
					String data = jsonObject.getString("data");
					String apkStr = jsonObject.getString("apk");
					Version version = JSON.parseObject(data, Version.class);
					Version apk = JSON.parseObject(apkStr, Version.class);
					File lastfile = checkPCVersion(version,apk);// 检查教师端文件是否最新版本
					if (lastfile == null)
						return;
					install(lastfile);
				}
			}

			@Override
			public void onSubmit(URL url, ParamsWrapper params) {
			}

			@Override
			public void onConnectError(IOException exp) {
				JOptionPane.showMessageDialog(null, "不能连接到服务器，请检查网络！");
				System.exit(0);
			}

			@Override
			public void onStreamError(IOException exp) {
				JOptionPane.showMessageDialog(null, "数据解析错误！");
				System.exit(0);
			}
		});
	}

	private static void initGlobalFontSetting() {
		Font font = new Font("Microsoft YaHei", Font.PLAIN, 12);
		FontUIResource fontRes = new FontUIResource(font);
		Enumeration<?> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource)
				UIManager.put(key, fontRes);
		}
	}

	private static void registerExceptionHandler() {
		 Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler());
	}

	/**
	 * 密钥检查
	 */
	private static void checkMac() {
		File checkingKey = new File("./key/key.dat");
		if (!checkingKey.exists()) {
			schoolLogin();
			return;
		}
		try {
			FileInputStream fis = new FileInputStream("./key/key.dat");
			byte[] bt = new byte[32];// 只检查前32字节，超过部分忽略
			int length = fis.read(bt);
			fis.close();
			if (length == 0) {
				schoolLogin();
				return;
			}
			final String mac = new String(bt);
			checkMac(mac);// 检查数据库中密钥是否存在
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * pc端版本号检查
	 * @param version
	 * @param apk
	 * @return
	 */
	private static File checkPCVersion(Version version,Version apk) {
		File file = new File("update/互动课堂_" + version.getName() + "(" + version.getCode() + ").exe");
		if (file.exists())
			return file;
		load(version,apk);
		return null;
	}
	
	private static void load(Version version ,Version apk){
		final CountDownLatch downLatch = new CountDownLatch(1);
		loadFile(version, downLatch,"exe");
		try {
			downLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.info("线程被打断："+e);
		}
		loadFile(apk, null,"apk");
		VersionUpdater.getInstance(apk.getCode(),updatePath+"/互动课堂_"+apk.getCode()+".apk");
	}

	private static void loadFile(final Version version,final CountDownLatch downLatch,final String type) {
		new Thread() {
			@Override
			public void run() {
				File dir = new File("update");
				dir.delete();
				dir.mkdirs();
				File file = null;
				if("apk".equals(type)){
					file = new File(dir,"互动课堂_"+version.getCode()+"."+type);
				}else{
					file = new File(dir, "互动课堂_" + version.getName() + "(" + version.getCode() + ")."+type);					
				}
				String url = URLs.URL_DOWNLOAD_UPDATE + "id=" + version.getId();
				HttpClient client = new HttpClient();
				GetMethod httpGet = new GetMethod(url);
				try {
					client.executeMethod(httpGet);
					InputStream in = httpGet.getResponseBodyAsStream();
					FileOutputStream fos = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = in.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
					in.close();
					fos.close();
				} catch (Exception e) {
					log.info("更新失败，更新过程中断网", e);// 待更改
				} finally {
					httpGet.releaseConnection();
					if(downLatch!=null)
					downLatch.countDown();
				}
			}
		}.start();
	}
	
	private static void checkPadVersion(int versionCode){
		File file = new File("update/互动课堂_"+ versionCode + ".apk");
		if(!file.exists()){
			//TODO
			JOptionPane.showMessageDialog(null, "pad端更新程序丟失");
			return;
		}
	}

	private static void install(File file) {
		Runtime run = Runtime.getRuntime();
		try {
			run.exec("update.exe " + file.getAbsolutePath().replace(' ', '*'));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
	

	private static void schoolLogin() {
		Application.getInstance();
		new RoomLogin1();
	}

	private static void checkMac(String mac) {
		AsyncHttpConnection http = AsyncHttpConnection.getInstance();
		ParamsWrapper pram = new ParamsWrapper();
		pram.put("mac", mac);
		http.post(URLs.URL_CHECK_MAC, pram, new StringResponseHandler() {
			@Override
			public void onSubmit(URL url, ParamsWrapper params) {
			}

			@Override
			public void onStreamError(IOException exp) {
			}

			@Override
			public void onConnectError(IOException exp) {
				JOptionPane.showMessageDialog(null, "检查密钥失败，请检查网络！");
				System.exit(0);
			}

			@Override
			protected void onResponse(String content, URL url) {
				JSONObject result = JSONObject.parseObject(content);
				int flag = result.getIntValue("code");
				String mac = result.getString("mac");
				if (flag == 0) {
					schoolLogin();
					return;
				}
				Application.getInstance().setMac(mac);// 密钥验证正确，准许登录
				new Login();
			}
		});
	}

}
