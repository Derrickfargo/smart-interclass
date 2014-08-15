package cn.com.incito.interclass.main;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import cn.com.incito.server.api.Application;

/**
 * 应用程序入口
 * 
 * @author 刘世平
 * 
 */
public class Main {

	public static void main(String args[]) {
		//设置观感
		setLookAndFeel();
		//设置字体
		initGlobalFontSetting();
		// 初始化应用程序
		Application.getInstance();
	}
	
	private static void setLookAndFeel(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
}
