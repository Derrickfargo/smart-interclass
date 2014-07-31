package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	
	private Application app = Application.getInstance();
	/**
	 * 当前教室所有Table，初始化界面时初始化本属性
	 */
	private List<TablePanel> tableList = new ArrayList<TablePanel>();
	/**
	 * 当前教室所有Group，初始化数据时初始化本属性
	 */
	private List<Group> groupList = new ArrayList<Group>();
	
	
	public MainCenterPanel() {//this.setSize(878, 620);
		this.setLayout(null);
		this.setOpaque(false);
		initView();
	}

	private void initView(){
		//初始化界面
		int x = 20;
		for (int i = 1; i <= 12; i++) {
			TablePanel pnlTable = new TablePanel(i);
			pnlTable.setBounds(20, x, 836, 139);
			add(pnlTable);
			tableList.add(pnlTable);
			x += 150;
		}
		//加载数据
		refresh();
	}
	
	public void refresh(){
		initData();
		//遍历内存模型，修改物理模型
		for (int i = 0; i < groupList.size(); i++) {// 遍历分组内存模型
			Group group = groupList.get(i);
			TablePanel tablePanel = tableList.get(i);
			tablePanel.setVisible(true);
			//遍历当前组/桌的设备，内存模型 
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
			//遍历当前组/桌的学生，内存模型
			List<Student> studentList = group.getStudents();
			if(studentList == null || studentList.size() == 0){
				return;
			}
			List<JLabel> studentLabelList = tablePanel.getStudentList();
			for (int k = 0; k < studentList.size(); k++) {
				Student student = studentList.get(k);
				JLabel lblStudent = studentLabelList.get(k);
				if (app.getOnlineStudent().contains(student)) {
					lblStudent.setText(student.getName());
					lblStudent.setBackground(new Color(Integer.parseInt("5ec996", 16)));
					lblStudent.setVisible(true);
				} else {
					lblStudent.setText(student.getName());
					lblStudent.setBackground(new Color(Integer.parseInt("e1e1e1", 16)));
					lblStudent.setVisible(true);
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
	
}
