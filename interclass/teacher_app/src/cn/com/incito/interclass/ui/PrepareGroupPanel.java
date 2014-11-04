package cn.com.incito.interclass.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.UIHelper;

public class PrepareGroupPanel extends JPanel {

	private static final long serialVersionUID = 882552987989905663L;
	private Group group;
	private JLabel lblLogo;
	private JLabel lblNumber;
	private JLabel lblGroupName;
	private List<PadPanel> deviceList = new ArrayList<PadPanel>();
	private Application app = Application.getInstance();

	public PrepareGroupPanel() {
		setLayout(null);
		setVisible(false);
		List<Group> groups = app.getGroupList();
		
		for (int i = 0; i < 8; i++) {
			Group g = new Group();
			g.setName("测试" + i);
		}
		
//		for(Group group : groups){
			createGroup(null);
//		}
	}

	private void createGroup(Group group) {
		// 组号
		lblNumber = createDeskNumber();
		add(lblNumber);
		lblNumber.setBounds(20, 55, 25, 25);

		//分组loading图标
		lblLogo = new JLabel("", JLabel.CENTER);
		lblLogo.setIcon(new ImageIcon("images/main/load_groupinfo.gif"));
		add(lblLogo);
		lblLogo.setBounds(50, 95, 32, 32);
		lblLogo.setVisible(false);

		// 小组名称
		lblGroupName = new JLabel();
		lblGroupName.setForeground(UIHelper.getDefaultFontColor());
		add(lblGroupName);
		lblGroupName.setBounds(50, 95, 80, 30);
		lblGroupName.setVisible(false);

		// 小组与pad的分割线
		JLabel lblLine = getLine();
		add(lblLine);
		lblLine.setBounds(15, 85, 812, 2);

		// pad列表
		int y = 95;
		for (int i = 0; i < 4; i++) {
			JLabel lblName = getNameLabel();
			y += 150;
		}
		//背景
		JLabel lblBackground = new JLabel();
		ImageIcon bg = new ImageIcon("images/main/bg_index_list.png");
		lblBackground.setIcon(bg);
		lblBackground.setBounds(8, 45, 825, 90);
		bg.setImage(bg.getImage().getScaledInstance(823,90,Image.SCALE_DEFAULT));
		add(lblBackground);
	}

	private JLabel createDeskNumber() {
		JLabel lblDesk = new JLabel("", JLabel.CENTER);
		lblDesk.setOpaque(true);
		lblDesk.setBackground(new Color(Integer.parseInt("a4b981", 16)));
		lblDesk.setForeground(new Color(Integer.parseInt("ffffff", 16)));
		return lblDesk;
	}

	private JLabel getLine() {
		return new JLabel() {
			private static final long serialVersionUID = 2679733728559406364L;

			@Override
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				Stroke stroke = g2d.getStroke();
				Color color = g2d.getColor();
				g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_MITER));
				g2d.setColor(new Color(Integer.parseInt("e1e1e1", 16)));
				g2d.drawLine(0, 0, this.getWidth(), 0);
				g2d.setStroke(stroke);
				g2d.setColor(color);
				this.paintComponents(g2d);
			}
		};
	}

	private JLabel getNameLabel(){
		JLabel lblName = new JLabel("", JLabel.CENTER);
		lblName.setOpaque(true);
		lblName.setBackground(new Color(Integer.parseInt("E1E1E1", 16)));
		lblName.setForeground(new Color(Integer.parseInt("FFFFFF", 16)));
		return lblName;
	}
	
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public void showGrouping(boolean grouping) {
		if (grouping) {
			lblGroupName.setVisible(false);
			lblLogo.setVisible(true);
		} else {
			lblGroupName.setVisible(true);
			lblLogo.setVisible(false);
		}
	}

	public JLabel getLblGroupName() {
		return lblGroupName;
	}

	public List<PadPanel> getDeviceList() {
		return deviceList;
	}

	public void setTableNumber(int tableNumber) {
		lblNumber.setText(String.valueOf(tableNumber));
	}
}
