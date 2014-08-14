/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.incito.interclass.Listener;

import cn.com.incito.interclass.ui.MainFrame;

/**
 * 
 * @author tewang
 */
public interface MySystemTrayEvent {
	// "打开/隐藏主页面(O)",
	public void openHide(MainFrame frame);

	public void config();

	// "关于我们(A)...",
	public void about();

	// "退出(X)"
	public void exit();
}
