package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import cn.com.incito.server.utils.UIHelper;

import com.sun.awt.AWTUtilities;
/**
 * 白版
 * @author caicai
 *
 */
public class CoverPanel extends JFrame{
	private static final long serialVersionUID = 3518050566015738014L;
	private CoverDialog coverDialog;
	
	public CoverPanel() {
		this.coverDialog = new CoverDialog(this);
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
		
		coverDialog.setModal(true);
		coverDialog.setVisible(true);
	}
	public class CoverDialog extends JDialog implements MouseListener{
		private static final long serialVersionUID = -2216276219179107707L;
		private static final String BACKGROUND = "images/quiz/bg_main.png";
		private static final String BTN_CLOSE_NORMAL = "images/quiz/ico_close.png";
		private static final String BTN_CLOSE_HOVER = "images/quiz/ico_close_hover.png";
		private CoverPanel coverFrame;
		private JButton btnClose;
		private JLabel lblBackground, lblTitle;
		
		public	CoverDialog(final CoverPanel coverFrame) {
			super(coverFrame, true);
			this.coverFrame = coverFrame;
			
			setSize(972, 698);
			setUndecorated(true);//去除窗体
			setLocationRelativeTo(null);// 设置窗体中间位置
			setLayout(null);// 绝对布局
			setAlwaysOnTop(true); // 设置界面悬浮
			setBackground(new Color(0,0,0,0));//窗体透明
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			
			btnClose = new JButton();
			btnClose.setFocusable(false);
			btnClose.setBorderPainted(false);//设置边框不可见
	        btnClose.setContentAreaFilled(false);//设置透明
	        ImageIcon imgMax = new ImageIcon(BTN_CLOSE_NORMAL);
	        btnClose.setIcon(imgMax);//设置图片
	        btnClose.setBounds(920, 0, 30, 30);
	        add(btnClose);
	        btnClose.addMouseListener(this);
	        
	        lblTitle = new JLabel();
			lblTitle.setForeground(UIHelper.getDefaultFontColor());
			lblTitle.setBounds(54, 20, 200, 25);
			add(lblTitle);
	        //设置背景
			setBackground();
			//按ESC退出
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
		}
		//设置背景
	    private void setBackground() {
	        lblBackground = new JLabel();
	        lblBackground.setIcon(new ImageIcon(BACKGROUND));
	        lblBackground.setBounds(34, 15, 907, 698);
	        add(lblBackground);
	    }
		
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == btnClose) {
				dispose();
				if(coverFrame != null){
					coverFrame.dispose();
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//鼠标进入效果
	        if (e.getSource() == btnClose) {
	            btnClose.setIcon(new ImageIcon(BTN_CLOSE_HOVER));
	        }
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (e.getSource() == btnClose) {
				btnClose.setIcon(new ImageIcon(BTN_CLOSE_NORMAL));
			}
		}
		
	}
}
