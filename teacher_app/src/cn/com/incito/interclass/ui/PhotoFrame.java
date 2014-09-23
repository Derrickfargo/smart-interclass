package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

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
	
	public class PhotoDialog extends JDialog implements MouseListener{
		private static final long serialVersionUID = -2216276219179107707L;
		private static final int PAGE_COUNT = 6;
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
			private Quiz quiz;

			public ImagePanel() {
				setLayout(null);
				
				lblImage = new JLabel();
				lblImage.setBounds(15, 8, 100, 70);
				lblImage.setHorizontalAlignment(JLabel.CENTER);
				add(lblImage);
				
				lblBackground = new JLabel();
				ImageIcon imgBack = new ImageIcon(BACKGROUND_NORMAL);
				lblBackground.setIcon(imgBack);
				lblBackground.setBounds(0, 0, 132, 87);
				add(lblBackground);

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

			public Quiz getQuiz() {
				return quiz;
			}

			public void setQuiz(Quiz quiz) {
				this.quiz = quiz;
				if (quiz == null) {
					ImageIcon image = new ImageIcon();
					lblImage.setIcon(image);
				} else {
					ImageIcon image = new ImageIcon(quiz.getThumbnail());
					lblImage.setIcon(image);
				}
			}
			
		}
		
		
		public PhotoDialog(final PhotoFrame coverFrame, String url) {
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
	        lblImage.setBounds(1, 1, 865, 540);
	        lblImage.setHorizontalAlignment(JLabel.CENTER);
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
			btnPrevious.setFocusPainted(false);
			btnPrevious.setBorderPainted(false);//设置边框不可见
			btnPrevious.setContentAreaFilled(false);//设置透明
	        ImageIcon prev = new ImageIcon(BTN_PREV_NORMAL);
	        btnPrevious.setIcon(prev);//设置图片
	        btnPrevious.setBounds(-12, 611, prev.getIconWidth(), prev.getIconHeight());
	        add(btnPrevious);
	        btnPrevious.addMouseListener(this);
	        
	        btnNext = new JButton();
	        btnNext.setFocusPainted(false);
	        btnNext.setBorderPainted(false);//设置边框不可见
	        btnNext.setContentAreaFilled(false);//设置透明
	        ImageIcon next = new ImageIcon(BTN_NEXT_NORMAL);
	        btnNext.setIcon(next);//设置图片
	        btnNext.setBounds(933, 611, next.getIconWidth(), next.getIconHeight());
	        add(btnNext);
	        btnNext.addMouseListener(this);
	        //设置背景
			setBackground();
			//查找当前url对应的位置
			initData(url);
			//根据当前位置加载数据
			refresh();
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
	    
	    public void refresh(){
			List<Quiz> page = getPage(-1);
			if(page.size() == 0){
				return ;
			}
			Quiz quiz = page.get(0);
			ImageIcon image = new ImageIcon(quiz.getQuizUrl());
	        lblImage.setIcon(image);
			for (int i = 0; i < 6; i++) {//6个缩略图
				ImagePanel panel = imageList.get(i);
				if (i < page.size()) {
					panel.setQuiz(page.get(i));
				} else {
					panel.setQuiz(null);
				}
			}
			for (int j = 0; j < imageList.size(); j++) {
				ImagePanel panel = imageList.get(j);
				if (j == 0) {
					panel.setSelected(true);
					quiz = panel.getQuiz();
					if (quiz != null && quiz.getGroup() != null) {
						if (quiz.getGroup().getName() == null) {
							lblTitle.setText(String.format("未分组[%s]", quiz.getName()));
						} else {
							String title = quiz.getGroup().getName() + "[%s]";
							lblTitle.setText(String.format(title, quiz.getName()));
						}
					}
				} else {
					panel.setSelected(false);
				}
			}
	    }
	    
		/**
		 * 
		 * @param index 当前页,暂时不用
		 * @return 某页数据,最多6个
		 */
		private List<Quiz> getPage(int index) {
			List<Quiz> quizzes = new ArrayList<Quiz>();
			if (position >= quizList.size()) {
				return quizzes;
			}
			int count = 1;
			while (position < quizList.size()) {
				quizzes.add(quizList.get(position));
				position++;
				if (count == PAGE_COUNT) {
					break;
				}
				count++;
			}
			return quizzes;
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
						Quiz quiz = panel.getQuiz();
						if (quiz == null) {
							continue;
						}
						ImageIcon icon = new ImageIcon(quiz.getQuizUrl());
						if (quiz != null && quiz.getGroup() != null) {
							if (quiz.getGroup().getName() == null) {
								lblTitle.setText(String.format("未分组[%s]", quiz.getName()));
							} else {
								String title = quiz.getGroup().getName() + "[%s]";
								lblTitle.setText(String.format(title, quiz.getName()));
							}
						}
						lblImage.setIcon(icon);
					}
				}
			}
			if (e.getSource() == btnPrevious) {
				position -= 6;
				if (position < 0) {
					position = 0;
				}
				refresh();
			}
			if (e.getSource() == btnNext) {
//				position += 6;
//				if (position > quizList.size()) {
//					position = quizList.size();
//				}
				refresh();
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
				btnPrevious.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			if(e.getSource() == btnNext){
				btnNext.setIcon(new ImageIcon(BTN_NEXT_HOVER));
				btnNext.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
				btnPrevious.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			if(e.getSource() == btnNext){
				btnNext.setIcon(new ImageIcon(BTN_NEXT_NORMAL));
				btnNext.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
		
	}
	
}
