package cn.com.incito.interclass.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Quiz;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;
import cn.com.incito.server.utils.LogoUtils;

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
//	private static final String ICON_NO_DESK = "images/main/bg_binding_desk.png";
//	private JLabel lblNoDesk;
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
//		lblNoDesk = new JLabel();
//		ImageIcon icon = new ImageIcon(ICON_NO_DESK);
//		lblNoDesk.setIcon(icon);
//		lblNoDesk.setBounds(288, 235, 300, 160);
//		lblNoDesk.setVisible(false);
//		add(lblNoDesk);
	}

	private void hideGroup() {
		int i = 0;
		while (i < groupList.size()) {
			hideQuizGroup(quizGroupList.get(i));
			if (++i < groupList.size()) {
				hideQuizGroup(quizGroupList.get(i));
			}
			i++;
		}
	}
	
	private void hideQuizGroup(QuizGroupPanel panel) {
		panel.setVisible(false);
		panel.getLblLogo().setIcon(new ImageIcon(""));
		panel.getLblGroupName().setText("");
		panel.getLblGroupName().setToolTipText("");
		
		List<JPanel> quizPanel = panel.getQuizPanel();
		List<JLabel> quizList = panel.getNameList();
		List<JLabel> orderList = panel.getOrderList();
		for (int i = 0; i < quizPanel.size(); i++) {
			JLabel lblOrder = orderList.get(i);
			quizPanel.get(i).setVisible(false);
			lblOrder.setVisible(false);
			JLabel lblName = quizList.get(i);
			lblName.setText("");
			lblName.setToolTipText("");
			panel.addImage(i, null);
		}
	}
	
	public void refresh() {
		hideGroup();
		initData();
//		if (groupList.size() == 0) {//未绑定 
//			lblNoDesk.setVisible(true);
//			return;
//		}
//		lblNoDesk.setVisible(false);
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
//		panel.getLblDesk().setText(String.format("%d号桌", group.getTableNumber()));
		panel.getLblLogo().setIcon(new ImageIcon(LogoUtils.getInstance().getLogo24(group.getLogo())));
		panel.getLblGroupName().setText(group.getName());
		panel.getLblGroupName().setToolTipText(group.getName());
		
		List<Student> studentList = group.getStudents();
		List<JPanel> quizPanel = panel.getQuizPanel();
		List<JLabel> quizList = panel.getNameList();
		List<JLabel> orderList = panel.getOrderList();
		for (int i = 0; i < studentList.size(); i++) {
			Student student = studentList.get(i);
			quizPanel.get(i).setVisible(true);
			
			JLabel lblName = quizList.get(i);
			lblName.setText(student.getName());
			lblName.setToolTipText(student.getName());
			
			String imei = student.getImei();
			Quiz quiz = app.getTempQuiz().get(imei);
			if (quiz != null) {
				panel.addImage(i, quiz);
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
			} else {
				panel.addImage(i, null);
				//设置作业为排名
				JLabel lblOrder = orderList.get(i);
				lblOrder.setVisible(false);
			}
		}
	}

	private void initData() {
		groupList = new ArrayList<Group>();
		// 课桌绑定分组，生成内存模型
		Set<Group> groups = app.getGroupList();
		Iterator<Group> it = groups.iterator();
		while (it.hasNext()) {
			// 获得课桌对应的分组
			Group group = it.next();
			groupList.add(group);
		}
//		Collections.sort(groupList);
	}
}
