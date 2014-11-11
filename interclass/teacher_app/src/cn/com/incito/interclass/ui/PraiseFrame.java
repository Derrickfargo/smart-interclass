package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import cn.com.incito.server.api.Application;

import com.sun.awt.AWTUtilities;

/**
 * 图片预览窗口
 * 
 * @author 刘世平
 * 
 */
public class PraiseFrame extends JFrame {
	private static final long serialVersionUID = -2216276219179107707L;
	private PhotoDialog photoDialog;

	public PraiseFrame(String groupName, int score) {
		this.photoDialog = new PhotoDialog(this, groupName, score);
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
		
		photoDialog.setModal(true);
		photoDialog.setVisible(true);
	}
	
	public class PhotoDialog extends JDialog {
		private static final long serialVersionUID = -2216276219179107707L;
		private static final String BACKGROUND = "images/praise/pic_praise2.png";
		private JLabel lblBackground, lblGroupName;
		
		public PhotoDialog(final PraiseFrame coverFrame, String groupName, int score) {
			super(coverFrame, true);
			
			setSize(900, 760);
			setUndecorated(true);//去除窗体
			setLocationRelativeTo(null);// 设置窗体中间位置
			setLayout(null);// 绝对布局
			setAlwaysOnTop(true); // 设置界面悬浮
			setBackground(new Color(0,0,0,0));//窗体透明
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			
			lblGroupName = new JLabel(groupName, JLabel.RIGHT);
			Font font = Application.getInstance().getDefinedFont();
			if (font != null) {
				font = font.deriveFont(48f);
				font = font.deriveFont(Font.BOLD);
			} else {
				font = new Font("Microsoft YaHei", Font.BOLD, 48);
			}
			lblGroupName.setFont(font);
			lblGroupName.setForeground(new Color(Integer.parseInt("FFFFFF", 16)));
			lblGroupName.setBounds(230, 380, 220, 60);
			add(lblGroupName);
		        
	        //设置背景
			setBackground();
			rootPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE , 0), "close");
			rootPane.getActionMap().put("close", new AbstractAction() {
				private static final long serialVersionUID = -7001796667148557833L;
				public void actionPerformed(ActionEvent e) {
					dispose();
					if(coverFrame != null){
						coverFrame.dispose();
					}
				}
			 });
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					dispose();
					if(coverFrame != null){
						coverFrame.dispose();
					}
				}
			});
			new Thread(){
				public void run() {
					try {
						Thread.sleep(15000);
						dispose();
						if(coverFrame != null){
							coverFrame.dispose();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
		
		//设置背景
	    private void setBackground() {
	        lblBackground = new JLabel();
	        lblBackground.setIcon(new ImageIcon(BACKGROUND));
	        lblBackground.setBounds(0, 0, 900, 760);
	        add(lblBackground);
	    }
	    
	}
	
}
