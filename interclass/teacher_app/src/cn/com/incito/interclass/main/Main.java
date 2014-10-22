package cn.com.incito.interclass.main;

import java.awt.Font;
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
import cn.com.incito.interclass.ui.widget.UpdateDialog;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.exception.AppExceptionHandler;
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
	public static final int VERSION_CODE = 1;

	public static void main(String args[]) {
		// 注册异常处理器
		registerExceptionHandler();
		// 设置观感
		setLookAndFeel();
		// 设置字体
		initGlobalFontSetting();
		// 检查是否有升级
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
					if (jsonObject.getIntValue("code") == 1) {//没有升级
						// 初始化应用程序
						Application.getInstance();
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
				JOptionPane.showMessageDialog(null, "不能连接到服务端，请检查网络！");
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
}
