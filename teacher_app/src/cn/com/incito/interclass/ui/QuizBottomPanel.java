package cn.com.incito.interclass.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.UIHelper;

public class QuizBottomPanel extends JPanel implements MouseListener{
	private static final long serialVersionUID = -9135075807085951600L;
	
	private static final String BTN_SEND_NORMAL = "images/quiz/btn_send_works.png";
	private static final String BTN_SEND_HOVER = "images/quiz/btn_send_works_hover.png";
	
	private static final String BTN_ACCEPT_NORMAL = "images/quiz/btn_accept_works.png";
	private static final String BTN_ACCEPT_HOVER = "images/quiz/btn_accept_works_hover.png";
	
	private boolean hasQuiz = false;//是否在作业
	
	private JButton btnQuiz;
	private Application app = Application.getInstance();
	
	public QuizBottomPanel(){
		setSize(878, 48);
		setLayout(null);
		setOpaque(false);
		
		JLabel lblExpected = new JLabel("应到 %d 人  | 实到 %d 人", JLabel.CENTER);
		lblExpected.setForeground(UIHelper.getDefaultFontColor());
		lblExpected.setBounds(10, 15, 150, 35);
		add(lblExpected);
		
		JPanel pnlClass = new JPanel() {
			private static final long serialVersionUID = 5365972834168199801L;

			@Override
			protected void paintComponent(Graphics g) {
				Image iconClass = new ImageIcon("images/main/bg_input_kc.png").getImage();
				g.drawImage(iconClass, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		pnlClass.setLayout(null);
		add(pnlClass);
		pnlClass.setBounds(180, 10, 120, 35);

		JPanel pnlCourse = new JPanel() {
			private static final long serialVersionUID = 5365972834168199801L;

			@Override
			protected void paintComponent(Graphics g) {
				Image iconClass = new ImageIcon("images/main/bg_input_kc.png").getImage();
				g.drawImage(iconClass, 0, 0, this.getWidth(), this.getHeight(), this);
			}
		};
		pnlCourse.setLayout(null);
		add(pnlCourse);
		pnlCourse.setBounds(320, 10, 120, 35);
		
		btnQuiz = new JButton();// 创建按钮对象
		btnQuiz.setFocusPainted(false);
		btnQuiz.setBorderPainted(false);// 设置边框不可见
		btnQuiz.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon(BTN_SEND_NORMAL);
		btnQuiz.setIcon(btnImage);// 设置图片
		add(btnQuiz);// 添加按钮
		btnQuiz.setBounds(470, -4, btnImage.getIconWidth(), btnImage.getIconHeight());
		btnQuiz.addMouseListener(this);
	}

	private void doSendQuiz(){
		int result = JOptionPane.showConfirmDialog(getParent().getParent(),
				"是否截图发送作业？", "提示", JOptionPane.YES_NO_OPTION);
		if (JOptionPane.YES_OPTION == result) {
			//TODO 截图发送
			
			return;
		}
		//TODO 发送白板
		
	}
	
	private void doAcceptQuiz(){
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == btnQuiz) {
			if (hasQuiz) {// 有作业，收作业
				btnQuiz.setIcon(new ImageIcon(BTN_ACCEPT_HOVER));
			} else {// 没作业，发作业
				doAcceptQuiz();
				btnQuiz.setIcon(new ImageIcon(BTN_SEND_HOVER));
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (hasQuiz) {// 有作业，收作业
			doSendQuiz();
			hasQuiz = false;
			btnQuiz.setIcon(new ImageIcon(BTN_SEND_HOVER));
		} else {// 没作业，发作业
			doAcceptQuiz();
			hasQuiz = true;
			btnQuiz.setIcon(new ImageIcon(BTN_ACCEPT_HOVER));
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == btnQuiz) {
			if (hasQuiz) {// 有作业，收作业
				btnQuiz.setIcon(new ImageIcon(BTN_ACCEPT_HOVER));
			} else {// 没作业，发作业
				doAcceptQuiz();
				btnQuiz.setIcon(new ImageIcon(BTN_SEND_HOVER));
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == btnQuiz) {
			if (hasQuiz) {// 有作业，收作业
				btnQuiz.setIcon(new ImageIcon(BTN_ACCEPT_NORMAL));
			} else {// 没作业，发作业
				doAcceptQuiz();
				btnQuiz.setIcon(new ImageIcon(BTN_SEND_NORMAL));
			}
		}
	}
	

}
