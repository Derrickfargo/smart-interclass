package cn.com.incito.interclass.main;

import javax.swing.UIManager;

import cn.com.incito.server.api.Application;

/**
 * 应用程序入口
 * 
 * @author 刘世平
 * 
 */
public class Main {

	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 初始化应用程序
		Application.getInstance();
	}
}
