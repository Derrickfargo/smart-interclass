package cn.com.incito.interclass.ui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Group;

public class GroupPanel extends JPanel {

	private static final long serialVersionUID = 882552987989905663L;
	
	private Group group;
	private JLabel lblLogo;
	private JLabel lblNumber;
	private JLabel lblGroupName;
	
	@Override
	protected void paintComponent(Graphics g) {
		Image image = new ImageIcon("images/quiz/bg_list.png").getImage();
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(),this);
	}
	
	public GroupPanel(){
		setLayout(null);
		add(createImagePanel(12,15));
		add(createImagePanel(210,15));
		add(createImagePanel(12,230));
		add(createImagePanel(210,230));
		
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
		imagePanel.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
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
