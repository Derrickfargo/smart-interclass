package cn.com.incito.interclass.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;

public class MainCenterPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6316121486627261595L;

	private static final String PAD_ONLINE = "images/main/ico_pad_connection.png";
	private static final String PAD_OFFLINE = "images/main/ico_pad_disconnect.png";
	private static final String DEFALUT_AVATAR = "images/main/ico_pad_connection.png";
	
	private Application app = Application.getInstance();
	private List<Group> groupList = new ArrayList<Group>();
	private Map<Integer,JLabel> logoMap = new HashMap<Integer, JLabel>();//组与logo
	private Map<String,JLabel> deviceMap = new HashMap<String, JLabel>();//设备IMEI和界面JLabel元素对应
	//key1:小组id，key2:姓名+学号
	private Map<Integer,Map<String,JLabel>> groupStudent = new HashMap<Integer, Map<String,JLabel>>();
	
	public MainCenterPanel() {//this.setSize(878, 620);
		this.setLayout(null);
		this.setOpaque(false);
		initView();
	}

	private void initView(){
		initData();
		int x = 20;
		for (Group group : groupList) {
			JPanel pnlTable1 = createTable(group);
			pnlTable1.setBounds(20, x, 836, 139);
			add(pnlTable1);
			x += 150;
		}
	}
	
	private JPanel createTable(Group group) {
		//小组容器
		JPanel pnlTable = new JPanel() {
			private static final long serialVersionUID = 1778895558158714379L;

			@Override
			protected void paintComponent(Graphics g) {
				Image iconUser = new ImageIcon("images/main/bg_list.png").getImage();
				g.drawImage(iconUser, 0, 0, this.getWidth(), this.getHeight(),this);
			}
		};
		pnlTable.setLayout(null);
		
		//课桌号
		String url = String.format("images/main/pic_%s.png", group.getTableNumber());
		Icon icon = new ImageIcon(url);
		JLabel number = new JLabel("课桌号", icon, JLabel.LEFT);
		number.setHorizontalTextPosition(JLabel.LEFT);
		pnlTable.add(number);
		number.setBounds(15, 5, 70, 30);

		//小组名称
		JLabel lblGroupName = new JLabel(group.getName());
		pnlTable.add(lblGroupName);
		lblGroupName.setBounds(80, 5, 80,30);
		
		//小组头像
		ImageIcon imgLogo = new ImageIcon(DEFALUT_AVATAR);//默认小组头像
		JLabel lblLogo = new JLabel();
		lblLogo.setIcon(imgLogo);
		pnlTable.add(lblLogo);
		lblLogo.setBounds(40, 60, imgLogo.getIconWidth(), imgLogo.getIconHeight());

		//小组与pad的分割线
		JLabel lblLine1 = getLine();
		pnlTable.add(lblLine1);
		lblLine1.setBounds(160, 10, 2,120);
		
		//pad列表
		List<List<Device>> devices = initDevices(group.getDevices());
		int y = 30;
		for (int i = 0; i < 2; i++) {
			List<Device> list = devices.get(i);
			int x = 200;
			for (int j = 0; j < 2; j++) {
				Device device = list.get(j);
				JLabel lblDevice = createPad(x, y, device.getImei());
				pnlTable.add(lblDevice);
				deviceMap.put(device.getImei(), lblDevice);
				x += 70;
			}
			y += 50;
		}
		
		//设备与学生的分割线
		JLabel lblLine2 = getLine();
		pnlTable.add(lblLine2);
		lblLine2.setBounds(367, 10, 2,120);
		
		//学生列表
		Map<String, JLabel> studentMap = new HashMap<String, JLabel>();
		List<List<Student>> students = initStudents(group.getStudents());
		int x = 400;
		for (int i = 0; i < 4; i++) {
			List<Student> list = students.get(i);
			y = 12;
			for (int j = 0; j < 4; j++) {
				Student student = list.get(j);
				JLabel lblName = getNameLabel(student.getName(), false);
				if(student.getId() != 0){
					String key = student.getName() + student.getNumber();
					studentMap.put(key, lblName);
				}
				pnlTable.add(lblName);
				lblName.setBounds(x, y, 90, 24);
				y += 30;
			}
			x += 110;
		}
		groupStudent.put(group.getTableNumber(), studentMap);
		return pnlTable;
	}

	private JLabel createPad(int x, int y, String imei) {
		ImageIcon imgPad = null;
		if (app.getOnlineDevice().contains(imei)) {
			imgPad = new ImageIcon(PAD_ONLINE);
		} else {
			imgPad = new ImageIcon(PAD_OFFLINE);
		}
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
	
	public void refresh(){
		initData();
		//遍历内存模型，修改物理模型
		for (Group group : groupList) {//遍历分组内存模型
			//遍历当前组/桌的设备，内存模型
			for (Device device : group.getDevices()) {//遍历设备内存模型，重绘所有pad状态
				String imei = device.getImei();
				ImageIcon imgPad = null;
				if (app.getOnlineDevice().contains(imei)) {
					imgPad = new ImageIcon(PAD_ONLINE);
					System.out.println("pad登陆:" + imei);
				} else {
					imgPad = new ImageIcon(PAD_OFFLINE);
				}
				JLabel lblDevice = deviceMap.get(imei);
				lblDevice.setIcon(imgPad);
			}
			Map<String,JLabel> studentMap = groupStudent.get(group.getTableNumber());
			//遍历当前组/桌的学生，内存模型
			List<Student> studentList = group.getStudents();
			for (Student student : studentList) {
				if(app.getOnlineStudent().contains(student)){
					String key = student.getName() + student.getNumber();
					JLabel lblName = studentMap.get(key);
					lblName.setBackground(new Color(Integer.parseInt("5ec996", 16)));
					lblName.setVisible(true);
				}else{
					String key = student.getName() + student.getNumber();
					JLabel lblName = studentMap.get(key);
					lblName.setBackground(new Color(Integer.parseInt("e1e1e1", 16)));
					lblName.setVisible(true);
				}
			}
		}
	}

	private void initData() {
		groupList = new ArrayList<Group>();
		//课桌绑定分组，生成内存模型
		List<Table> tableList = app.getTableList();
		for (Table table : tableList) {
			//获得课桌对应的分组
			Group group = app.getTableGroup().get(table.getNumber());
			if (group == null) {
				group = new Group();
			}
			group.setTableId(table.getId());
			group.setTableNumber(table.getNumber());
			group.setDevices(table.getDevices());
			groupList.add(group);
		}
		Collections.sort(groupList);
	}
	
	/**
	 * 初始化Pad为2行2列
	 * @param deviceList
	 * @return
	 */
	private List<List<Device>> initDevices(List<Device> deviceList) {
		List<List<Device>> result = new ArrayList<List<Device>>();
		int index = 0;
		for (int i = 0; i < 2; i++) {
			List<Device> list = new ArrayList<Device>();
			for (int j = 0; j < 2; j++) {
				if (index < deviceList.size()) {
					list.add(deviceList.get(index++));
				} else {
					list.add(new Device());
				}
			}
			result.add(list);
		}
		return result;
	}
	
	/**
	 * 初始化学生为4行4列
	 * @param studentList
	 * @return
	 */
	private List<List<Student>> initStudents(List<Student> studentList) {
		List<List<Student>> result = new ArrayList<List<Student>>();
		int index = 0;
		for (int i = 0; i < 4; i++) {
			List<Student> list = new ArrayList<Student>();
			for (int j = 0; j < 4; j++) {
				if (index < studentList.size()) {
					list.add(studentList.get(index++));
				} else {
					list.add(new Student());
				}
			}
			result.add(list);
		}
		return result;
	}
}
