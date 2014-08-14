/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.incito.interclass.Listener;

import javax.swing.*;

import cn.com.incito.interclass.ui.MainFrame;

/**
 * 
 * @author tewang
 */
public class MySystemTrayManager implements MySystemTrayEvent {

	public synchronized void openHide(MainFrame frame) {
		if (!frame.isVisible()) {
			frame.setVisible(true);
		} else {
			if (JFrame.NORMAL == frame.getState()) {
				frame.setState(JFrame.ICONIFIED);
			} else {
				frame.setState(JFrame.NORMAL);
			}
		}

	}

	public void startAll() {
	}

	public void stopAll() {
	}

	public void createSingleTask() {
	}

	public void createMulTask() {
	}

	public void suspendWindow() {

	}

	public void listenClipboard() {

	}

	public void listenrBroswserClick() {

	}

	public void speedSuspend() {

	}

	public void resourceDetect() {

	}

	public void config() {

	}

	public void about() {
		JOptionPane.showMessageDialog(null, "create by lgh!");
	}

	public void exit() {
		System.exit(0);
	}
}
