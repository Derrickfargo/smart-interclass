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
import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.interclass.po.Table;
import cn.com.incito.server.api.Application;

/**
 * 任务缩略图列表面板
 * 
 * @author 刘世平
 */
public class QuizPanel extends JPanel {

	/**
     *
     */
	private static final long serialVersionUID = 6316121486627261595L;
	private static final String ICON_NO_DESK = "images/main/bg_binding_desk.png";
	private JLabel lblNoDesk;
	private Application app = Application.getInstance();
	/**
	 * 当前教室所有试题，初始化界面时初始化本属性
	 */
	private List<QuizGroupPanel> quizGroupList = new ArrayList<QuizGroupPanel>();
	private List<Group> groupList = new ArrayList<Group>();
	
	public QuizPanel() {
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
			QuizGroupPanel pnlLeft = new QuizGroupPanel();
			pnlLeft.setBounds(10, y, 410, 374);
			add(pnlLeft);
			quizGroupList.add(pnlLeft);
			if (++i < 12) {
				QuizGroupPanel pnlRight = new QuizGroupPanel();
				pnlRight.setBounds(438, y, 410, 374);
				add(pnlRight);
				quizGroupList.add(pnlRight);
			}
			i++;
			y += 380;
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
		int i = 0;
		while (i < groupList.size()) {
			QuizGroupPanel pnlLeft = quizGroupList.get(i);
			showQuizGroup(pnlLeft, groupList.get(i));
			if (++i < groupList.size()) {
				QuizGroupPanel pnlRight = quizGroupList.get(i);
				showQuizGroup(pnlRight, groupList.get(i));
			}
			i++;
		}
		repaint();
		revalidate();
	}

	private void showQuizGroup(QuizGroupPanel panel, Group group) {
		panel.setVisible(true);
		panel.setGroup(group);
		panel.getLblDesk().setText(String.format("%d号桌", group.getTableNumber()));
//		panel.getLblLogo().setIcon(new ImageIcon(LogoUtils.getInstance().getLogo24(group.getLogo())));
		panel.getLblGroupName().setText(group.getName());
		
		List<Device> deviceList = group.getDevices();
		List<JPanel> quizPanel = panel.getQuizPanel();
		List<JLabel> quizList = panel.getNameList();
		List<JLabel> orderList = panel.getOrderList();
		for (int i = 0; i < deviceList.size(); i++) {
			Device device = deviceList.get(i);
			quizPanel.get(i).setVisible(true);
			String imei = device.getImei();
			Quiz quiz = app.getTempQuiz().get(imei);
			if (quiz != null) {
				panel.addImage(i, quiz);
				JLabel lblName = quizList.get(i);
				StringBuffer name = new StringBuffer();
				List<Student> students = app.getStudentByImei(imei);
				if (students != null) {
					for (Student student : students) {
						name.append(student.getName());
						name.append(",");
					}
				}
				if(name.length() != 0){
					lblName.setText(name.deleteCharAt(name.length() - 1).toString());
				}
				//设置作业为排名
				JLabel lblOrder = orderList.get(i);
				lblOrder.setVisible(true);
				int order = Application.getInstance().getQuizList().indexOf(quiz) + 1;
				lblOrder.setText(String.valueOf(order));
				switch (order) {
				case 1:
					lblOrder.setBackground(new Color(Integer.parseInt("BC3412", 16)));
					break;
				case 2:
					lblOrder.setBackground(new Color(Integer.parseInt("E07C00", 16)));
					break;
				case 3:
					lblOrder.setBackground(new Color(Integer.parseInt("F5DB00", 16)));
					break;
				default:
					lblOrder.setBackground(new Color(Integer.parseInt("ADADAD", 16)));
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
}
