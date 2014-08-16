package cn.com.incito.interclass.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.UIHelper;

public class QuizBottomPanel extends JPanel{
	private static final long serialVersionUID = -9135075807085951600L;
	private JButton btnSend;
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
		
		btnSend = new JButton();// 创建按钮对象
		btnSend.setFocusPainted(false);
		btnSend.setBorderPainted(false);// 设置边框不可见
		btnSend.setContentAreaFilled(false);// 设置透明
		ImageIcon btnImage = new ImageIcon("images/quiz/btn_send_works.png");
		btnSend.setIcon(btnImage);// 设置图片
		add(btnSend);// 添加按钮
		btnSend.setBounds(470, -4, btnImage.getIconWidth(),
				btnImage.getIconHeight());
		btnSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}
	

}
