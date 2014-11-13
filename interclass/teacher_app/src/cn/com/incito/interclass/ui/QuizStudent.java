package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.UIHelper;

/**
 * 学生作业Panel
 * @author 刘世平
 *
 */
public class QuizStudent extends JPanel implements MouseListener{
	private static final long serialVersionUID = 882552987989905663L;
	private static final String BTN_PLUS_NORMAL = "images/quiz/ico_add18x19.png";
	private static final String BTN_PLUS_HOVER = "images/quiz/ico_add_hover18x19.png";
	
	private static final String BTN_MINUS_NORMAL = "images/quiz/ico_js18x19.png";
	private static final String BTN_MINUS_HOVER = "images/quiz/ico_js_hover18x19.png";
	
	private static final String BTN_MEDAL_NORMAL = "images/quiz/ico_medal.png";
	private static final String BTN_MEDAL_HOVER = "images/quiz/ico_medalz_hover.png";
	
	private Quiz quiz;
	private Student student;
	private JPanel imagePanel;
	private JLabel lblOrder, lblImage, lblName;
	private JButton btnPlus, btnMinus, btnMedal;
	
	public QuizStudent(){
		setLayout(null);
		setVisible(false);
		
		lblOrder = getOrderLabel();
		lblOrder.setVisible(false);
		lblOrder.setBounds(2, 2, 15, 15);
		add(lblOrder);
		
		//小组加分按钮
		btnPlus = createPlusButton();
		btnPlus.addMouseListener(this);
		add(btnPlus);
		//小组减分按钮
		btnMinus = createMinusButton();
		btnMinus.addMouseListener(this);
		add(btnMinus);
		//小组勋章按钮
//		btnMedal = createMedalButton();
//		btnMedal.addMouseListener(this);
//		add(btnMedal);

		imagePanel = createImagePanel(0,0);
		imagePanel.setBounds(0, 0, imagePanel.getWidth(), imagePanel.getHeight());
		imagePanel.addMouseListener(this);
		add(imagePanel);
	}
	
	
	private JLabel getOrderLabel() {
		JLabel lblPad = new JLabel("", JLabel.CENTER);
		lblPad.setForeground(new Color(Integer.parseInt("FFFFFF", 16)));
		lblPad.setOpaque(true);
		return lblPad;
	}
	
	private JButton createPlusButton(){
		JButton lblPlus = new JButton();
		Icon iconPlus = new ImageIcon(BTN_PLUS_NORMAL);
		lblPlus = new JButton();
		lblPlus.setIcon(iconPlus);
		lblPlus.setFocusPainted(false);
//		lblPlus.setBorderPainted(false);// 设置边框不可见
//		lblPlus.setContentAreaFilled(false);// 设置透明
		lblPlus.setBounds(165,121, iconPlus.getIconWidth(), iconPlus.getIconHeight());
		return lblPlus;
	}
	
	private JButton createMinusButton(){
		JButton lblMinus = new JButton();
		Icon iconMinus = new ImageIcon(BTN_MINUS_NORMAL);
		lblMinus = new JButton();
		lblMinus.setIcon(iconMinus);
		lblMinus.setFocusPainted(false);
		lblMinus.setBorderPainted(false);// 设置边框不可见
		lblMinus.setContentAreaFilled(false);// 设置透明
		lblMinus.setBounds(141,121, iconMinus.getIconWidth(), iconMinus.getIconHeight());
		return lblMinus;
	}
	
//	private JButton createMedalButton(){
//		JButton lblMedal = new JButton();
//		Icon iconMedal = new ImageIcon(BTN_MEDAL_NORMAL);
//		lblMedal = new JButton();
//		lblMedal.setIcon(iconMedal);
//		lblMedal.setFocusPainted(false);
//		lblMedal.setBorderPainted(false);// 设置边框不可见
//		lblMedal.setContentAreaFilled(false);// 设置透明
//		lblMedal.setBounds(330,329, iconMedal.getIconWidth(), iconMedal.getIconHeight());
//		return lblMedal;
//	}
	
	private JPanel createImagePanel(int x, int y) {
		final ImageIcon icon = new ImageIcon("images/quiz/bg_img.png");
		JPanel imagePanel = new JPanel(){
			private static final long serialVersionUID = -5620925583420692590L;
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(icon.getImage(), 0, 0, icon.getIconWidth(), icon.getIconHeight(),this);
			}
		};
		imagePanel.setLayout(null);
		imagePanel.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
		lblImage = new JLabel();
		lblImage.setBounds(2, 2, 185, 109);
		imagePanel.add(lblImage);
		
		lblName = new JLabel("", JLabel.LEFT);
		lblName.setForeground(UIHelper.getDefaultFontColor());
		lblName.setBounds(5, icon.getIconHeight()- 25 , 100, 25);
		imagePanel.add(lblName);
		return imagePanel;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == btnPlus) {
//			new PraiseDialog(null, group);
			
		}
		if (e.getSource() == btnMinus) {
//			new PunishDialog(null, group);
		}
		if (e.getSource() == btnMedal) {
//			new MedalDialog(MainFrame.getInstance().getFrame(), group);
		}
		if (e.getSource() == imagePanel) {
			if (e.getClickCount() == 2) {
				if(quiz != null){
					String url = quiz.getQuizUrl();
					if (url != null && !url.equals("")) {
						new PhotoFrame(url);
					}
				}
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == btnPlus) {
			btnPlus.setIcon(new ImageIcon(BTN_PLUS_HOVER));
		}
		if (e.getSource() == btnMinus) {
			btnMinus.setIcon(new ImageIcon(BTN_MINUS_HOVER));
		}
		if (e.getSource() == btnMedal) {
			btnMedal.setIcon(new ImageIcon(BTN_MEDAL_HOVER));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == btnPlus) {
			btnPlus.setIcon(new ImageIcon(BTN_PLUS_NORMAL));
		}
		if (e.getSource() == btnMinus) {
			btnMinus.setIcon(new ImageIcon(BTN_MINUS_NORMAL));
		}
		if (e.getSource() == btnMedal) {
			btnMedal.setIcon(new ImageIcon(BTN_MEDAL_NORMAL));
		}
	}

	public void setStudent(Student student) {
		this.student = student;
		lblName.setText(student.getName());
		String imei = student.getImei();
		Application app = Application.getInstance();
		quiz = app.getTempQuiz().get(imei);
		if (quiz != null) {
			String url = quiz.getThumbnail();
			Icon icon = new ImageIcon(url);
			lblImage.setIcon(icon);
			int order = Application.getInstance().getQuizList().indexOf(quiz) + 1;
			lblOrder.setText(String.valueOf(order));
			switch (order) {
			case 1:
				lblOrder.setBackground(new Color(Integer.parseInt("BC3412", 16)));
				break;
			case 2:
				lblOrder.setBackground(new Color(Integer.parseInt("E07C00", 16)));
				break;
			case 3:
				lblOrder.setBackground(new Color(Integer.parseInt("F5DB00", 16)));
				break;
			default:
				lblOrder.setBackground(new Color(Integer.parseInt("ADADAD", 16)));
			}
			lblOrder.setVisible(true);
		}
	}
}
