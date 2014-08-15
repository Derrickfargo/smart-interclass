package cn.com.incito.interclass.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.server.utils.UIHelper;

public class TablePanel extends JPanel {

	private static final long serialVersionUID = 882552987989905663L;
	private static final String PAD_OFFLINE = "images/main/ico_pad_disconnect.png";
	private static final String DEFALUT_AVATAR = "images/main/ico_pad_connection.png";
	
	private Group group;
	private JLabel lblLogo;
	private JLabel lblNumber;
	private JLabel lblGroupName;
	private List<JLabel> deviceList = new ArrayList<JLabel>();
	private List<JLabel> studentList = new ArrayList<JLabel>();
	
	@Override
	protected void paintComponent(Graphics g) {
		Image iconUser = new ImageIcon("images/main/bg_list.png").getImage();
		g.drawImage(iconUser, 0, 0, this.getWidth(), this.getHeight(),this);
	}
	
	public TablePanel(){
		setLayout(null);
		setVisible(false);
		
		//课桌号
		Icon icon = new ImageIcon("images/main/pic_%s.png");
		lblNumber = new JLabel("课桌号", icon, JLabel.LEFT);
		lblNumber.setForeground(UIHelper.getDefaultFontColor());
		lblNumber.setHorizontalTextPosition(JLabel.LEFT);
		add(lblNumber);
		lblNumber.setBounds(15, 5, 70, 30);

		//小组名称
		lblGroupName = new JLabel();
		lblGroupName.setForeground(UIHelper.getDefaultFontColor());
		add(lblGroupName);
		lblGroupName.setBounds(80, 5, 80,30);
		lblGroupName.setVisible(false);
		
		//小组头像
		ImageIcon imgLogo = new ImageIcon(DEFALUT_AVATAR);//默认小组头像
		lblLogo = new JLabel();
		lblLogo.setIcon(imgLogo);
		add(lblLogo);
		lblLogo.setBounds(40, 60, imgLogo.getIconWidth(), imgLogo.getIconHeight());

		//小组与pad的分割线
		JLabel lblLine1 = getLine();
		add(lblLine1);
		lblLine1.setBounds(160, 10, 2,120);
		
		//pad列表
		int y = 30;
		for (int i = 0; i < 2; i++) {
			int x = 200;
			for (int j = 0; j < 2; j++) {
				JLabel lblDevice = createPad(x, y);
				add(lblDevice);
				lblDevice.setVisible(false);
				deviceList.add(lblDevice);
				x += 70;
			}
			y += 50;
		}
		
		//设备与学生的分割线
		JLabel lblLine2 = getLine();
		add(lblLine2);
		lblLine2.setBounds(367, 10, 2,120);
		
		//学生列表
		int x = 400;
		for (int i = 0; i < 4; i++) {
			y = 12;
			for (int j = 0; j < 4; j++) {
				JLabel lblName = getNameLabel();
				add(lblName);
				lblName.setBounds(x, y, 90, 24);
				studentList.add(lblName);
				y += 30;
			}
			x += 110;
		}
	}
	
	private JLabel createPad(int x, int y) {
		ImageIcon imgPad = new ImageIcon(PAD_OFFLINE);
		JLabel lblPad = new JLabel();
		lblPad.setIcon(imgPad);
		lblPad.setBounds(x, y, imgPad.getIconWidth(), imgPad.getIconHeight());
		return lblPad;
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

	private JLabel getNameLabel(){
		JLabel lblName = new JLabel("", JLabel.CENTER);
		lblName.setOpaque(true);
		lblName.setBackground(new Color(Integer.parseInt("e1e1e1", 16)));
		lblName.setForeground(new Color(Integer.parseInt("454545", 16)));
		return lblName;
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

	public List<JLabel> getDeviceList() {
		return deviceList;
	}

	public List<JLabel> getStudentList() {
		return studentList;
	}
	
	public void setTableNumber(int tableNumber){
		String url = String.format("images/main/pic_%s.png",tableNumber);
		Icon icon = new ImageIcon(url);
		lblNumber.setIcon(icon);
	}
}
