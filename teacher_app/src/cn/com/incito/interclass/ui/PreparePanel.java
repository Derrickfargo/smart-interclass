package cn.com.incito.interclass.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;

public class PreparePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6316121486627261595L;
	private static final String ICON_NO_DESK = "images/main/bg_binding_desk.png";
	private JLabel lblNoDesk;
	private Application app = Application.getInstance();
	/**
	 * 当前教室所有Table，初始化界面时初始化本属性
	 */
	private List<PrepareGroupPanel> tableList = new ArrayList<PrepareGroupPanel>();
	/**
	 * 当前教室所有Group，初始化数据时初始化本属性
	 */
	private List<Group> groupList = new ArrayList<Group>();

	public PreparePanel() {
		// this.setSize(878, 620);
		this.setLayout(null);
		this.setOpaque(true);
		// 初始化界面
		initView();

		// 加载数据
		refresh();
	}

	private void initView() {
		int i = 0;
		int y = 10;
		while (i < 12) {
			PrepareGroupPanel pnlLeft = new PrepareGroupPanel();
			pnlLeft.setBounds(10, y, 410, 210);
			add(pnlLeft);
			tableList.add(pnlLeft);
			if (++i < 12) {
				PrepareGroupPanel pnlRight = new PrepareGroupPanel();
				pnlRight.setBounds(438, y, 410, 210);
				add(pnlRight);
				tableList.add(pnlRight);
			}
			i++;
			y += 220;
		}
		lblNoDesk = new JLabel();
		ImageIcon icon = new ImageIcon(ICON_NO_DESK);
		lblNoDesk.setIcon(icon);
		lblNoDesk.setBounds(288, 235, 300, 160);
		lblNoDesk.setVisible(false);
		add(lblNoDesk);
	}

	public void refresh() {
		initData();
		if (groupList.size() == 0) {//未绑定 
			lblNoDesk.setVisible(true);
			return;
		}
		lblNoDesk.setVisible(false);
		// 遍历内存模型，绑定到物理模型
		for (int i = 0; i < groupList.size(); i++) {// 遍历分组内存模型
			Group group = groupList.get(i);
			PrepareGroupPanel tablePanel = tableList.get(i);
			tablePanel.setGroup(group);
			tablePanel.setVisible(true);
			tablePanel.setTableNumber(group.getTableNumber());
			if (group.getName() != null) {// 显示小组名称
				tablePanel.getLblGroupName().setVisible(true);
				tablePanel.getLblGroupName().setText(group.getName());
			}
			// 遍历当前组/桌的设备，内存模型
			List<PadPanel> devicePanelList = tablePanel.getDeviceList();
			List<Device> deviceList = group.getDevices();
			// 遍历设备内存模型，重绘所有pad状态
			for (int j = 0; j < deviceList.size(); j++) {
				Device device = deviceList.get(j);
				String imei = device.getImei();
				PadPanel pnlPad = devicePanelList.get(j);
				pnlPad.isOnline(app.getOnlineDevice().contains(imei));
				pnlPad.setStudents(app.getStudentByImei(imei));
				pnlPad.repaint();
				pnlPad.setVisible(true);
			}
		}
		repaint();
		revalidate();
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
}
