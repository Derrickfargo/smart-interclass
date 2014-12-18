package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import cn.com.incito.server.core.CoreSocket;
import cn.com.incito.server.core.Message;
import cn.com.incito.server.message.DataType;
import cn.com.incito.server.message.MessagePacking;
import cn.com.incito.server.utils.BufferUtils;
import cn.com.incito.server.utils.UIHelper;

import com.sun.awt.AWTUtilities;

/**
 * 作业互评
 * 
 * @author 刘世平
 * 
 */
public class QuizFeedbackFrame extends JFrame {
	private static final long serialVersionUID = -2216276219179107707L;
	private PhotoDialog photoDialog;

	
	public QuizFeedbackFrame() {
		this.photoDialog = new PhotoDialog(this);
		setIconImage(new ImageIcon("images/main/icon.png").getImage());
		setUndecorated(true);// 去除窗体
		setAlwaysOnTop(true); // 设置界面悬浮
		getContentPane().setBackground(Color.BLACK);
		AWTUtilities.setWindowOpacity(this, 0.7f);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
		setSize(screen.width, screen.height - insets.bottom);
		photoDialog.setModal(true);
	}
	
	public void showFrame(){
		refresh();
		setVisible(true);
		photoDialog.setVisible(true);
	}
	
	public void refresh() {
		photoDialog.refresh();
	}
	
	public class PhotoDialog extends JDialog implements MouseListener{
		private static final long serialVersionUID = -2216276219179107707L;
		private static final String BACKGROUND = "images/quiz/bg_feedback.png";
		private static final String BTN_CLOSE_NORMAL = "images/quiz/ico_close.png";
		private static final String BTN_CLOSE_HOVER = "images/quiz/ico_close_hover.png";
		private static final String BTN_SHOW_NORMAL = "images/quiz/btn_show.png";
		private static final String BTN_SHOW_HOVER = "images/quiz/btn_show.png";
		
		private JButton btnClose, btnShow;
		private JLabel lblBackground, lblTitle;
		private QuizFeedbackFrame coverFrame;
		private QuizFeedbackContainer container;
		
		public PhotoDialog(final QuizFeedbackFrame coverFrame) {
			super(coverFrame, true);
			this.coverFrame = coverFrame;
			
			setSize(920, 740);
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
	        btnClose.setBounds(875, 0, 30, 30);
	        add(btnClose);
	        btnClose.addMouseListener(this);
	        
	        lblTitle = new JLabel("学生互评结果");
			lblTitle.setForeground(UIHelper.getDefaultFontColor());
			lblTitle.setBounds(20, 10, 75, 25);
			add(lblTitle);
			
			btnShow = new JButton();
			btnShow.setFocusable(false);
			btnShow.setBorderPainted(false);//设置边框不可见
			btnShow.setContentAreaFilled(false);//设置透明
	        btnShow.setIcon(new ImageIcon(BTN_SHOW_NORMAL));//设置图片
	        btnShow.setBounds(100, 6, 110, 34);
	        add(btnShow);
	        btnShow.addMouseListener(this);
			
			container = new QuizFeedbackContainer();
			container.setBackground(Color.WHITE);
	        JScrollPane quizScrollPane = new JScrollPane(container);
	        quizScrollPane.getVerticalScrollBar().setUnitIncrement(100);
	        quizScrollPane.setBorder(null);
	        quizScrollPane.setBounds(20, 40, 865, 680);
	        container.setPreferredSize(new Dimension(quizScrollPane.getWidth() - 50, (quizScrollPane.getHeight() - 50) * 4));
	        add(quizScrollPane);
	        
	        setBackground();
		}
		
		//设置背景
	    private void setBackground() {
	        lblBackground = new JLabel();
	        lblBackground.setIcon(new ImageIcon(BACKGROUND));
	        lblBackground.setBounds(0, 0, 900, 740);
	        add(lblBackground);
	    }

	    public void refresh(){
	    	container.refresh();
	    }
	    
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == btnClose) {
				int retval = JOptionPane.showConfirmDialog(this, "确实要退出互评吗？",
						"互动课堂", JOptionPane.YES_NO_OPTION);
				if (retval == JOptionPane.YES_OPTION) {
					setVisible(false);
					if(coverFrame != null){
						coverFrame.setVisible(false);
					}
					//结束互评
					MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_QUIZ_FEEDBACK_COMPLETE);
					messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString("false"));
					CoreSocket.getInstance().sendMessage(messagePacking.pack().array());
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
			if (e.getSource() == btnClose) {
	            btnClose.setIcon(new ImageIcon(BTN_CLOSE_HOVER));
	        }
			if (e.getSource() == btnShow) {
				btnShow.setIcon(new ImageIcon(BTN_SHOW_HOVER));
	        }
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (e.getSource() == btnClose) {
				btnClose.setIcon(new ImageIcon(BTN_CLOSE_NORMAL));
			}
			if (e.getSource() == btnShow) {
				btnShow.setIcon(new ImageIcon(BTN_SHOW_NORMAL));
	        }
		}
	    
	}
	
}
