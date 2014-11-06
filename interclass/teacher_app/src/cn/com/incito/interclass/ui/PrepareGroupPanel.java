package cn.com.incito.interclass.ui;

import java.util.Set;

import javax.swing.JPanel;

import cn.com.incito.interclass.po.Group;
import cn.com.incito.server.api.Application;

public class PrepareGroupPanel extends JPanel {

	private static final long serialVersionUID = 882552987989905663L;
	private Application app = Application.getInstance();

	public PrepareGroupPanel() {
		setLayout(null);
		setVisible(false);
		initView();
	}

	private void initView() {
		Set<Group> groups = app.getGroupList();
		int x = 10, y = 55;
		for (int i = 0; i < 8; i++) {
			Group g = new Group();
			g.setName("测试" + i);
			GroupPanel groupPanel = new GroupPanel(g);
			groupPanel.setBounds(x, y, 825, 100);
			add(groupPanel);
			y += 110;
		}
	}

	public void refresh() {

	}
}
