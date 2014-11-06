package cn.com.incito.interclass.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.interclass.po.Student;
import cn.com.incito.server.api.Application;

public class PrepareGroupPanel extends JPanel {

	private static final long serialVersionUID = 882552987989905663L;
	private Application app = Application.getInstance();
	private List<GroupPanel> gpList = new ArrayList<GroupPanel>();

	public PrepareGroupPanel() {
		setLayout(null);
		setOpaque(true);
		initView();
		refresh();
	}

	private void initView() {
		int x = 10, y = 10;
		for (int i = 0; i < 10; i++) {
			GroupPanel groupPanel = new GroupPanel();
			groupPanel.setBounds(x, y, 825, 100);
			add(groupPanel);
			groupPanel.setVisible(false);
			gpList.add(groupPanel);
			y += 110;
		}
	}

	public void refresh() {
		Set<Group> groups = app.getGroupList();
		Iterator<Group> it = groups.iterator();
		int index = 0;
		while (it.hasNext()) {
			Group group = it.next();
			GroupPanel groupPanel = gpList.get(index);
			groupPanel.setVisible(true);
			groupPanel.setGroup(group);
			groupPanel.getLblGroupName().setText(group.getName());
			List<JLabel> lblStudents = groupPanel.getStudentList();
			List<Student> students = group.getStudents();
			for (int i = 0; i < students.size(); i++) {
				Student student = students.get(i);
				JLabel lblName = lblStudents.get(i);
				lblName.setName(student.getName());
				lblName.setVisible(true);
			}
		}
	}
}
