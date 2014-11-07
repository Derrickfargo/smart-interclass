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
import cn.com.incito.server.utils.UIHelper;

public class GroupPanel extends JPanel {

	private static final long serialVersionUID = 882552987989905663L;
	private static final int COLUMN_COUNT = 8;//每行最多8个学生
	private Group group;
	private JLabel lblLogo;
	private JLabel lblGroupName;
	private List<JLabel> studentList = new ArrayList<JLabel>();
	

	public GroupPanel() {
		setLayout(null);
		setSize(823, 95);
		setBackground(Color.white);
		initView();
	}

	private void initView() {
		// 小组图标
		lblLogo = new JLabel("", JLabel.CENTER);
		lblLogo.setIcon(new ImageIcon("images/main/load_groupinfo.gif"));
		add(lblLogo);
		lblLogo.setBounds(10, 2, 32, 32);
		lblLogo.setVisible(false);

		// 小组名称
		lblGroupName = new JLabel();
		lblGroupName.setForeground(UIHelper.getDefaultFontColor());
		add(lblGroupName);
		lblGroupName.setBounds(50, 5, 80, 30);
		lblGroupName.setVisible(false);

		// 小组与pad的分割线
		JLabel lblLine = getLine();
		add(lblLine);
		lblLine.setBounds(5, 35, 812, 2);

		// pad列表
		int x = 20;
		for (int i = 0; i < COLUMN_COUNT; i++) {
			JLabel lblName = getNameLabel();
			lblName.setBounds(x, 45, 81, 24);
			add(lblName);
			lblName.setVisible(false);
			studentList.add(lblName);
			x += 100;
		}
		// 背景
		JLabel lblBackground = new JLabel();
		ImageIcon bg = new ImageIcon("images/main/bg_index_list.png");
		lblBackground.setIcon(bg);
		lblBackground.setBounds(0, 0, 825, 90);
		bg.setImage(bg.getImage().getScaledInstance(823, 90, Image.SCALE_DEFAULT));
		add(lblBackground);
	}

	private JLabel getLine() {
		return new JLabel() {
			private static final long serialVersionUID = 2679733728559406364L;

			@Override
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				Stroke stroke = g2d.getStroke();
				Color color = g2d.getColor();
				g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
				g2d.setColor(new Color(Integer.parseInt("e1e1e1", 16)));
				g2d.drawLine(0, 0, this.getWidth(), 0);
				g2d.setStroke(stroke);
				g2d.setColor(color);
				this.paintComponents(g2d);
			}
		};
	}

	private JLabel getNameLabel() {
		JLabel lblName = new JLabel("", JLabel.CENTER);
		lblName.setOpaque(true);
		lblName.setBackground(new Color(Integer.parseInt("5ec996", 16)));
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

	public List<JLabel> getStudentList() {
		return studentList;
	}

}
