package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.UIHelper;

import com.sun.awt.AWTUtilities;

/**
 * 图片预览窗口
 * 
 * @author 刘世平
 * 
 */
public class PhotoFrame extends JFrame {
	private static final long serialVersionUID = -2216276219179107707L;
	private PhotoDialog photoDialog;

	public PhotoFrame(String image) {
		this.photoDialog = new PhotoDialog(this, image);
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
	
	public static void main(String[] args) {
		new PhotoFrame("");
	}
	
	public class PhotoDialog extends JDialog implements MouseListener{
		private static final long serialVersionUID = -2216276219179107707L;
		private static final String BACKGROUND = "images/quiz/bg_main.png";
		private static final String BTN_CLOSE_NORMAL = "images/quiz/ico_close.png";
		private static final String BTN_CLOSE_HOVER = "images/quiz/ico_close_hover.png";
		private static final String BTN_PREV_NORMAL = "images/quiz/ico_prev.png";
		private static final String BTN_PREV_HOVER = "images/quiz/ico_prev_hover.png";
		private static final String BTN_NEXT_NORMAL = "images/quiz/ico_next.png";
		private static final String BTN_NEXT_HOVER = "images/quiz/ico_next_hover.png";
		private static final String BACKGROUND_NORMAL = "images/quiz/bg_thumb.png";
		private static final String BACKGROUND_SELECTED = "images/quiz/bg_thumb_hover.png";
		private PhotoFrame coverFrame;
		private JButton btnClose, btnPrevious, btnNext;
		private JLabel lblBackground, lblTitle, lblImage;
		private int position;
		private List<Quiz> quizList = new ArrayList<Quiz>();
		private List<ImagePanel> imageList = new ArrayList<ImagePanel>();
		
		public class ImagePanel extends JPanel {
			private static final long serialVersionUID = -8125124617095670805L;
			private JLabel lblBackground;
			private JLabel lblImage;
			private boolean isSelected;

			public ImagePanel() {
				setLayout(null);
				
				lblBackground = new JLabel();
				ImageIcon imgBack = new ImageIcon(BACKGROUND_NORMAL);
				lblBackground.setIcon(imgBack);
				lblBackground.setBounds(0, 0, 132, 87);
				add(lblBackground);

				lblImage = new JLabel();
				lblImage.setBounds(1, 1, 130, 85);
				add(lblImage);
			}

			public void setPicture(String url) {
				ImageIcon image = new ImageIcon(url);
				lblImage.setIcon(image);
			}
			
			public void setSelected(boolean isSelected){
				this.isSelected = isSelected;
				if(isSelected){
					ImageIcon imgBack = new ImageIcon(BACKGROUND_SELECTED);
					lblBackground.setIcon(imgBack);
				}else{
					ImageIcon imgBack = new ImageIcon(BACKGROUND_NORMAL);
					lblBackground.setIcon(imgBack);
				}
			}
			
			public void setTempSelected(boolean isTempSelected) {
				if (isTempSelected) {
					ImageIcon imgBack = new ImageIcon(BACKGROUND_SELECTED);
					lblBackground.setIcon(imgBack);
				} else {
					if (!isSelected) {
						ImageIcon imgBack = new ImageIcon(BACKGROUND_NORMAL);
						lblBackground.setIcon(imgBack);
					}
				}
			}
		}
		
		
		public PhotoDialog(PhotoFrame coverFrame, String url) {
			super(coverFrame, true);
			this.coverFrame = coverFrame;
			initData(url);
			
			setSize(972, 698);
			setUndecorated(true);//去除窗体
			setLocationRelativeTo(null);// 设置窗体中间位置
			setLayout(null);// 绝对布局
			setAlwaysOnTop(true); // 设置界面悬浮
			setBackground(new Color(0,0,0,0));//窗体透明
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			
			btnClose = new JButton();
			btnClose.setBorderPainted(false);//设置边框不可见
	        btnClose.setContentAreaFilled(false);//设置透明
	        ImageIcon imgMax = new ImageIcon(BTN_CLOSE_NORMAL);
	        btnClose.setIcon(imgMax);//设置图片
	        btnClose.setBounds(920, 0, 30, 30);
	        add(btnClose);
	        btnClose.addMouseListener(this);
	        
	        lblTitle = new JLabel();
			lblTitle.setText("哇哈哈小组-[哈哈哈，呵呵呵]");
			lblTitle.setForeground(UIHelper.getDefaultFontColor());
			lblTitle.setBounds(54, 20, 200, 25);
			add(lblTitle);
			
	        JPanel pnlImage = new JPanel(){
	        	private static final long serialVersionUID = 1778895558158714379L;

				@Override
				protected void paintComponent(Graphics g) {
					Image image = new ImageIcon("images/quiz/bg_mainpic.png").getImage();
					g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
				}
	        };
	        pnlImage.setLayout(null);
	        pnlImage.setBounds(54, 45, 867, 542);
	        add(pnlImage);
	        lblImage = new JLabel();
	        ImageIcon image = new ImageIcon(url);
	        lblImage.setIcon(image);
	        lblImage.setBounds(1, 1, 865, 540);
	        pnlImage.add(lblImage);
	        
	        int x = 56;
			for (int i = 0; i < 6; i++) {
				ImagePanel thumb1 = new ImagePanel();
				thumb1.setBounds(x, 595, 132, 87);
				add(thumb1);
				imageList.add(thumb1);
				thumb1.addMouseListener(this);
				x += 146;
			}
	        
			btnPrevious = new JButton();
			btnPrevious.setBorderPainted(false);//设置边框不可见
			btnPrevious.setContentAreaFilled(false);//设置透明
	        ImageIcon prev = new ImageIcon(BTN_PREV_NORMAL);
	        btnPrevious.setIcon(prev);//设置图片
	        btnPrevious.setBounds(3, 611, prev.getIconWidth(), prev.getIconHeight());
	        add(btnPrevious);
	        btnPrevious.addMouseListener(this);
	        
	        btnNext = new JButton();
	        btnNext.setBorderPainted(false);//设置边框不可见
	        btnNext.setContentAreaFilled(false);//设置透明
	        ImageIcon next = new ImageIcon(BTN_NEXT_NORMAL);
	        btnNext.setIcon(next);//设置图片
	        btnNext.setBounds(950, 611, next.getIconWidth(), next.getIconHeight());
	        add(btnNext);
	        btnNext.addMouseListener(this);
	        
			setBackground();
		}
		
		//设置背景
	    private void setBackground() {
	        lblBackground = new JLabel();
	        lblBackground.setIcon(new ImageIcon(BACKGROUND));
	        lblBackground.setBounds(34, 15, 907, 698);
	        add(lblBackground);
	    }
	    
		private void initData(String url) {
			//所有作业
			quizList = Application.getInstance().getQuizList();
			for (int i = 0; i < quizList.size(); i++) {
				Quiz quiz = quizList.get(i);
				if(quiz.getQuizUrl().equals(url)){
					position = i;
					break;
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == btnClose) {
				dispose();
				if(coverFrame != null){
					coverFrame.dispose();
				}
			}
			if (imageList.contains(e.getSource())) {
				for (ImagePanel panel : imageList) {
					panel.setSelected(false);
					if(panel == e.getSource()){
						panel.setSelected(true);
					}
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
	        if (imageList.contains(e.getSource())) {
				for (ImagePanel panel : imageList) {
					panel.setTempSelected(false);
					if(panel == e.getSource()){
						panel.setTempSelected(true);
					}
				}
			}
			if(e.getSource() == btnPrevious){
				btnPrevious.setIcon(new ImageIcon(BTN_PREV_HOVER));
			}
			if(e.getSource() == btnNext){
				btnNext.setIcon(new ImageIcon(BTN_NEXT_HOVER));
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (e.getSource() == btnClose) {
				btnClose.setIcon(new ImageIcon(BTN_CLOSE_NORMAL));
			}
			for (ImagePanel panel : imageList) {
				if(panel == e.getSource()){
					panel.setTempSelected(false);
					break;
				}
			}
			if(e.getSource() == btnPrevious){
				btnPrevious.setIcon(new ImageIcon(BTN_PREV_NORMAL));
			}
			if(e.getSource() == btnNext){
				btnNext.setIcon(new ImageIcon(BTN_NEXT_NORMAL));
			}
		}
	}
}
