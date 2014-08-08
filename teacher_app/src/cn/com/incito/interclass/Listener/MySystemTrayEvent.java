/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.incito.interclass.Listener;

import java.awt.event.MouseAdapter;

import javax.swing.*;

/**
 * 
 * @author tewang
 */
public interface MySystemTrayEvent {
	// "打开/隐藏主页面(O)",
	public void openHide(JFrame frame);

	public void config();

	// "关于我们(A)...",
	public void about();

	// "退出(X)"
	public void exit();
}
