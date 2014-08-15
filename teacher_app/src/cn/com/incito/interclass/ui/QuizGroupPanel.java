package cn.com.incito.interclass.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.server.utils.UIHelper;

public class QuizGroupPanel extends JPanel {

	private static final long serialVersionUID = 882552987989905663L;
	
	private Group group;
	private JLabel lblLogo;
	private JLabel lblNumber;
	private JLabel lblGroupName;
	private List<JPanel> quizList = new ArrayList<JPanel>();
	private List<JLabel> nameList = new ArrayList<JLabel>();
	@Override
	protected void paintComponent(Graphics g) {
		Image image = new ImageIcon("images/quiz/bg_list.png").getImage();
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(),this);
	}
	
	public QuizGroupPanel(){
		setLayout(null);
//		setVisible(false);
		
		JPanel quiz1 = createImagePanel(14,15);
		quizList.add(quiz1);
		add(quiz1);
//		quiz1.setVisible(false);
		
		JPanel quiz2 = createImagePanel(208,15);
		quizList.add(quiz2);
		add(quiz2);
//		quiz2.setVisible(false);
		
		JPanel quiz3 = createImagePanel(14,165);
		quizList.add(quiz3);
		add(quiz3);
//		quiz3.setVisible(false);
		
		JPanel quiz4 = createImagePanel(208,165);
		quizList.add(quiz4);
		add(quiz4);
//		quiz4.setVisible(false);
		
		//小组信息
		
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
		JLabel lblName = new JLabel("测试测试从测试", JLabel.CENTER);
		lblName.setForeground(UIHelper.getDefaultFontColor());
		nameList.add(lblName);
		lblName.setBounds(0, icon.getIconHeight()- 25 , icon.getIconWidth(), 25);
		imagePanel.add(lblName);
		return imagePanel;
	}
	
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public JLabel getLblLogo() {
		return lblLogo;
	}

	public JLabel getLblGroupName() {
		return lblGroupName;
	}

	public void setTableNumber(int tableNumber){
		String url = String.format("images/main/pic_%s.png",tableNumber);
		Icon icon = new ImageIcon(url);
		lblNumber.setIcon(icon);
	}
}
