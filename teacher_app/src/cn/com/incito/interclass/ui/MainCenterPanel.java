package cn.com.incito.interclass.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.util.Collections;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;

public class MainCenterPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6316121486627261595L;

	public MainCenterPanel() {
//		this.setSize(878, 620);
		this.setLayout(null);
		this.setOpaque(false);
		initView();
	}

	private void initView(){
		Application app = Application.getInstance();
		List<Group> groupList = app.getGroupList();
		if (groupList == null || groupList.size() == 0) {
			List<Table> tableList = app.getTableList();
		} else {
			Collections.sort(groupList);
			for (Group group : groupList) {
				
			}
		}
		
		
		//TODO 以下代码为测试代码
		int x = 20;
		for (int i = 0; i < 8; i++) {
			JPanel pnlTable1 = createTable(String.valueOf(i + 1), null);
			pnlTable1.setBounds(20, x, 836, 139);
			add(pnlTable1);
			x += 150;
		}
	}
	
	private JPanel createTable(String tableNumber,Group group) {
		JPanel pnlTable = new JPanel() {
			private static final long serialVersionUID = 1778895558158714379L;

			@Override
			protected void paintComponent(Graphics g) {
				Image iconUser = new ImageIcon("images/main/bg_list.png")
						.getImage();
				g.drawImage(iconUser, 0, 0, this.getWidth(), this.getHeight(),
						this);
			}
		};
		pnlTable.setLayout(null);
		String url = String.format("images/main/pic_%s.png", tableNumber);
		Icon icon = new ImageIcon(url);
		JLabel number = new JLabel("课桌号", icon, JLabel.LEFT);
		number.setHorizontalTextPosition(JLabel.LEFT);
		pnlTable.add(number);
		number.setBounds(15, 5, 70, 30);

		JLabel lblName = new JLabel("哇哈哈" + tableNumber);
		pnlTable.add(lblName);
		lblName.setBounds(80, 5, 80,30);
		
		ImageIcon imgLogo = new ImageIcon("images/main/ico_pad_connection.png");
		JLabel lblLogo = new JLabel();
		lblLogo.setIcon(imgLogo);
		pnlTable.add(lblLogo);
		lblLogo.setBounds(40, 60, imgLogo.getIconWidth(), imgLogo.getIconHeight());

		JLabel lblLine1 = getLine();
		pnlTable.add(lblLine1);
		lblLine1.setBounds(160, 10, 2,120);
		
		ImageIcon imgPadOnline = new ImageIcon("images/main/ico_pad_connection.png");
		ImageIcon imgPadOffline = new ImageIcon("images/main/ico_pad_disconnect.png");

		JLabel lblPad1 = new JLabel();
		lblPad1.setIcon(imgPadOffline);
		pnlTable.add(lblPad1);
		lblPad1.setBounds(200, 30, imgPadOffline.getIconWidth(), imgPadOffline.getIconHeight());

		JLabel lblPad2 = new JLabel();
		lblPad2.setIcon(imgPadOffline);
		pnlTable.add(lblPad2);
		lblPad2.setBounds(270, 30, imgPadOffline.getIconWidth(), imgPadOffline.getIconHeight());

		JLabel lblPad3 = new JLabel();
		lblPad3.setIcon(imgPadOffline);
		pnlTable.add(lblPad3);
		lblPad3.setBounds(200, 80, imgPadOffline.getIconWidth(), imgPadOffline.getIconHeight());

		JLabel lblPad4 = new JLabel();
		lblPad4.setIcon(imgPadOffline);
		pnlTable.add(lblPad4);
		lblPad4.setBounds(270, 80, imgPadOffline.getIconWidth(), imgPadOffline.getIconHeight());

		JLabel lblLine2 = getLine();
		pnlTable.add(lblLine2);
		lblLine2.setBounds(367, 10, 2,120);
		
		//list1
		JLabel lblName1 = getNameLabel("", false);
		pnlTable.add(lblName1);
		lblName1.setBounds(400, 12, 90, 24);
		JLabel lblName2 = getNameLabel("", false);
		pnlTable.add(lblName2);
		lblName2.setBounds(400, 42, 90, 24);
		JLabel lblName3 = getNameLabel("", false);
		pnlTable.add(lblName3);
		lblName3.setBounds(400, 72, 90, 24);
		JLabel lblName4 = getNameLabel("", false);
		pnlTable.add(lblName4);
		lblName4.setBounds(400, 102, 90, 24);
		//list2
		JLabel lblName21 = getNameLabel("", false);
		pnlTable.add(lblName21);
		lblName21.setBounds(510, 12, 90, 24);
		JLabel lblName22 = getNameLabel("", false);
		pnlTable.add(lblName22);
		lblName22.setBounds(510, 42, 90, 24);
		JLabel lblName23 = getNameLabel("", false);
		pnlTable.add(lblName23);
		lblName23.setBounds(510, 72, 90, 24);
		JLabel lblName24 = getNameLabel("", false);
		pnlTable.add(lblName24);
		lblName24.setBounds(510, 102, 90, 24);
		//list3
		JLabel lblName31 = getNameLabel("", false);
		pnlTable.add(lblName31);
		lblName31.setBounds(620, 12, 90, 24);
		JLabel lblName32 = getNameLabel("", false);
		pnlTable.add(lblName32);
		lblName32.setBounds(620, 42, 90, 24);
		JLabel lblName33 = getNameLabel("", false);
		pnlTable.add(lblName33);
		lblName33.setBounds(620, 72, 90, 24);
		JLabel lblName34 = getNameLabel("", false);
		pnlTable.add(lblName34);
		lblName34.setBounds(620, 102, 90, 24);
		
		return pnlTable;
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
				g2d.drawLine(0, 0, 0, this.getHeight());
				g2d.setStroke(stroke);
				g2d.setColor(color);
				this.paintComponents(g2d);
			}
		};
	}

	private JLabel getNameLabel(String name,final boolean isOnline){
		JLabel lblName = new JLabel(name, JLabel.CENTER);
		lblName.setOpaque(true);
		lblName.setBackground(new Color(Integer.parseInt("e1e1e1", 16)));
		lblName.setForeground(new Color(Integer.parseInt("000000", 16)));
		if(isOnline){
			lblName.setBackground(new Color(Integer.parseInt("5ec996", 16)));
			lblName.setForeground(new Color(Integer.parseInt("ffffff", 16)));
		}
		return lblName;
	}
	
	/**
	 * TODO 更新UI
	 */
	public void refresh() {
		removeAll();
		initView();
	}
}
