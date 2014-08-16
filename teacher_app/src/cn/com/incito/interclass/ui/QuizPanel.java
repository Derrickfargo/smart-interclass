package cn.com.incito.interclass.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Device;
import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Quiz;
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

	private Application app = Application.getInstance();
	/**
	 * 当前教室所有试题，初始化界面时初始化本属性
	 */
	private List<QuizGroupPanel> quizGroupList = new ArrayList<QuizGroupPanel>();
	
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
	}

	public void refresh() {
		List<Group> groupList = app.getGroupList();
		int i = 0;
		while (i < groupList.size()) {
			Group group = groupList.get(i);
			QuizGroupPanel pnlLeft = quizGroupList.get(i);
			showQuizGroup(pnlLeft, group);
			if (++i < groupList.size()) {
				QuizGroupPanel pnlRight = quizGroupList.get(i);
				showQuizGroup(pnlRight, group);
			}
			i++;
		}
	}

	private void showQuizGroup(QuizGroupPanel panel, Group group) {
		panel.setVisible(true);
		panel.setGroup(group);
		panel.getLblDesk().setText(String.format("%d号桌", group.getTableNumber()));
		panel.getLblLogo().setIcon(new ImageIcon(LogoUtils.getInstance().getLogo24(group.getLogo())));
		panel.getLblGroupName().setText(group.getName());
		
		List<Device> deviceList = group.getDevices();
		List<JPanel> quizPanel = panel.getQuizPanel();
		List<JLabel> quizList = panel.getNameList();
		for (int i = 0; i < deviceList.size(); i++) {
			Device device = deviceList.get(i);
			quizPanel.get(i).setVisible(true);
			String imei = device.getImei();
			Quiz quiz = app.getTempQuiz().get(imei);
			if (quiz != null) {
				panel.addImage(i, quiz.getThumbnail());
				JLabel lblName = quizList.get(i);
				lblName.setText(quiz.getName());
			}
		}
	}

}
