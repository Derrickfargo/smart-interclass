package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import cn.com.incito.server.api.Application;

import com.sun.awt.AWTUtilities;

public class ResponderFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5493385648819577301L;
	private ResponderDialog resDialog;
	
	public ResponderFrame(String name){
		this.resDialog = new ResponderDialog(name, this);
		setIconImage(new ImageIcon("images/main/icon.png").getImage());
		setUndecorated(true);// 去除窗体
		setAlwaysOnTop(true); // 设置界面悬浮
		getContentPane().setBackground(Color.BLACK);
		AWTUtilities.setWindowOpacity(this, 0.7f);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
		setSize(screen.width, screen.height - insets.bottom);

		setVisible(true);
		
		resDialog.setModal(true);
		resDialog.setVisible(true);
	}
	
	
	private class ResponderDialog extends JDialog{

		/**
		 * 
		 */
		private static final long serialVersionUID = -7840107243119733444L;
		private JButton btnClose;
		private JLabel lblBackground, lblName,lblsex;
		private String pathName = "images/responder/responder_result.png";
		private String pathSex  = "images/responder/boy.png";
		
		public  ResponderDialog(String name,final JFrame resFrame){
			super(resFrame, true);
			Icon img = setBackground(pathName);
			setSize(img.getIconWidth(),img.getIconHeight());
			setUndecorated(true);//去除窗体
			setLocationRelativeTo(null);// 设置窗体中间位置
			setLayout(null);// 绝对布局
			setAlwaysOnTop(true); // 设置界面悬浮
			setBackground(new Color(0,0,0,0));//窗体透明
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			
			lblName = new JLabel(name, JLabel.LEFT);
			Font font = new Font("Microsoft YaHei", Font.BOLD, 16);
			lblName.setFont(font);
			lblName.setForeground(Color.black);
			lblName.setBounds(250, 380, 350, 200);
			add(lblName);
			
			Icon iconSex = new ImageIcon(pathSex);
			lblsex = new JLabel(iconSex);
			lblsex.setBounds(85, 418, iconSex.getIconWidth(), iconSex.getIconHeight());
			lblsex.setForeground(Color.red);
			add(lblsex);
			
			lblBackground = new JLabel(img);
			lblBackground.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
			add(lblBackground);
			
			Icon image = new ImageIcon("images/quiz/ico_close.png");
			btnClose = new JButton();
			btnClose.setFocusPainted(false);
			btnClose.setBorderPainted(false);// 设置边框不可见
			btnClose.setContentAreaFilled(false);// 设置透明
			btnClose.setIcon(image);
			btnClose.setBounds(635, 100, image.getIconWidth(),image.getIconHeight());
			btnClose.addActionListener(new ActionListener(){
	            public void actionPerformed(ActionEvent e) {
	            	dispose();
					if(resFrame != null){
						resFrame.dispose();
					}
	            }
	        });
			add(btnClose);
			
		
			
			rootPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE , 0), "close");
			rootPane.getActionMap().put("close", new AbstractAction() {
				private static final long serialVersionUID = -7001796667148557833L;
				public void actionPerformed(ActionEvent e) {
					dispose();
					if(resFrame != null){
						resFrame.dispose();
					}
				}
			 });
			new Thread(){
				public void run() {
					try {
						Thread.sleep(15000);
						dispose();
						if(resFrame != null){
							resFrame.dispose();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
		
		//设置背景
	    private Icon setBackground(String background) {
	        Icon icon = new ImageIcon(background);
	        return icon;
	    }
		
	}
}
