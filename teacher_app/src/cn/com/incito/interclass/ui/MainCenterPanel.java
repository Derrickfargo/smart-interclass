package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.LogoUtils;

public class MainCenterPanel extends JPanel implements UIContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6316121486627261595L;

	private static final String PAD_ONLINE = "images/main/ico_pad_connection.png";
	private static final String PAD_OFFLINE = "images/main/ico_pad_disconnect.png";

	private Application app = Application.getInstance();
	/**
	 * 当前教室所有Table，初始化界面时初始化本属性
	 */
	private List<TablePanel> tableList = new ArrayList<TablePanel>();
	/**
	 * 当前教室所有Group，初始化数据时初始化本属性
	 */
	private List<Group> groupList = new ArrayList<Group>();
	private JScrollPane scrollPane;

	public MainCenterPanel() {
		// this.setSize(878, 620);
		this.setLayout(null);
		this.setOpaque(true);
		revalidate();
		// 初始化界面
		initView();

		// 加载数据
		refresh();
	}

	private void initView() {
		int x = 20;
		for (int i = 1; i <= 12; i++) {
			TablePanel pnlTable = new TablePanel();
			pnlTable.setBounds(20, x, 836, 139);
			add(pnlTable);
			tableList.add(pnlTable);
			x += 150;
		}
	}

	public void refresh() {
		initData();
		// clearView();
		// 遍历内存模型，绑定到物理模型
		for (int i = 0; i < groupList.size(); i++) {// 遍历分组内存模型
			Group group = groupList.get(i);
			TablePanel tablePanel = tableList.get(i);
			tablePanel.setGroup(group);
			tablePanel.setVisible(true);
			tablePanel.setTableNumber(group.getTableNumber());
			if (group.getName() != null) {// 显示小组名称
				tablePanel.getLblGroupName().setVisible(true);
				tablePanel.getLblGroupName().setText(group.getName());
			}
			if (group.getLogo() != null) {
				String logo = LogoUtils.getInstance().getLogo(group.getLogo());
				ImageIcon imgLogo = new ImageIcon(logo);
				tablePanel.getLblLogo().setIcon(imgLogo);
				tablePanel.getLblLogo().setBounds(40, 40, 80, 80);
			}
			// 遍历当前组/桌的设备，内存模型
			List<JLabel> deviceLabelList = tablePanel.getDeviceList();
			List<Device> deviceList = group.getDevices();
			// 遍历设备内存模型，重绘所有pad状态
			for (int j = 0; j < deviceList.size(); j++) {
				Device device = deviceList.get(j);
				String imei = device.getImei();
				ImageIcon imgPad = null;
				if (app.getOnlineDevice().contains(imei)) {
					imgPad = new ImageIcon(PAD_ONLINE);
				} else {
					imgPad = new ImageIcon(PAD_OFFLINE);
				}
				JLabel lblDevice = deviceLabelList.get(j);
				lblDevice.setIcon(imgPad);
				lblDevice.setVisible(true);
			}
			// 遍历当前组/桌的学生，内存模型
			List<Student> studentList = group.getStudents();
			if (studentList == null || studentList.size() == 0) {
				return;
			}
			List<JLabel> studentLabelList = tablePanel.getStudentList();
			for (int k = 0; k < studentList.size(); k++) {
				Student student = studentList.get(k);
				JLabel lblStudent = studentLabelList.get(k);
				if (app.getOnlineStudent().contains(student)) {
					lblStudent.setText(student.getName());
					lblStudent.setBackground(new Color(Integer.parseInt(
							"5ec996", 16)));
					lblStudent.setVisible(true);
				} else {
					lblStudent.setText(student.getName());
					lblStudent.setBackground(new Color(Integer.parseInt(
							"e1e1e1", 16)));
					lblStudent.setVisible(true);
				}
			}
		}
	}

	private void initData() {
		groupList = new ArrayList<Group>();
		// 课桌绑定分组，生成内存模型
		List<Table> tables = app.getTableList();
		for (Table table : tables) {
			// 获得课桌对应的分组
			Group group = app.getTableGroup().get(table.getId());
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
	 * 暂时不用，会导致界面闪烁
	 */
	private void clearView() {
		for (TablePanel tablePanel : tableList) {
			List<JLabel> deviceLabelList = tablePanel.getDeviceList();
			for (JLabel lblDevice : deviceLabelList) {
				ImageIcon imgPad = new ImageIcon(PAD_OFFLINE);
				lblDevice.setIcon(imgPad);
				lblDevice.setVisible(false);
			}
			List<JLabel> studentLabelList = tablePanel.getStudentList();
			for (JLabel lblStudent : studentLabelList) {
				lblStudent.setText("");
				lblStudent.setBackground(new Color(Integer.parseInt("e1e1e1",
						16)));
				lblStudent.setVisible(false);
			}
		}
	}
}
