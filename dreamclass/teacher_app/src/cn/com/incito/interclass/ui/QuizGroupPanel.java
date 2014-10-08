package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.ui.widget.MedalDialog;
import cn.com.incito.interclass.ui.widget.PraiseDialog;
import cn.com.incito.interclass.ui.widget.PunishDialog;
import cn.com.incito.server.utils.UIHelper;

/**
 * 小组作业Panel
 * @author 刘世平
 *
 */
public class QuizGroupPanel extends JPanel implements MouseListener{
	 private Logger logger = Logger.getLogger(QuizGroupPanel.class.getName());
	private static final long serialVersionUID = 882552987989905663L;
	private static final String BTN_PLUS_NORMAL = "images/quiz/ico_add.png";
	private static final String BTN_PLUS_HOVER = "images/quiz/ico_add_hover.png";
	
	private static final String BTN_MINUS_NORMAL = "images/quiz/ico_js.png";
	private static final String BTN_MINUS_HOVER = "images/quiz/ico_js_hover.png";
	
	private static final String BTN_MEDAL_NORMAL = "images/quiz/ico_medal.png";
	private static final String BTN_MEDAL_HOVER = "images/quiz/ico_medalz_hover.png";
	
	private Group group;
	private JLabel lblDesk;
//	private JLabel lblLogo;
	private JLabel lblGroupName;
//	private JLabel lblScore;
	private JButton btnPlus,btnMinus,btnMedal;
	private List<JLabel> quizList = new ArrayList<JLabel>();
	private List<JPanel> quizPanel = new ArrayList<JPanel>();
	private List<JLabel> nameList = new ArrayList<JLabel>();
	private List<JLabel> orderList = new ArrayList<JLabel>();
	private Map<String,Quiz> quizMap = new HashMap<String, Quiz>();
	
	@Override
	protected void paintComponent(Graphics g) {
		Image image = new ImageIcon("images/quiz/bg_list.png").getImage();
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(),this);
	}
	
	public QuizGroupPanel(){
		setLayout(null);
		setVisible(false);
		
		JLabel lblOrder1 = getOrderLabel();
		lblOrder1.setVisible(false);
		lblOrder1.setBounds(16, 17, 15, 15);
		add(lblOrder1);
		orderList.add(lblOrder1);
		JPanel quiz1 = createImagePanel(14,15);
		quiz1.setVisible(false);
		add(quiz1);
		quizPanel.add(quiz1);
		
		JLabel lblOrder2 = getOrderLabel();
		lblOrder2.setVisible(false);
		lblOrder2.setBounds(210, 17, 15, 15);
		add(lblOrder2);
		orderList.add(lblOrder2);
		JPanel quiz2 = createImagePanel(208,15);
		quiz2.setVisible(false);
		add(quiz2);
		quizPanel.add(quiz2);
		
		JLabel lblOrder3 = getOrderLabel();
		lblOrder3.setVisible(false);
		lblOrder3.setBounds(16, 167, 15, 15);
		add(lblOrder3);
		orderList.add(lblOrder3);
		JPanel quiz3 = createImagePanel(14,165);
		quiz3.setVisible(false);
		add(quiz3);
		quizPanel.add(quiz3);
		
		JLabel lblOrder4 = getOrderLabel();
		lblOrder4.setVisible(false);
		lblOrder4.setBounds(210, 167, 15, 15);
		add(lblOrder4);
		orderList.add(lblOrder4);
		JPanel quiz4 = createImagePanel(208,165);
		quiz4.setVisible(false);
		add(quiz4);
		quizPanel.add(quiz4);
		
		//小组课桌号
		lblDesk = createDeskLabel();
		add(lblDesk);
		//TODO 去掉小组logo
//		lblLogo = createLogoLabel();
//		add(lblLogo);
		//小组名字
		lblGroupName = createGroupName();
		add(lblGroupName);
		//TODO 去掉小组积分
//		lblScore = createScoreLabel();
//		add(lblScore);
		//小组加分按钮
		btnPlus = createPlusButton();
		btnPlus.addMouseListener(this);
		add(btnPlus);
		//小组减分按钮
		btnMinus = createMinusButton();
		btnMinus.addMouseListener(this);
		add(btnMinus);
		//小组勋章按钮
		btnMedal = createMedalButton();
		btnMedal.addMouseListener(this);
		add(btnMedal);

	}
	
	private JLabel createDeskLabel(){
		JLabel lblDesk = new JLabel("", JLabel.CENTER);
		lblDesk.setOpaque(true);
		lblDesk.setBounds(14,331, 60, 24);
		lblDesk.setBackground(new Color(Integer.parseInt("39a2de", 16)));
		lblDesk.setForeground(new Color(Integer.parseInt("ffffff", 16)));
		return lblDesk;
	}
	
	private JLabel createLogoLabel(){
		JLabel lblLogo = new JLabel();
		final ImageIcon icon = new ImageIcon("images/logo/24/rainbow.png");
		lblLogo.setIcon(icon);
		lblLogo.setBounds(76, 331, 24, 24);
		return lblLogo;
	}
	
	private JLabel createGroupName(){
		JLabel lblName = new JLabel("");
//		lblName.setBounds(105,331, 100, 24);
		lblName.setBounds(76,331, 100, 24);
		lblName.setForeground(UIHelper.getDefaultFontColor());
		return lblName;
	}
	
	private JLabel getOrderLabel() {
		JLabel lblPad = new JLabel("", JLabel.CENTER);
		lblPad.setForeground(new Color(Integer.parseInt("FFFFFF", 16)));
		lblPad.setOpaque(true);
		return lblPad;
	}
	
	private JLabel createScoreLabel(){
		JLabel lblDesk = new JLabel("0分", JLabel.CENTER);
		lblDesk.setOpaque(true);
		lblDesk.setBounds(210,331, 40, 24);
		lblDesk.setBackground(Color.RED);
		lblDesk.setForeground(Color.WHITE);
		return lblDesk;
	}
	
	private JButton createPlusButton(){
		JButton lblPlus = new JButton();
		Icon iconPlus = new ImageIcon(BTN_PLUS_NORMAL);
		lblPlus = new JButton();
		lblPlus.setIcon(iconPlus);
		lblPlus.setFocusPainted(false);
		lblPlus.setBorderPainted(false);// 设置边框不可见
		lblPlus.setContentAreaFilled(false);// 设置透明
		lblPlus.setBounds(260,329, iconPlus.getIconWidth(), iconPlus.getIconHeight());
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
		lblMinus.setBounds(295,329, iconMinus.getIconWidth(), iconMinus.getIconHeight());
		return lblMinus;
	}
	
	private JButton createMedalButton(){
		JButton lblMedal = new JButton();
		Icon iconMedal = new ImageIcon(BTN_MEDAL_NORMAL);
		lblMedal = new JButton();
		lblMedal.setIcon(iconMedal);
		lblMedal.setFocusPainted(false);
		lblMedal.setBorderPainted(false);// 设置边框不可见
		lblMedal.setContentAreaFilled(false);// 设置透明
		lblMedal.setBounds(330,329, iconMedal.getIconWidth(), iconMedal.getIconHeight());
		return lblMedal;
	}
	
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
		JLabel lblImage = new JLabel();
		lblImage.setBounds(2, 2, 185, 109);
		quizList.add(lblImage);
		imagePanel.add(lblImage);
		imagePanel.addMouseListener(this);
		
		JLabel lblName = new JLabel("", JLabel.CENTER);
		lblName.setForeground(UIHelper.getDefaultFontColor());
		nameList.add(lblName);
		lblName.setBounds(0, icon.getIconHeight()- 25 , icon.getIconWidth(), 25);
		imagePanel.add(lblName);
		return imagePanel;
	}
	
	public void addImage(int i, Quiz quiz) {
		if (quiz != null) {
			String url = quiz.getThumbnail();
			Icon icon1 = new ImageIcon(url);
			quizList.get(i).setIcon(icon1);
			quizList.get(i).getParent().setName(quiz.getQuizUrl());
			quizMap.put(quiz.getImei(), quiz);
		} else {
			Icon icon = new ImageIcon("");
			quizList.get(i).setIcon(icon);
			quizList.get(i).getParent().setName("");
			quizMap.clear();
		}
	}
	
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public JLabel getLblDesk() {
		return lblDesk;
	}

//	public JLabel getLblScore() {
//		return lblScore;
//	}

//	public JLabel getLblLogo() {
//		return lblLogo;
//	}

	public JLabel getLblGroupName() {
		return lblGroupName;
	}

	public List<JLabel> getOrderList() {
		return orderList;
	}

	public List<JLabel> getQuizList() {
		return quizList;
	}

	public List<JLabel> getNameList() {
		return nameList;
	}

	public List<JPanel> getQuizPanel() {
		return quizPanel;
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
			new PraiseDialog(null, group);
			
		}
		if (e.getSource() == btnMinus) {
			new PunishDialog(null, group);
		}
		if (e.getSource() == btnMedal) {
			new MedalDialog(MainFrame.getInstance().getFrame(), group);
		}
		if (quizPanel.contains(e.getSource())) {
			if (e.getClickCount() == 2) {
				JPanel lblPanel = (JPanel) e.getSource();
				String url = lblPanel.getName();
				if (url != null && !url.equals("")) {
					new PhotoFrame(url);
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
}
