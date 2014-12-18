package cn.com.incito.interclass.main;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import cn.com.incito.http.AsyncHttpConnection;
import cn.com.incito.http.StringResponseHandler;
import cn.com.incito.http.support.ParamsWrapper;
import cn.com.incito.interclass.po.Version;
import cn.com.incito.interclass.ui.Login;
import cn.com.incito.interclass.ui.RoomLogin1;
import cn.com.incito.interclass.ui.widget.UpdateDialog;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.URLs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 应用程序入口
 * 
 * @author 刘世平
 * 
 */
public class Main {
	public static final int VERSION_CODE = 23;
	private static final long FREE_SIZE = 1024 * 1024 * 100;//100M
	
	public static void main(String args[]) {
		// 注册异常处理器
		registerExceptionHandler();
		//设置观感
		setLookAndFeel();
		//设置字体
		initGlobalFontSetting();
		//检查升级
		checkUpdate();
	}
	
	private static void setLookAndFeel(){
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
					if (jsonObject.getIntValue("code") == 1) {//没有升级
						// 初始化应用程序
						checkMac();
						return;
					}
					File file = new File("update.exe");
					if(!file.exists()){
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
					Version version = JSON.parseObject(data, Version.class);
					new UpdateDialog(version).setVisible(true);
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
	
	private static void registerExceptionHandler(){
//		Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler());
	}
	
	/**
	 * 密钥检查
	 */
	private static void checkMac(){
		File checkingKey = new File("./key/key.dat");
		if(!checkingKey.exists()){
			schoolLogin();
			return;
		}
		try {
			FileInputStream fis = new FileInputStream("./key/key.dat");
			byte[] bt = new byte[32];//只检查前32字节，超过部分忽略
			int length = fis.read(bt);
			fis.close();
			if(length==0){
				schoolLogin();
				return;
			}
			 final String mac = new String(bt);
			 checkMac(mac);//检查数据库中密钥是否存在
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void schoolLogin(){
		Application.getInstance();
		new RoomLogin1();
	}
	private static void checkMac(String mac){
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
				 JSONObject result=JSONObject.parseObject(content);
				 int flag=result.getIntValue("code");
				 String mac = result.getString("mac");
				 if(flag==0){
						schoolLogin();
						return;
				 }
				Application.getInstance().setMac(mac);//密钥验证正确，准许登录
					new Login();
			}
		 });
	}
	
}
